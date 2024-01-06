package com.mukiva.openweather.ui.view

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.search.SearchView

class CustomSearchView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SearchView(context, attributeSet, defStyleAttr) {

    private var mOnShowListener: () -> Unit = {}
    private var mOnHideListener: () -> Unit = {}

    fun setOnShowListener(block: () -> Unit) {
        mOnShowListener = block
    }

    fun setOnHideListener(block: () -> Unit) {
        mOnHideListener = block
    }

    override fun show() {
        super.show()
        mOnShowListener()
    }

    override fun hide() {
        mOnHideListener()
        super.hide()
    }

}