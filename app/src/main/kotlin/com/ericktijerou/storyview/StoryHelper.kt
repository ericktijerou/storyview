package com.ericktijerou.storyview

import android.text.format.DateUtils
import java.util.concurrent.TimeUnit
import kotlin.random.Random

object StoryHelper {

    fun generateDataSet(): List<StoryUserModel> {
        val list = mutableListOf<StoryUserModel>()
        for (i in 1..10) {
            list.add(
                StoryUserModel(
                    username = getRandomAlphaNumericString(10),
                    profileUrl = pictureList[Random.nextInt(pictureList.size)],
                    storyList = generateStoryList()
                )
            )
        }
        return list
    }

    private fun generateStoryList(): List<StoryModel> {
        val storySize = Random.nextInt(1, 5)
        val list = mutableListOf<StoryModel>()
        for (i in 0 until storySize) {
            val timeInMilliseconds = System.currentTimeMillis() - (1 * (24 - i) * 60 * 60 * 1000)
            list.add(
                StoryModel(
                    url = mediaList[Random.nextInt(mediaList.size)],
                    relativeTime = getRelativeTime(timeInMilliseconds)
                )
            )
        }
        return list.toList()
    }

    private fun getRelativeTime(timeInMilliseconds: Long): String {
        val now = System.currentTimeMillis()
        val difference = now - timeInMilliseconds
        val relativeTime = when {
            difference < DateUtils.MINUTE_IN_MILLIS -> "${TimeUnit.MILLISECONDS.toSeconds(difference)} s"
            difference < DateUtils.HOUR_IN_MILLIS -> "${TimeUnit.MILLISECONDS.toMinutes(difference)} min"
            difference < DateUtils.DAY_IN_MILLIS -> "${TimeUnit.MILLISECONDS.toHours(difference)} h"
            difference < DateUtils.WEEK_IN_MILLIS -> DateUtils.getRelativeTimeSpanString(
                timeInMilliseconds,
                now,
                DateUtils.DAY_IN_MILLIS
            )
            else -> DateUtils.getRelativeTimeSpanString(
                timeInMilliseconds,
                now,
                DateUtils.WEEK_IN_MILLIS
            )
        }
        return relativeTime.toString()
    }

    private val mediaList = listOf(
        "https://player.vimeo.com/external/289189952.sd.mp4?s=756cbea276c653d18bc7141d8458693936225dd9&profile_id=165&oauth2_token_id=57447761",
        "https://firebasestorage.googleapis.com/v0/b/testvideo-91d3a.appspot.com/o/4.mp4?alt=media&token=517ad60c-ca28-400e-ab46-49fb8c122d75",
        "https://firebasestorage.googleapis.com/v0/b/testvideo-91d3a.appspot.com/o/1.mp4?alt=media&token=36032747-7815-473d-beef-061098f08c18",
        "https://firebasestorage.googleapis.com/v0/b/testvideo-91d3a.appspot.com/o/3.mp4?alt=media&token=a7ccda22-7264-4c64-9328-86a4c2ec31cd",
        "https://firebasestorage.googleapis.com/v0/b/testvideo-91d3a.appspot.com/o/2.mp4?alt=media&token=b6218221-6699-402b-8b89-7e3354ac32dc",
        "https://firebasestorage.googleapis.com/v0/b/testvideo-91d3a.appspot.com/o/5.mp4?alt=media&token=965a0494-7aaf-4248-85c5-fefac581ee7f",
        "https://firebasestorage.googleapis.com/v0/b/testvideo-91d3a.appspot.com/o/7.mp4?alt=media&token=2f6a3c9b-bfc4-483e-ad5b-bb7d539ee765",
        "https://firebasestorage.googleapis.com/v0/b/testvideo-91d3a.appspot.com/o/8.mp4?alt=media&token=87e20ffd-2b5c-422a-ad85-33b90b4e2169",
        "https://firebasestorage.googleapis.com/v0/b/testvideo-91d3a.appspot.com/o/9.mp4?alt=media&token=83911bd2-6083-43d1-824e-2049f1fb11e7",
        "https://images.pexels.com/photos/3723033/pexels-photo-3723033.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
        "https://images.pexels.com/photos/3727251/pexels-photo-3727251.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500",
        "https://images.pexels.com/photos/3290067/pexels-photo-3290067.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500",
        "https://images.pexels.com/photos/3727260/pexels-photo-3727260.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500",
        "https://images.pexels.com/photos/3723037/pexels-photo-3723037.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500",
        "https://images.pexels.com/photos/1257245/pexels-photo-1257245.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500",
        "https://images.pexels.com/photos/1289118/pexels-photo-1289118.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500",
        "https://images.pexels.com/photos/868096/pexels-photo-868096.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500",
        "https://images.pexels.com/photos/5560867/pexels-photo-5560867.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500",
        "https://images.pexels.com/photos/4753928/pexels-photo-4753928.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500",
        "https://images.pexels.com/photos/4498150/pexels-photo-4498150.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500",
        "https://images.pexels.com/photos/5613958/pexels-photo-5613958.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500",
        "https://images.pexels.com/photos/4254899/pexels-photo-4254899.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500",
        "https://images.pexels.com/photos/2703202/pexels-photo-2703202.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500",
        "https://images.pexels.com/photos/1736200/pexels-photo-1736200.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500",
        "https://images.pexels.com/photos/3571569/pexels-photo-3571569.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500"
    )

    private val pictureList = listOf(
        "https://randomuser.me/api/portraits/women/1.jpg",
        "https://randomuser.me/api/portraits/men/1.jpg",
        "https://randomuser.me/api/portraits/women/2.jpg",
        "https://randomuser.me/api/portraits/men/2.jpg",
        "https://randomuser.me/api/portraits/women/3.jpg",
        "https://randomuser.me/api/portraits/men/3.jpg",
        "https://randomuser.me/api/portraits/women/4.jpg",
        "https://randomuser.me/api/portraits/men/4.jpg",
        "https://randomuser.me/api/portraits/women/5.jpg",
        "https://randomuser.me/api/portraits/men/5.jpg",
        "https://randomuser.me/api/portraits/women/6.jpg",
        "https://randomuser.me/api/portraits/men/6.jpg",
        "https://randomuser.me/api/portraits/women/7.jpg",
        "https://randomuser.me/api/portraits/men/7.jpg",
        "https://randomuser.me/api/portraits/women/8.jpg",
        "https://randomuser.me/api/portraits/men/8.jpg",
        "https://randomuser.me/api/portraits/women/9.jpg",
        "https://randomuser.me/api/portraits/men/9.jpg",
        "https://randomuser.me/api/portraits/women/10.jpg",
        "https://randomuser.me/api/portraits/men/10.jpg",
        "https://randomuser.me/api/portraits/women/11.jpg",
        "https://randomuser.me/api/portraits/men/11.jpg",
    )
}