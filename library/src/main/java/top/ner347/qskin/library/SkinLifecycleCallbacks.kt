package top.ner347.qskin.library

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater

class SkinLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        val layoutInflater = LayoutInflater.from(activity)
        layoutInflater.factory2 = SkinFactory()
    }

    override fun onActivityResumed(activity: Activity) {
    }

}