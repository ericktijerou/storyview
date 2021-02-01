package com.ericktijerou.storyview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ericktijerou.storyview.databinding.ItemStoryBinding

class StoryAdapter(val pageViewListener: PageViewListener, private val list: List<StoryUserModel>) :
    RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    var currentPage = -1
     set(value) {
         notifyItemChanged(field)
         notifyItemChanged(value)
         field = value
     }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bindViews(list[position], position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(view)
    }

    private interface PagerViewHolder {
        fun bindViews(story: StoryUserModel, position: Int)
    }

    inner class StoryViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root), PagerViewHolder {
        override fun bindViews(story: StoryUserModel, position: Int) {
            binding.root.apply {
                storyUser = story
                pageViewListener = this@StoryAdapter.pageViewListener
                if (currentPage == position) {
                    startStory()
                } else {
                    stopStory()
                }
            }
        }
    }
}