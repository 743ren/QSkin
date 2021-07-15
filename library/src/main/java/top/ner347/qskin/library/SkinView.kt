package top.ner347.qskin.library

import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat

// 记录属性名和其对应属性值
data class AttributeNameAndValue(var attrName: String, var resId: Int)

// 一个类，参数是 View 和需要被修改的属性，对外暴露一个方法，去真正修改 View 的属性
class SkinView(
    private val view: View,
    private val attributeNameAndValues: List<AttributeNameAndValue>) {

    fun applySkinChange(typeface: Typeface?) {
        applyTypeFace(typeface) // 更改字体文件
        applySkinSupport() // 更改自定义View属性
        for (attributeNameAndValue in attributeNameAndValues) {
            var left: Drawable? = null
            var top: Drawable? = null
            var right: Drawable? = null
            var bottom: Drawable? = null

            when (attributeNameAndValue.attrName) {
                "background" -> {
                    SkinResources.getBackground(attributeNameAndValue.resId)?.let {
                        if (it is Int) {
                            view.setBackgroundColor(it)
                        } else {
                            ViewCompat.setBackground(view, it as Drawable)
                        }
                    }
                }
                "src" -> {
                    SkinResources.getBackground(attributeNameAndValue.resId)?.let {
                        if (it is Int) {
                            (view as ImageView).setImageDrawable(ColorDrawable((it)))
                        } else {
                            (view as ImageView).setImageDrawable(it as Drawable)
                        }
                    }
                }
                "textColor" -> {
                    SkinResources.getColorStateList(attributeNameAndValue.resId)?.let {
                        (view as TextView).setTextColor(it)
                    }
                }
                "drawableLeft" -> left = SkinResources.getDrawable(attributeNameAndValue.resId)
                "drawableTop" -> top = SkinResources.getDrawable(attributeNameAndValue.resId)
                "drawableRight" -> right = SkinResources.getDrawable(attributeNameAndValue.resId)
                "drawableBottom" -> bottom = SkinResources.getDrawable(attributeNameAndValue.resId)
                "skinTypeface" -> applyTypeFace(SkinResources.getTypeface(attributeNameAndValue.resId))
            }
            if (null != left || null != right || null != top || null != bottom) {
                (view as TextView).setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom)
            }
        }
    }

    private fun applyTypeFace(typeface: Typeface?) {
        if (view is TextView && typeface != null) {
            view.typeface = typeface
        }
    }

    private fun applySkinSupport() {
        if (view is SkinViewSupport) {
            (view as SkinViewSupport).applySkinChange()
        }
    }
}