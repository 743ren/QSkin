package top.ner347.qskin.library

import android.app.Application

class SkinManager private constructor(app : Application) {

    init {
        app.registerActivityLifecycleCallbacks(SkinLifecycleCallbacks())
    }

    companion object {
        @Volatile
        private var instance: SkinManager? = null

        fun getInstance(app : Application) : SkinManager {
            if (instance == null) {
                synchronized(SkinManager::class) {
                    if (instance == null) {
                        instance = SkinManager(app)
                    }
                }
            }
            return instance!!
        }
    }
}