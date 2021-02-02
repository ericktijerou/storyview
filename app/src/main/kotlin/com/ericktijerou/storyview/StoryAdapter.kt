package com.ericktijerou.storyview

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class StoryAdapter(fragmentActivity: FragmentActivity, private val storyList: List<StoryUserModel>) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = storyList.size

    override fun createFragment(position: Int): Fragment {
        return StoryFragment.newInstance(position, storyList[position])
    }
}