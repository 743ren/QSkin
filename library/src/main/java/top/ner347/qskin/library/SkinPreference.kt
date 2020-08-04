package top.ner347.qskin.library

import android.content.Context

class SkinPreference private constructor(context: Context){

    private var mPref = context.getSharedPreferences(SKIN_SHARED, Context.MODE_PRIVATE)

    companion object {
        const val SKIN_SHARED = "skins"
        const val KEY_SKIN_PATH = "skin-path"

        @Volatile
        private var instance: SkinPreference? = null

        fun init(context: Context): SkinPreference {
            if (instance == null) {
                synchronized(SkinPreference::class) {
                    if (instance == null) {
                        instance = SkinPreference(context.applicationContext)
                    }
                }
            }
            return instance!!
        }
    }

    fun setSkin(skinPath: String?) {
        mPref.edit().putString(KEY_SKIN_PATH, skinPath).apply()
    }

    fun getSkin(): String? {
        return mPref.getString(KEY_SKIN_PATH, null)
    }
}