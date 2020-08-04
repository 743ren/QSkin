package top.ner347.qskin.library

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat

class SkinAttribute {

    // 需要替换的属性名称集合
    private val mAttributes: MutableList<String> = mutableListOf();

    init {
        mAttributes.add("background")
        mAttributes.add("src")
        mAttributes.add("textColor")
        mAttributes.add("drawableLeft")
        mAttributes.add("drawableTop")
        mAttributes.add("drawableRight")
        mAttributes.add("drawableBottom")
    }

    private val skinViews: MutableList<SkinView> = ArrayList()

    // 封装的一个 JavaBean，记录属性名和其对应属性值
    internal class AttributeNameAndValue(var attrName: String, var resId: Int)

    /**
     * 对外提供的方法，参数是一个 View 和它的属性
     */
    fun load(view: View, attrs: AttributeSet) {
        val attributeNameAndValues: MutableList<AttributeNameAndValue> = ArrayList()
        for (i in 0 until attrs.attributeCount) {
            val attributeName: String = attrs.getAttributeName(i) // 获取属性名
            if (!mAttributes.contains(attributeName)) continue // 不需要替换值的属性名，跳过
            val attributeValue: String = attrs.getAttributeValue(i) // 属性值
            if (attributeValue.startsWith("#")) continue // #ff0000 这种写死的值，无需替换，跳过
            val resId = if (attributeValue.startsWith("?")) {
                // android:textColor="?colorPrimary" 这种系统里面的自带值，在 R 文件里是 ?123456 这种
                val attrId = attributeValue.substring(1).toInt()
                // 拿到对应的属性值
                SkinThemeUtils.getResId(view.context, intArrayOf(attrId))[0]
            } else {
                // @drawable/x.png 这种自定义属性，拿到值
                attributeValue.substring(1).toInt()
            }
            if (resId != 0) {
                val attributeNameAndValue = AttributeNameAndValue(attributeName, resId)
                // 将哪个属性对应哪个属性值，保存起来
                attributeNameAndValues.add(attributeNameAndValue)
            }
        }
        // for 循环后，attributeNameAndValues 内记录了所有需要替换的属性和属性值
        if (attributeNameAndValues.isNotEmpty()) {
            val skinView = SkinView(view, attributeNameAndValues)
            // Activity 创建后会经过 Factory2 调到这里，主动去应用当前皮肤包资源
            // 这样如果切换了皮肤后，新创建的 Activity 也能使用新的皮肤
            skinView.applySkinChange()
            // 记录下来，方便以后直接用
            skinViews.add(skinView)
        }
    }

    // 一个类，参数是 View 和需要被修改的属性，对外暴露一个方法，去真正修改 View 的属性
    internal class SkinView(
        private val view: View,
        private val attributeNameAndValues: List<AttributeNameAndValue>
    ) {
        fun applySkinChange() {
            for (attributeNameAndValue in attributeNameAndValues) {
                var left: Drawable? = null
                var top: Drawable? = null
                var right: Drawable? = null
                var bottom: Drawable? = null

                val skinResources = SkinResources.getInstance() ?: return

                when (attributeNameAndValue.attrName) {
                    "background" -> {
                        val background = skinResources.getBackground(attributeNameAndValue.resId)
                        if (background is Int) {
                            view.setBackgroundColor(background)
                        } else {
                            ViewCompat.setBackground(view, background as Drawable)
                        }
                    }
                    "src" -> {
                        val background = skinResources.getBackground(attributeNameAndValue.resId)
                        if (background is Int) {
                            (view as ImageView).setImageDrawable(ColorDrawable((background)))
                        } else {
                            (view as ImageView).setImageDrawable(background as Drawable)
                        }
                    }
                    "textColor" -> (view as TextView).setTextColor(
                        skinResources.getColorStateList(attributeNameAndValue.resId)
                    )
                    "drawableLeft" -> left = skinResources.getDrawable(attributeNameAndValue.resId)
                    "drawableTop" -> top = skinResources.getDrawable(attributeNameAndValue.resId)
                    "drawableRight" -> right = skinResources.getDrawable(attributeNameAndValue.resId)
                    "drawableBottom" -> bottom = skinResources.getDrawable(attributeNameAndValue.resId)
                    else -> {
                    }
                }
                if (null != left || null != right || null != top || null != bottom) {
                    (view as TextView).setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom)
                }
            }
        }
    }

    fun applySkin() {
        for (skinView in skinViews) {
            skinView.applySkinChange()
        }
    }
}