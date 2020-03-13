package com.neuifo.mangareptile.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.view.*
import android.widget.*
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AlertDialog
import com.kelin.okpermission.Renewable
import com.neuifo.data.util.DeviceUtils
import com.neuifo.mangareptile.R
import com.neuifo.mangareptile.domain.util.DialogHelper
import io.reactivex.Observable
import java.text.DecimalFormat

/**
 * Created by neuifo on 2017/5/8.
 */

object StyleHelper {

    private val sDialogMap = HashMap<Context, Dialog>()

    fun showCommonErrorDialog(
        context: Activity,
        title: CharSequence?,
        msg: CharSequence,
        btnPositiveText: String = "确定",
        dismissListener: (isSure: Boolean) -> Unit
    ) {
        showCommonDialog(
            context,
            R.drawable.ic_dialog_error,
            title,
            msg, null, btnPositiveText, false, dismissListener
        )
    }

    fun showCommonDialog(
        context: Activity, @DrawableRes icon: Int,
        title: CharSequence?,
        msg: CharSequence,
        btnNegativeText: String? = "取消",
        btnPositiveText: String = "确定",
        cancelable: Boolean = false,
        dismissListener: (isSure: Boolean) -> Unit
    ) {
        val dialog = DialogHelper.Builder(context)
            .setContentView(R.layout.dialog_error_hint)
            .setStyle(R.style.CommonWidgetDialog)
            .setCancelable(cancelable)
            .setSize(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            .setGravity(Gravity.CENTER)
            .createDialog()
        if (icon != 0) {
            val ivIcon = dialog.findViewById<ImageView>(R.id.ivIcon)
            ivIcon.visibility = View.VISIBLE
            ivIcon.setImageResource(icon)
        }
        val tvTitle = dialog.findViewById<TextView>(R.id.tvTitle)
        val tvMsg = dialog.findViewById<TextView>(R.id.tvMsg)
        if (title != null && title.isNotEmpty()) {
            tvTitle.visibility = View.VISIBLE
            tvTitle.text = title
        } else {
            tvMsg.setTextColor(context.resources.getColor(R.color.black_333))
        }
        tvMsg.text = msg

        if (TextUtils.isEmpty(msg)) {
            tvMsg.visibility = View.GONE
        }

        val btnNegative = dialog.findViewById<Button>(R.id.btnNegative)
        val btnPositive = dialog.findViewById<Button>(R.id.btnPositive)
        if (btnNegativeText == null || btnNegativeText.isEmpty()) {
            btnNegative.visibility = View.GONE
            dialog.findViewById<View>(R.id.tvBtnSlicer).visibility = View.GONE
            btnPositive.setBackgroundResource(R.drawable.bg_dialog_error_commit)
        } else {
            btnNegative.text = btnNegativeText
        }
        btnPositive.text = btnPositiveText
        btnNegative.setOnClickListener {
            dismissListener(false)
            dialog.dismiss()
        }
        btnPositive.setOnClickListener {
            dismissListener(true)
            dialog.dismiss()
        }
        dialog.show()
    }

    fun showProgress(context: Activity, total: Float, current: Float) {
        val df = DecimalFormat("######0.00")
        var mProgressDialog: Dialog? = sDialogMap[context]
        val process = (current / total * 100).toInt()
        if (mProgressDialog != null && mProgressDialog.isShowing) {
            val progressBar = mProgressDialog.findViewById<View>(R.id.progress) as ProgressBar
            val textView = mProgressDialog.findViewById<View>(R.id.tv_percentage) as TextView
            val textrang = mProgressDialog.findViewById<View>(R.id.tv_totalrange) as TextView
            textrang.tag = df.format(current.toDouble()) + "M/" + df.format(total.toDouble()) + "M"
            progressBar.tag = process
            textView.tag = process
            textView.post {
                textView.text = textView.tag.toString() + "%"
                textView.postInvalidate()
            }
            progressBar.post {
                progressBar.progress = progressBar.tag as Int
                progressBar.postInvalidate()
            }
            textrang.post {
                textrang.text = textrang.tag as String
                textrang.postInvalidate()
            }
            return
        } else {
            mProgressDialog = DialogHelper.Builder(context)
                .setContentView(R.layout.dialog_horizontal_progress_layout)
                .setCancelable(true)
                .setStyle(R.style.CommonWidgetDialog)
                .setSize(
                    DeviceUtils.getScreenWidth(context, 0.75),
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                .setGravity(Gravity.CENTER)
                .createDialog()
        }

        val progressBar = mProgressDialog!!.findViewById<View>(R.id.progress) as ProgressBar
        val textView = mProgressDialog.findViewById<View>(R.id.tv_percentage) as TextView
        val textrang = mProgressDialog.findViewById<View>(R.id.tv_totalrange) as TextView
        textrang.text = current.toString() + "M/" + total + "M"
        progressBar.progress = process
        textView.text = process.toString() + "%"

        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.setCancelable(false)
        mProgressDialog.setOnDismissListener { sDialogMap.remove(context) }
        mProgressDialog.show()
        sDialogMap[context] = mProgressDialog
    }

    fun showProgress(context: Context?, cancelable: Boolean = true, text: String = "请稍候...") {
        if (context == null) {
            return
        }
        var dialog = sDialogMap[context]
        if (dialog == null) {
            dialog = Dialog(context, R.style.loading_dialog)
            dialog.setContentView(R.layout.loading_progress)
            val params = dialog.window!!.attributes
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
            dialog.window!!.attributes = params
            dialog.setCanceledOnTouchOutside(false)
            dialog.setCancelable(cancelable)
            dialog.setOnDismissListener { sDialogMap.remove(context) }
            dialog.show()
            sDialogMap[context] = dialog
        }

        val tv = dialog.findViewById<TextView>(R.id.tv_load_dialog)
        tv.visibility = if (text.isEmpty()) {
            View.GONE
        } else {
            tv.text = text
            View.VISIBLE
        }
    }

    fun hideProgress(context: Context?) {
        if (context == null) {
            return
        }
        val dialog = sDialogMap[context]
        if (dialog != null && dialog.isShowing) {
            dialog.dismiss()
            sDialogMap.remove(context)
        }
    }

    fun showCommonConfirmAgainDialog(
        activity: Activity,
        msg: String,
        sureText: String,
        cancelText: String
    ): Observable<Boolean> {
        val dialog = DialogHelper.Builder(activity)
            .setContentView(R.layout.dialog_common_confirm_again)
            .setCancelable(true)
            .setStyle(R.style.CommonWidgetDialog_BottomAnim)
            .setSize(-1, ViewGroup.LayoutParams.WRAP_CONTENT)
            .setGravity(Gravity.BOTTOM)
            .createDialog()
        val tvMsg = dialog.findViewById<TextView>(R.id.tvMsg)
        val btnSure = dialog.findViewById<TextView>(R.id.btnSure)
        val btnCancel = dialog.findViewById<TextView>(R.id.btnCancel)
        tvMsg.text = msg
        btnSure.text = sureText
        btnCancel.text = cancelText
        return Observable.create { t ->
            btnSure.setOnClickListener {
                t.onNext(true)
                dialog.dismiss()
            }
            btnCancel.setOnClickListener {
                t.onNext(false)
                dialog.dismiss()
            }
            dialog.setOnDismissListener {
                t.onNext(false)
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    fun showCenterConfirmAgainDialog(
        context: Activity,
        msg: String,
        sureText: String,
        cancelText: String,
        dismiss: (sure: Boolean) -> Unit
    ) {
        val dialog = DialogHelper.Builder(context)
            .setContentView(R.layout.dialog_center_confirm_again)
            .setCancelable(true)
            .setStyle(R.style.CommonWidgetDialog)
            .setSize(-1, ViewGroup.LayoutParams.WRAP_CONTENT)
            .setGravity(Gravity.CENTER)
            .createDialog()
        val tvMsg = dialog.findViewById<TextView>(R.id.tvMsg)
        val btnSure = dialog.findViewById<TextView>(R.id.btnSure)
        val btnCancel = dialog.findViewById<TextView>(R.id.btnCancel)
        tvMsg.text = msg
        btnSure.text = sureText
        btnCancel.text = cancelText
        if (cancelText.isEmpty()) {
            btnCancel.visibility = View.GONE
            btnSure.setBackgroundResource(R.drawable.bg_dialog_error_commit)
        }

        btnSure.setOnClickListener {
            dialog.dismiss()
            dismiss(true)
        }
        btnCancel.setOnClickListener {
            dialog.dismiss()
            dismiss(false)
        }
        dialog.show()
    }



    fun showMissCameraDialog(activity: Activity, renewable: Renewable) {
        AlertDialog.Builder(activity)
            .setCancelable(false)
            .setTitle("提示")
            .setMessage("使用相机前，应用需要获取相机的使用权限")
            .setNegativeButton("取消") { _, _ ->
                renewable.continueWorking(false)
            }
            .setPositiveButton("去设置") { _, _ ->
                renewable.continueWorking(true)
            }.show()
    }

    fun showMissStorageDialog(activity: Activity, renewable: Renewable, msg: String) {
        AlertDialog.Builder(activity)
            .setCancelable(false)
            .setTitle("提示")
            .setMessage(msg)
            .setNegativeButton("取消") { _, _ ->
                renewable.continueWorking(false)
            }
            .setPositiveButton("去设置") { _, _ ->
                renewable.continueWorking(true)
            }.show()
    }
}
