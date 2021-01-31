package com.ericktijerou.storyview

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class StoryAdapter(val adapterOnClick: StoryOnClick, private val list:List<StoryUserModel>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PagerViewHolder).bindViews(list[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return StoryViewHolder(parent.inflate(R.layout.item_story))
    }

    private interface PagerViewHolder {
        fun bindViews(pageModel: StoryUserModel)
    }

    private inner class StoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), PagerViewHolder {
        override fun bindViews(pageModel: StoryUserModel) = with(pageModel) {

        }
    }
}