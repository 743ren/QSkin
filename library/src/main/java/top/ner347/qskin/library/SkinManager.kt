package top.ner347.qskin.library

import android.app.Application
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import android.text.TextUtils
import java.io.File
import java.util.*

class SkinManager private constructor(private val app : Application) : Observable() {
    private var skinPreference : SkinPreference = SkinPreference.init(app)
    private var skinResources: SkinResources = SkinResources.init(app)

    init {
        app.registerActivityLifecycleCallbacks(SkinLifecycleCallbacks())

        // 加载皮肤
        changeSkin(skinPreference.getSkin())
    }

    companion object {
        @Volatile
        private var instance: SkinManager? = null

        @JvmStatic
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
        if (TextUtils.isEmpty(skinPath) || !File(skinPath!!).exists()) {
            skinPreference.setSkin("")
            skinResources.reset() // 使用默认皮肤，清空一些
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
                skinPreference.setSkin(skinPath)

                // 获取皮肤包包名
                val packageName = app.packageManager
                    .getPackageArchiveInfo(skinPath!!, PackageManager.GET_ACTIVITIES)
                    ?.packageName
                skinResources.applySkin(skinResource, packageName)
            } catch (e: Exception) {
                e.printStackTrace()
                skinPreference.setSkin("")
                skinResources.reset()
            }
        }
        // 通知所有观察者
        setChanged()
        notifyObservers(null)
    }
}