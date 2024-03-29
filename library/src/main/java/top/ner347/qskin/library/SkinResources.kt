package top.ner347.qskin.library

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.TextUtils

object SkinResources {

    var appResources: Resources? = null // app 本身的 Resources
        private set
    var skinResources: Resources? = null // 皮肤包的 Resources
        private set
    private var skinPkgName: String? = null
    var isDefaultSkin = true
        private set

    fun init(context: Context) {
        appResources = context.resources
    }

    /**
     * 重置皮肤包的 Resource 和包名，用默认皮肤
     */
    fun reset() {
        skinResources = null
        skinPkgName = ""
        isDefaultSkin = true
    }

    /**
     * 应用皮肤包的 Resource 和包名
     */
    fun applySkin(resources: Resources?, pkgName: String?) {
        skinResources = resources
        skinPkgName = pkgName
        // 是否使用默认皮肤
        isDefaultSkin = TextUtils.isEmpty(pkgName) || resources == null
    }

    fun getIdentifier(resId: Int): Int {
        if (isDefaultSkin) {
            return resId
        }
        // 先从 app 本身的 resources.arsc 中读取这个 id 对应的 Name 和 Resource Type
        val resName = appResources?.getResourceEntryName(resId)
        val resType = appResources?.getResourceTypeName(resId)
        // 再根据这个 Name 和 Resource Type 读出在皮肤包的 resources.arsc 中对应的 id
        return skinResources?.getIdentifier(resName, resType, skinPkgName)?:0
    }

    /**
     * 读取皮肤包的资源
     */
    fun getColor(resId: Int): Int? {
        if (isDefaultSkin) {
            return appResources?.getColor(resId)
        }
        // 读取皮肤包中的 id
        val skinId = getIdentifier(resId)
        return if (skinId == 0) {
            // 如果皮肤包中没有这个资源，就还用 app 本身的
            appResources?.getColor(resId)
        } else skinResources?.getColor(skinId)
    }

    fun getColorStateList(resId: Int): ColorStateList? {
        if (isDefaultSkin) {
            return appResources?.getColorStateList(resId)
        }
        val skinId = getIdentifier(resId)
        return if (skinId == 0) {
            appResources?.getColorStateList(resId)
        } else skinResources?.getColorStateList(skinId)
    }

    fun getDrawable(resId: Int): Drawable? {
        if (isDefaultSkin) {
            return appResources?.getDrawable(resId)
        }
        val skinId = getIdentifier(resId)
        return if (skinId == 0) {
            appResources?.getDrawable(resId)
        } else skinResources?.getDrawable(skinId)
    }

    /**
     * 可能是Color 也可能是drawable
     */
    fun getBackground(resId: Int): Any? {
        val resourceTypeName: String? = appResources?.getResourceTypeName(resId)
        return if (resourceTypeName == "color") {
            getColor(resId)
        } else {
            getDrawable(resId)
        }
    }

    fun getString(resId: Int): String? {
        if (isDefaultSkin) {
            return appResources?.getString(resId)
        }
        val skinId = getIdentifier(resId)
        return if (skinId == 0) {
            appResources?.getString(resId)
        } else skinResources?.getString(skinId)
    }

    fun getTypeface(skinTypefaceId: Int): Typeface {
        // 使用者没有在主题全局配置
        if (skinTypefaceId == 0) {
            return Typeface.DEFAULT
        }
        val skinTypefacePath = getString(skinTypefaceId)
        // 没有在 strings.xml 中写字体路径
        if (TextUtils.isEmpty(skinTypefacePath)) {
            return Typeface.DEFAULT
        }
        try {
            return if (isDefaultSkin) {
                // 在 app 的 assets 中找这个字体文件
                Typeface.createFromAsset(appResources?.assets, skinTypefacePath)
            } else {
                // 在皮肤包的 assets 中找这个字体文件
                Typeface.createFromAsset(skinResources?.assets, skinTypefacePath)
            }
        } catch (e: Exception) {
        }
        // 如果上面没能返回成功一个默认字体
        return Typeface.DEFAULT
    }

}