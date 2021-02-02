package com.ericktijerou.storyview

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StoryModel(val url: String, val relativeTime: String): Parcelable

fun StoryModel.isVideo() =  url.contains(".mp4")
