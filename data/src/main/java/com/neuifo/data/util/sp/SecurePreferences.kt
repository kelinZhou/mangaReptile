/*
 * Copyright (C) 2015, Scott Alexander-Bown, Daniel Abraham
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.neuifo.data.util.sp

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Build
import android.preference.PreferenceManager
import android.provider.Settings
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import java.io.UnsupportedEncodingException
import java.security.GeneralSecurityException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

/**
 * Wrapper class for Android's [SharedPreferences] interface, which adds a
 * layer of encryption to the persistent storage and retrieval of sensitive
 * key-value pairs of primitive data types.
 *
 *
 * This class provides important - but nevertheless imperfect - protection
 * against simple attacks by casual snoopers. It is crucial to remember that
 * even encrypted data may still be susceptible to attacks, especially on rooted devices
 *
 *
 * Recommended to use with user password, in which case the key will be derived from the password and not stored in the file.
 *
 *
 * TODO: Handle OnSharedPreferenceChangeListener
 */
class SecurePreferences private constructor(
    context: Context,
    secretKey: com.neuifo.data.util.sp.AesCbcWithIntegrity.SecretKeys?,
    password: String?,
    salt: String?,
    sharedPrefFilename: String?
) : SharedPreferences {
    //the backing pref file
    private var sharedPreferences: SharedPreferences? = null

    //secret keys used for enc and dec
    private var keys: com.neuifo.data.util.sp.AesCbcWithIntegrity.SecretKeys? = null

    //the salt used for enc and dec
    private val salt: String?

    //name of the currently loaded sharedPrefFile, can be null if default
    private var sharedPrefFilename: String? = null

    /**
     * User password defaults to app generated password that's stores obfucated with the other preference values. Also this uses the Default shared pref file
     *
     * @param context should be ApplicationContext not Activity
     */
    @JvmOverloads
    constructor(
        context: Context,
        password: String? = "",
        sharedPrefFilename: String? = null
    ) : this(context, password, null, sharedPrefFilename)

    /**
     * @param context            should be ApplicationContext not Activity
     * @param secretKey          that you've generated
     * @param sharedPrefFilename name of the shared pref file. If null use the default shared prefs
     */
    constructor(
        context: Context,
        secretKey: com.neuifo.data.util.sp.AesCbcWithIntegrity.SecretKeys?,
        sharedPrefFilename: String?
    ) : this(context, secretKey, null, null, sharedPrefFilename) {
    }

    /**
     * @param context        should be ApplicationContext not Activity
     */
    constructor(
        context: Context,
        password: String?,
        salt: String?,
        sharedPrefFilename: String?
    ) : this(context, null, password, salt, sharedPrefFilename) {
    }

    /**
     * if a prefFilename is not defined the getDefaultSharedPreferences is used.
     *
     * @param context should be ApplicationContext not Activity
     */
    private fun getSharedPreferenceFile(
        context: Context,
        prefFilename: String?
    ): SharedPreferences {
        sharedPrefFilename = prefFilename
        return if (TextUtils.isEmpty(prefFilename)) {
            PreferenceManager
                .getDefaultSharedPreferences(context)
        } else {
            context.getSharedPreferences(prefFilename, Context.MODE_PRIVATE)
        }
    }

    /**
     * nulls in memory keys
     */
    fun destroyKeys() {
        keys = null
    }

    /**
     * Uses device and application values to generate the pref key for the encryption key
     *
     * @param context        should be ApplicationContext not Activity
     * @return String to be used as the AESkey Pref key
     * @throws GeneralSecurityException if something goes wrong in generation
     */
    @Throws(GeneralSecurityException::class)
    private fun generateAesKeyName(context: Context): String? {
        val password = context.packageName
        val salt = getSalt(context)!!.toByteArray()
        val generatedKeyName: com.neuifo.data.util.sp.AesCbcWithIntegrity.SecretKeys =
            com.neuifo.data.util.sp.AesCbcWithIntegrity.generateKeyFromPassword(password, salt)
        return hashPrefKey(generatedKeyName.toString())
    }

    /**
     * Gets the salt value
     *
     * @param context used for accessing hardware serial number of this device in case salt is not set
     * @return
     */
    private fun getSalt(context: Context): String? {
        return if (TextUtils.isEmpty(salt)) {
            getDeviceSerialNumber(context)
        } else {
            salt
        }
    }

    private fun encrypt(cleartext: String?): String? {
        if (TextUtils.isEmpty(cleartext)) {
            return cleartext
        }
        try {
            return com.neuifo.data.util.sp.AesCbcWithIntegrity.encrypt(cleartext!!, keys!!)
                .toString()
        } catch (e: GeneralSecurityException) {
            if (isLoggingEnabled) {
                Log.w(
                    TAG,
                    "encrypt",
                    e
                )
            }
            return null
        } catch (e: UnsupportedEncodingException) {
            if (isLoggingEnabled) {
                Log.w(
                    TAG,
                    "encrypt",
                    e
                )
            }
        }
        return null
    }

    /**
     * @param ciphertext
     * @return decrypted plain text, unless decryption fails, in which case null
     */
    private fun decrypt(ciphertext: String?): String? {
        if (TextUtils.isEmpty(ciphertext)) {
            return ciphertext
        }
        try {
            val cipherTextIvMac: com.neuifo.data.util.sp.AesCbcWithIntegrity.CipherTextIvMac =
                com.neuifo.data.util.sp.AesCbcWithIntegrity.CipherTextIvMac(ciphertext!!)
            return com.neuifo.data.util.sp.AesCbcWithIntegrity.decryptString(cipherTextIvMac, keys!!)
        } catch (e: GeneralSecurityException) {
            if (isLoggingEnabled) {
                Log.w(
                    TAG,
                    "decrypt",
                    e
                )
            }
        } catch (e: UnsupportedEncodingException) {
            if (isLoggingEnabled) {
                Log.w(
                    TAG,
                    "decrypt",
                    e
                )
            }
        }
        return null
    }

    /**
     * @return map of with decrypted values (excluding the key if present)
     */
    override fun getAll(): Map<String, String?> {
        //wont be null as per http://androidxref.com/5.1.0_r1/xref/frameworks/base/core/java/android/app/SharedPreferencesImpl.java
        val encryptedMap = sharedPreferences!!.all
        val decryptedMap: MutableMap<String, String?> =
            HashMap(
                encryptedMap.size
            )
        for ((key, cipherText) in encryptedMap) {
            try {
                //don't include the key
                if (cipherText != null && cipherText != keys.toString()) {
                    //the prefs should all be strings
                    decryptedMap[key] = decrypt(cipherText.toString())
                }
            } catch (e: Exception) {
                if (isLoggingEnabled) {
                    Log.w(
                        TAG,
                        "error during getAll",
                        e
                    )
                }
                // Ignore issues that unencrypted values and use instead raw cipher text string
                decryptedMap[key] = cipherText.toString()
            }
        }
        return decryptedMap
    }

    override fun getString(key: String, defaultValue: String): String {
        val encryptedValue = sharedPreferences!!.getString(
            hashPrefKey(key), null
        )
        val decryptedValue = decrypt(encryptedValue)
        return if (encryptedValue != null && decryptedValue != null) {
            decryptedValue
        } else {
            defaultValue
        }
    }

    /**
     * Added to get a values as as it can be useful to store values that are
     * already encrypted and encoded
     *
     * @param key          pref key
     * @param defaultValue
     * @return Encrypted value of the key or the defaultValue if no value exists
     */
    fun getEncryptedString(key: String, defaultValue: String?): String {
        val encryptedValue = sharedPreferences!!.getString(
            hashPrefKey(key), null
        )
        return (encryptedValue ?: defaultValue)!!
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    override fun getStringSet(
        key: String,
        defaultValues: Set<String>
    ): Set<String> {
        val encryptedSet = sharedPreferences!!.getStringSet(
            hashPrefKey(key), null
        )
            ?: return defaultValues
        val decryptedSet: MutableSet<String> =
            HashSet(
                encryptedSet.size
            )
        for (encryptedValue in encryptedSet) {
            decryptedSet.add(decrypt(encryptedValue)!!)
        }
        return decryptedSet
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        val encryptedValue = sharedPreferences!!.getString(
            hashPrefKey(key), null
        )
            ?: return defaultValue
        return try {
            decrypt(encryptedValue)!!.toInt()
        } catch (e: NumberFormatException) {
            throw ClassCastException(e.message)
        }
    }

    override fun getLong(key: String, defaultValue: Long): Long {
        val encryptedValue = sharedPreferences!!.getString(
            hashPrefKey(key), null
        )
            ?: return defaultValue
        return try {
            decrypt(encryptedValue)!!.toLong()
        } catch (e: NumberFormatException) {
            throw ClassCastException(e.message)
        }
    }

    override fun getFloat(key: String, defaultValue: Float): Float {
        val encryptedValue = sharedPreferences!!.getString(
            hashPrefKey(key), null
        )
            ?: return defaultValue
        return try {
            decrypt(encryptedValue)!!.toFloat()
        } catch (e: NumberFormatException) {
            throw ClassCastException(e.message)
        }
    }

    override fun getBoolean(
        key: String,
        defaultValue: Boolean
    ): Boolean {
        val encryptedValue = sharedPreferences!!.getString(
            hashPrefKey(key), null
        )
            ?: return defaultValue
        return try {
            java.lang.Boolean.parseBoolean(decrypt(encryptedValue))
        } catch (e: NumberFormatException) {
            throw ClassCastException(e.message)
        }
    }

    override fun contains(key: String): Boolean {
        return sharedPreferences!!.contains(
            hashPrefKey(
                key
            )
        )
    }

    /**
     * Cycle through the unencrypt all the current prefs to mem cache, clear, then encypt with key generated from new password.
     * This method can be used if switching from the generated key to a key derived from user password
     *
     *
     * Note: the pref keys will remain the same as they are SHA256 hashes.
     *
     * @param newPassword
     * @param context        should be ApplicationContext not Activity
     */
    @SuppressLint("CommitPrefEdits")
    @Throws(GeneralSecurityException::class)
    fun handlePasswordChange(
        newPassword: String,
        context: Context
    ) {
        val salt = getSalt(context)!!.toByteArray()
        val newKey: com.neuifo.data.util.sp.AesCbcWithIntegrity.SecretKeys =
            com.neuifo.data.util.sp.AesCbcWithIntegrity.generateKeyFromPassword(newPassword, salt)
        val allOfThePrefs = sharedPreferences!!.all
        val unencryptedPrefs: MutableMap<String, String?> =
            HashMap(allOfThePrefs.size)
        //iterate through the current prefs unencrypting each one
        for (prefKey in allOfThePrefs.keys) {
            val prefValue = allOfThePrefs[prefKey]
            if (prefValue is String) {
                //all the encrypted values will be Strings
                val plainTextPrefValue = decrypt(prefValue)
                unencryptedPrefs[prefKey] = plainTextPrefValue
            }
        }

        //destroy and clear the current pref file
        destroyKeys()
        val editor = sharedPreferences!!.edit()
        editor.clear()
        editor.apply()

        //refresh the sharedPreferences object ref: I found it was retaining old ref/values
        sharedPreferences = null
        sharedPreferences = getSharedPreferenceFile(context, sharedPrefFilename)

        //assign new key
        keys = newKey
        val updatedEditor = sharedPreferences!!.edit()

        //iterate through the unencryptedPrefs encrypting each one with new key
        for (prefKey in unencryptedPrefs.keys) {
            val prefPlainText = unencryptedPrefs[prefKey]
            updatedEditor.putString(prefKey, encrypt(prefPlainText))
        }
        updatedEditor.apply()
    }

    override fun edit(): Editor {
        return Editor()
    }

    /**
     * Wrapper for Android's [SharedPreferences.Editor].
     *
     *
     * Used for modifying values in a [SecurePreferences] object. All
     * changes you make in an editor are batched, and not copied back to the
     * original [SecurePreferences] until you call [.commit] or
     * [.apply].
     */
    inner class Editor : SharedPreferences.Editor {
        private val mEditor: SharedPreferences.Editor
        override fun putString(
            key: String,
            value: String
        ): SharedPreferences.Editor {
            mEditor.putString(
                hashPrefKey(key),
                encrypt(value)
            )
            return this
        }

        /**
         * This is useful for storing values that have be encrypted by something
         * else or for testing
         *
         * @param key   - encrypted as usual
         * @param value will not be encrypted
         * @return
         */
        fun putUnencryptedString(
            key: String,
            value: String?
        ): SharedPreferences.Editor {
            mEditor.putString(
                hashPrefKey(key),
                value
            )
            return this
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        override fun putStringSet(
            key: String,
            values: Set<String>
        ): SharedPreferences.Editor {
            val encryptedValues: MutableSet<String?> =
                HashSet(
                    values.size
                )
            for (value in values) {
                encryptedValues.add(encrypt(value))
            }
            mEditor.putStringSet(
                hashPrefKey(key),
                encryptedValues
            )
            return this
        }

        override fun putInt(key: String, value: Int): SharedPreferences.Editor {
            mEditor.putString(
                hashPrefKey(key),
                encrypt(Integer.toString(value))
            )
            return this
        }

        override fun putLong(
            key: String,
            value: Long
        ): SharedPreferences.Editor {
            mEditor.putString(
                hashPrefKey(key),
                encrypt(java.lang.Long.toString(value))
            )
            return this
        }

        override fun putFloat(
            key: String,
            value: Float
        ): SharedPreferences.Editor {
            mEditor.putString(
                hashPrefKey(key),
                encrypt(java.lang.Float.toString(value))
            )
            return this
        }

        override fun putBoolean(
            key: String,
            value: Boolean
        ): SharedPreferences.Editor {
            mEditor.putString(
                hashPrefKey(key),
                encrypt(java.lang.Boolean.toString(value))
            )
            return this
        }

        override fun remove(key: String): SharedPreferences.Editor {
            mEditor.remove(hashPrefKey(key))
            return this
        }

        override fun clear(): SharedPreferences.Editor {
            mEditor.clear()
            return this
        }

        override fun commit(): Boolean {
            return mEditor.commit()
        }

        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        override fun apply() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                mEditor.apply()
            } else {
                commit()
            }
        }

        /**
         * Constructor.
         */
        init {
            mEditor = sharedPreferences!!.edit()
        }
    }

    override fun registerOnSharedPreferenceChangeListener(
        listener: OnSharedPreferenceChangeListener
    ) {
        sharedPreferences?.registerOnSharedPreferenceChangeListener(listener)
    }

    /**
     * @param listener    OnSharedPreferenceChangeListener
     * @param decryptKeys Callbacks receive the "key" parameter decrypted
     */
    fun registerOnSharedPreferenceChangeListener(
        listener: OnSharedPreferenceChangeListener, decryptKeys: Boolean
    ) {
        if (!decryptKeys) {
            registerOnSharedPreferenceChangeListener(listener)
        }
    }

    override fun unregisterOnSharedPreferenceChangeListener(
        listener: OnSharedPreferenceChangeListener
    ) {
        sharedPreferences?.unregisterOnSharedPreferenceChangeListener(listener)
    }

    companion object {
        var isLoggingEnabled = false
        private val TAG =
            SecurePreferences::class.java.name

        /**
         * Gets the hardware serial number of this device.
         *
         * @return serial number or Settings.Secure.ANDROID_ID if not available.
         */
        @SuppressLint("HardwareIds")
        private fun getDeviceSerialNumber(context: Context): String {
            // We're using the Reflection API because Build.SERIAL is only available
            // since API Level 9 (Gingerbread, Android 2.3).
            return try {
                val deviceSerial = Build::class.java.getField("SERIAL")[null] as String
                if (TextUtils.isEmpty(deviceSerial)) {
                    Settings.Secure.getString(
                        context.contentResolver,
                        Settings.Secure.ANDROID_ID
                    )
                } else {
                    deviceSerial
                }
            } catch (ignored: Exception) {
                // Fall back  to Android_ID
                Settings.Secure.getString(
                    context.contentResolver,
                    Settings.Secure.ANDROID_ID
                )
            }
        }

        /**
         * The Pref keys must be same each time so we're using a hash to obscure the stored value
         *
         * @param prefKey
         * @return SHA-256 Hash of the preference key
         */
        fun hashPrefKey(prefKey: String): String? {
            val digest: MessageDigest
            try {
                digest = MessageDigest.getInstance("SHA-256")
                val bytes = prefKey.toByteArray(charset("UTF-8"))
                digest.update(bytes, 0, bytes.size)
                return Base64.encodeToString(
                    digest.digest(),
                    com.neuifo.data.util.sp.AesCbcWithIntegrity.BASE64_FLAGS
                )
            } catch (e: NoSuchAlgorithmException) {
                if (isLoggingEnabled) {
                    Log.w(
                        TAG,
                        "Problem generating hash",
                        e
                    )
                }
            } catch (e: UnsupportedEncodingException) {
                if (isLoggingEnabled) {
                    Log.w(
                        TAG,
                        "Problem generating hash",
                        e
                    )
                }
            }
            return null
        }

    }

    init {
        if (sharedPreferences == null) {
            sharedPreferences = getSharedPreferenceFile(context, sharedPrefFilename)
        }
        this.salt = salt
        if (secretKey != null) {
            keys = secretKey
        } else if (TextUtils.isEmpty(password)) {
            // Initialize or create encryption key
            try {
                val key = generateAesKeyName(context)
                val keyAsString = sharedPreferences!!.getString(key, null)
                if (keyAsString == null) {
                    keys = com.neuifo.data.util.sp.AesCbcWithIntegrity.generateKey()
                    //saving new key
                    val committed =
                        sharedPreferences!!.edit().putString(key, keys.toString()).commit()
                    if (!committed) {
                        Log.w(
                            TAG,
                            "Key not committed to prefs"
                        )
                    }
                } else {
                    keys = com.neuifo.data.util.sp.AesCbcWithIntegrity.keys(keyAsString)
                }
                if (keys == null) {
                    throw GeneralSecurityException("Problem generating Key")
                }
            } catch (e: GeneralSecurityException) {
                if (isLoggingEnabled) {
                    Log.e(
                        TAG,
                        "Error connectWithUrl:" + e.message
                    )
                }
                throw IllegalStateException(e)
            }
        } else {
            //use the password to generate the key
            keys = try {
                val saltBytes = getSalt(context)!!.toByteArray()
                com.neuifo.data.util.sp.AesCbcWithIntegrity.generateKeyFromPassword(
                    password!!,
                    saltBytes
                )
            } catch (e: GeneralSecurityException) {
                if (isLoggingEnabled) {
                    Log.e(
                        TAG,
                        "Error connectWithUrl using user password:" + e.message
                    )
                }
                throw IllegalStateException(e)
            }
        }
    }
}