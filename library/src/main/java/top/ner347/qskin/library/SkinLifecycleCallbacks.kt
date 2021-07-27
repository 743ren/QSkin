package top.ner347.qskin.library

import android.app.Activity
import android.app.Application
import android.os.Build
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
        SkinManager.deleteObserver(factory2)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        val layoutInflater = LayoutInflater.from(activity)
        val factory2 = SkinFactory(activity, SkinThemeUtils.getTypeface(activity))
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            var mFactory = layoutInflater.factory
            var mFactory2 = layoutInflater.factory2
            if (mFactory == null) {
                mFactory2 = factory2
                mFactory = mFactory2
            } else {
                mFactory2 = FactoryMerger(factory2, factory2, mFactory, mFactory2)
                mFactory = mFactory2
            }
            val mFactoryField = LayoutInflater::class.java.getDeclaredField("mFactory")
            mFactoryField.isAccessible = true
            mFactoryField.set(layoutInflater, mFactory)
            val mFactoryField2 = LayoutInflater::class.java.getDeclaredField("mFactory2")
            mFactoryField2.isAccessible = true
            mFactoryField2.set(layoutInflater, mFactory2)
        } else {
            try {
                val field = LayoutInflater::class.java.getDeclaredField("mFactorySet")
                field.isAccessible = true
                field.setBoolean(layoutInflater, false)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            layoutInflater.factory2 = factory2
        }

        layoutInflaterFactories[activity] = factory2
        SkinManager.addObserver(factory2)

        // 修改状态栏、导航栏
        SkinThemeUtils.updateNavigationBarColor(activity)
        if (SkinManager.includeStatusBar) {
            SkinManager.skinStatusBarHandler.changeStatusBar(activity)
        }
    }

    override fun onActivityResumed(activity: Activity) {
    }

}