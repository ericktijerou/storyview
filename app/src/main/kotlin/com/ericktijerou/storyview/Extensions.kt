package com.ericktijerou.storyview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

fun ViewGroup.inflate(layoutId: Int): View {
    return LayoutInflater.from(this.context)
        .inflate(layoutId, this, false)
}

fun Any?.isNull(): Boolean = this == null
fun Any?.isNotNull(): Boolean = this != null

fun View.visible(value: Boolean = true) {
    if (value) {
        visibility = View.VISIBLE
    } else {
        gone()
    }
}

fun View.gone() {
    visibility = View.GONE
}
