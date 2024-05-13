package com.mukiva.openweather.ui

import com.mukiva.core.ui.R
import com.mukiva.core.ui.databinding.LayListStatesBinding

fun LayListStatesBinding.emptyView(msg: String) {
    loadIndicator.gone()
    button.gone()
    image.gone()
    message.apply {
        text = msg
        visible()
    }
    root.visible()
}

fun LayListStatesBinding.error(
    msg: String,
    buttonText: String,
    onButtonClick: () -> Unit
) {
    loadIndicator.gone()
    button.apply {
        text = buttonText
        setOnClickListener { onButtonClick() }
        visible()
    }
    image.apply {
        setImageResource(R.drawable.ic_error)
        visible()
    }
    message.apply {
        text = msg
        visible()
    }
    root.visible()
}

fun LayListStatesBinding.hide() { root.gone() }

fun LayListStatesBinding.loading() {
    button.gone()
    message.gone()
    image.gone()
    loadIndicator.visible()
    root.visible()
}

fun LayListStatesBinding.notify(
    msg: String,
    buttonMsg: String,
    action: () -> Unit

) {
    message.apply {
        text = msg
        visible()
    }
    button.apply {
        setOnClickListener { action() }
        text = buttonMsg
        visible()
    }
    image.gone()
    loadIndicator.gone()
    root.visible()
}
