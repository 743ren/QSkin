package top.ner347.qskin.library

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.ArrayMap
import android.view.LayoutInflater

class SkinLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
    private val layoutInflaterFactories: ArrayMap<Activity, SkinFactory> = ArrayMap()

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        val factory2 = layoutInflaterFactories[activity]
        SkinManager.getInstance(activity.application).deleteObserver(factory2)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        val layoutInflater = LayoutInflater.from(activity)
        try {
            val field = LayoutInflater::class.java.getDeclaredField("mFactorySet")
            field.isAccessible = true
            field.setBoolean(layoutInflater, false)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val factory2 = SkinFactory(activity, SkinThemeUtils.getTypeface(activity))
        layoutInflater.factory2 = factory2
        layoutInflaterFactories[activity] = factory2
        SkinManager.getInstance(activity.application).addObserver(factory2)

        // 修改状态栏、导航栏
        SkinThemeUtils.updateNavigationBarColor(activity)
        SkinThemeUtils.updateStatusBarColor(activity)
    }

    override fun onActivityResumed(activity: Activity) {
    }

}