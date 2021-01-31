package com.ericktijerou.storyview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ericktijerou.storyview.StoryHelper.generateDataSet
import com.ericktijerou.storyview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), StoryOnClick {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        binding.run {
            viewPager.adapter = StoryAdapter(this@MainActivity, generateDataSet())
            viewPager.setPageTransformer(CubeOutTransformer())
        }
    }
}