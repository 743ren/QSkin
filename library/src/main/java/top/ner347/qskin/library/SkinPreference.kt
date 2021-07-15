package top.ner347.qskin.library

import android.content.Context
import android.content.SharedPreferences

object SkinPreference {
    private const val SKIN_SHARED = "skins"
    private const val KEY_SKIN_PATH = "skin-path"
    private var mPref: SharedPreferences? = null

    fun init(context: Context) {
        mPref = context.getSharedPreferences(SKIN_SHARED, Context.MODE_PRIVATE)
    }

    fun setSkin(skinPath: String?) {
        mPref?.edit()?.putString(KEY_SKIN_PATH, skinPath)?.apply()
    }

    fun getSkin(): String? {
        return mPref?.getString(KEY_SKIN_PATH, null)
    }
}