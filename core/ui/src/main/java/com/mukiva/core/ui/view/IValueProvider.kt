package com.mukiva.core.ui.view

interface IValueProvider {
    fun getValueByPos(pos: Int): Float
    fun getMaxValue(): Float
    fun getMinValue(): Float
    fun registerOnListSubmitted(block: () -> Unit)
    fun getFormattedValue(index: Int): String
}