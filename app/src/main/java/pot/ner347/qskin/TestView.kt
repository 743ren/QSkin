package pot.ner347.qskin

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import top.ner347.qskin.library.SkinResources
import top.ner347.qskin.library.SkinViewSupport

class TestView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), SkinViewSupport {
    var bgId : Int
    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TestView)
        // 如果没有设置或者直接设置的颜色值，bgId 为 0
        bgId = typedArray.getResourceId(R.styleable.TestView_bg, 0)
        setBackgroundColor(id)
        typedArray.recycle()
    }

    override fun applySkinChange() {
        if (bgId != 0) {
            SkinResources.getInstance()?.getColor(bgId)?.let {
                setBackgroundColor(it)
            }
        }
    }
}