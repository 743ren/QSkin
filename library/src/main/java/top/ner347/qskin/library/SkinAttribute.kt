package top.ner347.qskin.library

import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.widget.TextView

class SkinAttribute(private val typeface: Typeface?) {

    // 需要替换的属性名称集合
    private val mAttributes: MutableList<String> = mutableListOf();

    // 对应 SkinView 要处理
    init {
        mAttributes.add("background")
        mAttributes.add("src")
        mAttributes.add("textColor")
        mAttributes.add("drawableLeft")
        mAttributes.add("drawableTop")
        mAttributes.add("drawableRight")
        mAttributes.add("drawableBottom")
        mAttributes.add("skinTypeface")
    }

    private val skinViews: MutableList<SkinView> = mutableListOf()

    /**
     * 对外提供的方法，参数是一个 View 和它的属性
     */
    fun load(view: View, attrs: AttributeSet) {
        val attributeNameAndValues: MutableList<AttributeNameAndValue> = mutableListOf()
        for (i in 0 until attrs.attributeCount) {
            val attributeName = attrs.getAttributeName(i) // 获取属性名
            if (!mAttributes.contains(attributeName)) continue // 不需要替换值的属性名，跳过
            val attributeValue = attrs.getAttributeValue(i) // 属性值
            if (attributeValue.startsWith("#")) continue // #ff0000 这种写死的值，无需替换，跳过
            val resId = if (attributeValue.startsWith("?")) {
                // android:textColor="?colorPrimary" 这种系统里面的自带值，在 R 文件里是 ?123456 这种
                val attrId = attributeValue.substring(1).toInt()
                // 拿到对应的属性值
                SkinThemeUtils.getResId(view.context, intArrayOf(attrId))[0]
            } else {
                // @drawable/x.png 这种自定义属性，在 R 文件是 @ 开头
                attributeValue.substring(1).toInt()
            }
            if (resId != 0) {
                // 将哪个属性对应哪个属性值，保存起来
                attributeNameAndValues.add(AttributeNameAndValue(attributeName, resId))
            }
        }
        // for 循环后，attributeNameAndValues 内记录了所有需要替换的属性和属性值
        // 如果是空的，但是 TextView，可能需要换字体
        if (attributeNameAndValues.isNotEmpty() || view is TextView || view is SkinViewSupport) {
            val skinView = SkinView(view, attributeNameAndValues)
            // Activity 创建后会经过 Factory2 调到这里，主动去应用当前皮肤包资源
            // 这样如果切换了皮肤后，新创建的 Activity 也能使用新的皮肤
            skinView.applySkinChange(typeface)
            // 记录下来，方便以后直接用
            skinViews.add(skinView)
        }
    }

    fun applySkin(typeface: Typeface?) {
        for (skinView in skinViews) {
            skinView.applySkinChange(typeface)
        }
    }
}