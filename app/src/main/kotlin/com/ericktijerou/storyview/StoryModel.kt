package com.ericktijerou.storyview

data class StoryModel(val url: String, val relativeTime: String)

fun StoryModel.isVideo() =  url.contains(".mp4")
