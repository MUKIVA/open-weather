package com.github.mukiva.feature.dashboard.ui.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.github.mukiva.feature.dashboard.R
import com.github.mukiva.feature.dashboard.databinding.ViewFieldBinding

class FieldView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attributeSet, defStyleAttr, defStyleRes) {

    var text: String
        get() = mBinding.value.text.toString()
        set(value) = setValue(value)

    private val mBinding: ViewFieldBinding

    init {
        val inflater = LayoutInflater.from(context)
        mBinding = ViewFieldBinding.inflate(inflater, this, true)

        context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.Field,
            0, 0).apply {

            try {
                setLabel(getString(R.styleable.Field_label) ?: "")
                setValue(getString(R.styleable.Field_value) ?: "-")
                setImage(getDrawable(R.styleable.Field_image))
            } finally {
                recycle()
            }
        }

    }

    private fun setImage(drawable: Drawable?) = with(mBinding.label) {
        setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
    }

    private fun setLabel(label: String) = with(mBinding.label) {
        text = label
    }

    private fun setValue(value: String) = with(mBinding.value) {
        text = value
    }

}