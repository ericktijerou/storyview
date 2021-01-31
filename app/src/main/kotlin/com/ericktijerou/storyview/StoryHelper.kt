package com.ericktijerou.storyview

import android.text.format.DateUtils
import kotlin.random.Random

object StoryHelper {

    fun generateDataSet(): List<StoryUserModel> {
        val list = mutableListOf<StoryUserModel>()
        for (i in 1..10) {
            list.add(
                StoryUserModel(
                    username = "username$i",
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
            difference < DateUtils.MINUTE_IN_MILLIS -> DateUtils.getRelativeTimeSpanString(
                timeInMilliseconds,
                now,
                DateUtils.SECOND_IN_MILLIS
            )
            difference < DateUtils.HOUR_IN_MILLIS -> DateUtils.getRelativeTimeSpanString(
                timeInMilliseconds,
                now,
                DateUtils.MINUTE_IN_MILLIS
            )
            difference < DateUtils.DAY_IN_MILLIS -> DateUtils.getRelativeTimeSpanString(
                timeInMilliseconds,
                now,
                DateUtils.HOUR_IN_MILLIS
            )
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
        "https://player.vimeo.com/external/403295268.sd.mp4?s=3446f787cefa52e7824d6ce6501db5261074d479&profile_id=165&oauth2_token_id=57447761",
        "https://player.vimeo.com/external/409206405.sd.mp4?s=0bc456b6ff355d9907f285368747bf54323e5532&profile_id=165&oauth2_token_id=57447761",
        "https://player.vimeo.com/external/403295710.sd.mp4?s=788b046826f92983ada6e5caf067113fdb49e209&profile_id=165&oauth2_token_id=57447761",
        "https://player.vimeo.com/external/394678700.sd.mp4?s=353646e34d7bde02ad638c7308a198786e0dff8f&profile_id=165&oauth2_token_id=57447761",
        "https://player.vimeo.com/external/405333429.sd.mp4?s=dcc3bdec31c93d87c938fc6c3ef76b7b1b188580&profile_id=165&oauth2_token_id=57447761",
        "https://player.vimeo.com/external/363465031.sd.mp4?s=15b706ccd3c0e1d9dc9290487ccadc7b20fff7f1&profile_id=165&oauth2_token_id=57447761",
        "https://player.vimeo.com/external/422787651.sd.mp4?s=ec96f3190373937071ba56955b2f8481eaa10cce&profile_id=165&oauth2_token_id=57447761",
        "https://images.pexels.com/photos/1433052/pexels-photo-1433052.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
        "https://images.pexels.com/photos/1366630/pexels-photo-1366630.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
        "https://images.pexels.com/photos/1067333/pexels-photo-1067333.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
        "https://images.pexels.com/photos/1122868/pexels-photo-1122868.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
        "https://images.pexels.com/photos/1837603/pexels-photo-1837603.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
        "https://images.pexels.com/photos/1612461/pexels-photo-1612461.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
        "https://images.pexels.com/photos/1591382/pexels-photo-1591382.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
        "https://images.pexels.com/photos/53757/pexels-photo-53757.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500",
        "https://images.pexels.com/photos/134020/pexels-photo-134020.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
        "https://images.pexels.com/photos/1367067/pexels-photo-1367067.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
        "https://images.pexels.com/photos/1420226/pexels-photo-1420226.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500",
        "https://images.pexels.com/photos/2217365/pexels-photo-2217365.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
        "https://images.pexels.com/photos/2260800/pexels-photo-2260800.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
        "https://images.pexels.com/photos/1719344/pexels-photo-1719344.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
        "https://images.pexels.com/photos/364096/pexels-photo-364096.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
        "https://images.pexels.com/photos/3849168/pexels-photo-3849168.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
        "https://images.pexels.com/photos/2953901/pexels-photo-2953901.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
        "https://images.pexels.com/photos/3538558/pexels-photo-3538558.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
        "https://images.pexels.com/photos/2458400/pexels-photo-2458400.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260"
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