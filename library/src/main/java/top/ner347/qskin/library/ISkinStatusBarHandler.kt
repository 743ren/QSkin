package top.ner347.qskin.library

import android.app.Activity

/**
 * 调用者自己处理状态栏的变化
 */
interface ISkinStatusBarHandler {
    fun changeStatusBar(activity: Activity)
}