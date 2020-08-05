package top.ner347.qskin.library

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.LayoutInflater.Factory2
import android.view.View

class FactoryMerger (
    private val mF1: LayoutInflater.Factory,
    private val mF12: Factory2?,
    private val mF2: LayoutInflater.Factory,
    private val mF22: Factory2?
) :
    Factory2 {
    override fun onCreateView(
        name: String, context: Context,
        attrs: AttributeSet
    ): View? {
        val v = mF1.onCreateView(name, context, attrs)
        return v ?: mF2.onCreateView(name, context, attrs)
    }

    override fun onCreateView(
        parent: View?, name: String,
        context: Context, attrs: AttributeSet
    ): View? {
        val v =
            if (mF12 != null) mF12.onCreateView(parent, name, context, attrs) else mF1.onCreateView(
                name,
                context,
                attrs
            )
        if (v != null) return v
        return if (mF22 != null) mF22.onCreateView(
            parent,
            name,
            context,
            attrs
        ) else mF2.onCreateView(name, context, attrs)
    }
}