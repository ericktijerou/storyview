package com.ericktijerou.storyview

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StoryUserModel(val username: String, val profileUrl: String, val storyList: List<StoryModel>): Parcelable