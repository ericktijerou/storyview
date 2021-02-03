package com.ericktijerou.storyview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

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

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun Int?.orZero(): Int = this ?: 0

fun <T : Fragment> T.withArguments(vararg params: Pair<String, Any?>): T {
    arguments = bundleOf(*params)
    return this
}