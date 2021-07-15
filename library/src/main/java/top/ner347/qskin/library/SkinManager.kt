package top.ner347.qskin.library

import android.app.Application
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import android.text.TextUtils
import java.io.File
import java.util.*

object SkinManager: Observable() {
    private var app: Application? = null

    @JvmStatic
    fun init(app: Application) {
        this.app = app
        SkinResources.init(app)
        SkinPreference.init(app)
        app.registerActivityLifecycleCallbacks(SkinLifecycleCallbacks())
        changeSkin(SkinPreference.getSkin())
    }

    /**
     * 调用这个方法切换皮肤
     */
    fun changeSkin(skinPath : String?) {
        if (TextUtils.isEmpty(skinPath) || !File(skinPath!!).exists()) {
            SkinPreference.setSkin("")
            SkinResources.reset() // 使用默认皮肤，清空
        } else {
            try {
                // 反射创建 AssetManager 与 Resource
                val assetManager = AssetManager::class.java.newInstance()
                val addAssetPath = assetManager.javaClass.getMethod(
                    "addAssetPath",
                    String::class.java
                )
                // 用资源管理器加载这个路径下的资源
                addAssetPath.invoke(assetManager, skinPath)
                val appResource = app?.resources
                // 根据当前的显示与配置(横竖屏、语言等)创建皮肤包本身的 Resources
                val skinResource = Resources(
                    assetManager,
                    appResource?.displayMetrics,
                    appResource?.configuration
                )

                // 记录下使用的皮肤包路径
                SkinPreference.setSkin(skinPath)

                // 获取皮肤包包名
                val packageName = app?.packageManager
                    ?.getPackageArchiveInfo(skinPath!!, PackageManager.GET_ACTIVITIES)
                    ?.packageName
                SkinResources.applySkin(skinResource, packageName)
            } catch (e: Exception) {
                e.printStackTrace()
                SkinPreference.setSkin("")
                SkinResources.reset()
            }
        }
        // 通知所有观察者
        setChanged()
        notifyObservers(null)
    }
}
