package com.neuifo.mangareptile.data.core

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.neuifo.mangareptile.event.EventBus
import java.util.*
import kotlin.collections.ArrayList


/**
 * **描述:** 用来存放App运行时数据。
 *
 * **创建人:** kelin
 *
 * **创建时间:** 2019/3/27  6:11 PM
 *
 * **版本:** v 1.0.0
 */
@SuppressLint("StaticFieldLeak")
object AppModule {

    private val activities = HashSet<Activity>()
    private var applicationContext: Context? = null

    fun init(context: Application) {
        this.applicationContext = context.applicationContext
        context.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                activities.add(activity)
            }

            override fun onActivityResumed(activity: Activity) {
            }

            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityDestroyed(activity: Activity) {
                activities.remove(activity)
            }

            override fun onActivityStarted(activity: Activity?) {}

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {}

            override fun onActivityStopped(activity: Activity?) {}
        })
    }

    fun getContext() = applicationContext
        ?: throw RuntimeException("you must call the connectWithUrl method!")

    fun findActivityByClass(clazz: Class<out Activity>): Activity? {
        for (activity in activities) {
            if (clazz.simpleName == activity.javaClass.simpleName) {
                return activity
            }
        }
        return null
    }

    fun finishActivitiesBeside(target: Class<*>) {
        val iterator = activities.iterator()
        while (iterator.hasNext()) {
            val activity = iterator.next()
            if (target.simpleName != activity.javaClass.simpleName) {
                activity.finish()
                activities.remove(activity)
            }
        }
    }

    fun finishActivities(target: Class<*>) {
        val finished = ArrayList<Activity>()
        val iterator = activities.iterator()
        while (iterator.hasNext()) {
            val activity = iterator.next()
            if (target.simpleName == activity.javaClass.simpleName) {
                finished.add(activity)
            }
        }
        finished.forEach {
            it.finish()
            activities.remove(it)
        }
    }

    /**
     * 删除含有指定Fragment的Activity。
     * @return 过程执行中可能发生ConcurrentModificationException，如果发生就返回false否则返回true。
     */
    fun finishActivityByFragment(fragment: Class<out Fragment>) {
        activities.filter { activity ->
            if (activity is FragmentActivity) {
                val fs = activity.supportFragmentManager.fragments
                if (!fs.isNullOrEmpty()) {
                    for (f in fs) {
                        if (f.javaClass == fragment) {
                            return@filter true
                        }
                    }
                }
            }
            return@filter false
        }.iterator().forEach {
            it.finish()
            activities.remove(it)
        }
    }

    fun finishAllActivities() {
        val iterator = activities.iterator()
        while (iterator.hasNext()) {
            val activity = iterator.next()
            activity.finish()
        }
        activities.clear()
    }

    fun getActivityCount(): Int {
        return activities.size
    }

}