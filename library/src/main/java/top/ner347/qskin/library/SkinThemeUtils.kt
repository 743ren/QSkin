package top.ner347.qskin.library

import android.R
import android.app.Activity
import android.content.Context
import android.content.res.TypedArray
import android.os.Build
import androidx.annotation.RequiresApi

class SkinThemeUtils {

    companion object {
        val APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS = intArrayOf(androidx.appcompat.R.attr.colorPrimaryDark)
        val STATUS_BAR_COLOR_ATTR = intArrayOf(R.attr.statusBarColor)
        private val NAVIGATION_BAR_COLOR_ATTR = intArrayOf(R.attr.navigationBarColor)

        private val TYPEFACE_ATTR = intArrayOf(top.ner347.qskin.library.R.attr.skinTypeface)

        fun getResId(context: Context, attrs: IntArray): IntArray {
            val ints = IntArray(attrs.size)
            val typedArray: TypedArray = context.obtainStyledAttributes(attrs)
            for (i in 0 until typedArray.length()) {
                ints[i] = typedArray.getResourceId(i, 0)
            }
            typedArray.recycle()
            return ints
        }

        /**
         * 更换状态栏颜色，5.0以上才能修改
         */
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun updateStatusBarColor(activity: Activity) {
            if (!SkinManager.includeStatusBar) return

            // 当与 colorPrimaryDark 不同时，以 statusBarColor 为准
            val statusBarColorResId = getResId(activity, STATUS_BAR_COLOR_ATTR)[0]
            // 如果直接在 style 中写入固定颜色值(而不是 @color/XXX ) 获得 0
            if (statusBarColorResId != 0) {
                SkinResources.getColor(statusBarColorResId)?.let {
                    activity.window.statusBarColor = it
                }
            } else {
                // 获得 colorPrimaryDark
                val colorPrimaryDarkResId: Int = getResId(activity, APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS)[0]
                if (colorPrimaryDarkResId != 0) {
                    SkinResources.getColor(colorPrimaryDarkResId)?.let {
                        activity.window.statusBarColor = it
                    }
                }
            }
        }

        /**
         * 更换导航栏颜色
         */
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun updateNavigationBarColor(activity: Activity) {
            if (!SkinManager.includeNavigationBar) return

            val navigationBarColorResId = getResId(activity, NAVIGATION_BAR_COLOR_ATTR)[0]
            if (navigationBarColorResId != 0) {
                SkinResources.getColor(navigationBarColorResId)?.let {
                    activity.window.navigationBarColor = it
                }
            }
        }

        fun getTypeface(activity: Activity) =
            SkinResources.getTypeface(getResId(activity, TYPEFACE_ATTR)[0])
    }
}