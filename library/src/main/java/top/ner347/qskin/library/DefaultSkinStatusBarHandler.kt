package top.ner347.qskin.library

import android.app.Activity

class DefaultSkinStatusBarHandler: ISkinStatusBarHandler {
    override fun changeStatusBar(activity: Activity) {
        // 当与 colorPrimaryDark 不同时，以 statusBarColor 为准
        val statusBarColorResId = SkinThemeUtils.getResId(
            activity,
            SkinThemeUtils.STATUS_BAR_COLOR_ATTR
        )[0]
        // 如果直接在 style 中写入固定颜色值(而不是 @color/XXX ) 获得 0
        if (statusBarColorResId != 0) {
            SkinResources.getColor(statusBarColorResId)?.let {
                activity.window.statusBarColor = it
            }
        } else {
            // 获得 colorPrimaryDark
            val colorPrimaryDarkResId: Int = SkinThemeUtils.getResId(
                activity,
                SkinThemeUtils.APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS
            )[0]
            if (colorPrimaryDarkResId != 0) {
                SkinResources.getColor(colorPrimaryDarkResId)?.let {
                    activity.window.statusBarColor = it
                }
            }
        }
    }
}