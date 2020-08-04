package top.ner347.qskin.library

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import java.lang.reflect.Constructor
import java.util.*
import kotlin.collections.HashMap

class SkinFactory(private val activity : Activity, typeface : Typeface?) : LayoutInflater.Factory2, Observer {
    private var skinAttribute: SkinAttribute = SkinAttribute(typeface)

    /**
     * 进行缓存起来，因为 ClassLoader getConstructor 是耗费性能的
     */
    private val sConstructorMap: HashMap<String, Constructor<out View>> = hashMapOf()

    /**
     * 例如：new TextView(context, attr)  或  new Button(context, attr)  或 new Button(context, attr) ...
     * 所以需要建立获取控件的构造方法 参数类型，好去创建构造对象
     */
    private val mConstructorSignature = arrayOf(
        Context::class.java,
        AttributeSet::class.java
    )
    /**
     * 因为系统的控件，可能拿以下控件包名去拼起来，下面总有一个是对应上的，一旦对应上就能反射成功
     */
    private val sClassPrefixList = arrayOf(
        "android.widget.",
        "android.webkit.",
        "android.app.",
        "android.view."
    )

    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        var resultView: View? = createViewFromTag(name, context, attrs)
        // 如果为null，可认为是自定义View，所以需要传入 name + "" ---> 自定义控件包名和控件名 + ""
        if (null == resultView) {
            resultView = createView(name, "", context, attrs);
        }
        resultView?.let {
            skinAttribute.load(it, attrs)
        }
        return resultView
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return null
    }

    /**
     * 创建系统控件 例如：TextView，ImageView，Button ...
     */
    private fun createViewFromTag(
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        var view: View? = null
        for (s in sClassPrefixList) {
            view = createView(name, s, context, attrs)
            if (view != null) {
                break
            }
        }
        return view
    }

    /**
     * 参考系统的 createView
     * 真正进行反射的方式创建View
     * 1.当传入 name + s  -->  控件名 + 控件包名， 需要创建系统的控件。
     * 2.当传入 name + "" -->  控件名 + ""        这个控件名就是完整的 自定义 包名+自定义控件名
     */
    private fun createView(
        name: String,
        prefix: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        var constructor: Constructor<out View>? = sConstructorMap[name]
        if (null == constructor) { // 没缓存过
            try {
                val clazz = context.classLoader.loadClass(prefix + name)
                    .asSubclass(View::class.java)
                constructor = clazz.getConstructor(*mConstructorSignature)
                constructor.isAccessible = true
                sConstructorMap[name] = constructor // 缓存构造器
                return constructor.newInstance(context, attrs)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            try {
                constructor.isAccessible = true
                return constructor.newInstance(context, attrs)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }

    override fun update(o: Observable?, arg: Any?) {
        SkinThemeUtils.updateStatusBarColor(activity)
        SkinThemeUtils.updateNavigationBarColor(activity)
        skinAttribute.applySkin(SkinThemeUtils.getTypeface(activity))
    }
}