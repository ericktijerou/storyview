package com.ericktijerou.storyview

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.SparseIntArray
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.ericktijerou.storyview.StoryHelper.generateDataSet
import com.ericktijerou.storyview.databinding.ActivityMainBinding
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.CacheWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), PageViewListener {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        with(binding.viewPager) {
            val items = generateDataSet()
            preLoadStories(items)
            val storyAdapter = StoryAdapter(this@MainActivity, items)
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
            setPageTransformer(CubeOutTransformer())
            adapter = storyAdapter
        }
    }

    private fun preLoadStories(items: List<StoryUserModel>) {
        val imageList = mutableListOf<String>()
        val videoList = mutableListOf<String>()

        items.forEach { storyUser ->
            storyUser.storyList.forEach { story ->
                if (story.isVideo()) {
                    videoList.add(story.url)
                } else {
                    imageList.add(story.url)
                }
            }
        }
        preLoadVideos(videoList.toList())
        preLoadImages(imageList.toList())
    }

    private fun preLoadImages(imageList: List<String>) {
        imageList.forEach { imageUrl ->
            val request = ImageRequest.Builder(this)
                .data(imageUrl)
                .memoryCachePolicy(CachePolicy.DISABLED)
                .build()
            imageLoader.enqueue(request)
        }
    }

    private fun preLoadVideos(videoList: List<String>) {
        videoList.map { data ->
            GlobalScope.launch(Dispatchers.IO) {
                val dataUri = Uri.parse(data)
                val dataSpec = DataSpec(dataUri)
                val bufferSize = ByteArray(64 * 1024)
                val cacheDataSource =
                    CacheDataSource(StoryApp.simpleCache!!, DefaultHttpDataSource())
                val listener =
                    CacheWriter.ProgressListener { requestLength: Long, bytesCached: Long, _: Long ->
                        val downloadPercentage = (bytesCached * 100.0 / requestLength)
                        Log.d("preLoadVideos", "downloadPercentage: $downloadPercentage")
                    }
                val cacheWriter = CacheWriter(
                    cacheDataSource,
                    dataSpec,
                    true,
                    bufferSize,
                    listener
                )
                try {
                    cacheWriter.cache()
                } catch (e: Exception) {
                    cacheWriter.cancel()
                    e.printStackTrace()
                }
            }
        }
    }


    override fun onBackPageView() {
        binding.viewPager.apply {
            if (currentItem > 0) {
                currentItem--
            }
        }
    }

    override fun onNextPageView() {
        binding.viewPager.apply {
            if (currentItem + 1 < adapter?.itemCount ?: 0) {
                currentItem++
            }
        }
    }

    companion object {
        val progressState = SparseIntArray()
    }
}