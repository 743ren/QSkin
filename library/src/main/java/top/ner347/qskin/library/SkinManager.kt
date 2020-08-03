package top.ner347.qskin.library

import android.app.Application
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import android.text.TextUtils
import java.util.*

class SkinManager private constructor(val app : Application) : Observable() {

    init {
        SkinPreference.init(app)
        SkinResources.init(app)
        app.registerActivityLifecycleCallbacks(SkinLifecycleCallbacks())

        // 加载皮肤
        changeSkin(SkinPreference.getInstance()?.getSkin())
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

    /**
     * 调用这个方法切换皮肤
     */
    fun changeSkin(skinPath : String?) {
        if (TextUtils.isEmpty(skinPath)) {
            SkinPreference.getInstance()?.setSkin("")
            // 使用默认皮肤，清空一些
            SkinResources.getInstance()?.reset()
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
                val appResource = app.resources
                // 根据当前的显示与配置(横竖屏、语言等)创建皮肤包本身的 Resources
                val skinResource = Resources(
                    assetManager,
                    appResource.displayMetrics,
                    appResource.configuration
                )

                // 记录下使用的皮肤包路径
                SkinPreference.getInstance()?.setSkin(skinPath)

                // 获取皮肤包包名
                val mPm: PackageManager = app.packageManager
                val info = mPm.getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES)
                val packageName = info?.packageName
                SkinResources.getInstance()?.applySkin(skinResource, packageName)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        // 通知所有观察者
        setChanged()
        notifyObservers(null)
    }
}