package com.ericktijerou.storyview

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import coil.load
import coil.transform.CircleCropTransformation
import com.ericktijerou.storyview.databinding.ViewStoryBinding
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.util.Util

class StoryView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle), StoryProgressView.StoryListener {

    var position: Int = NUMBER_ZERO
    var storyUser: StoryUserModel? = null
        set(value) {
            field = value
            setUpUi()
        }
    private var simpleExoPlayer: SimpleExoPlayer? = null
    private lateinit var mediaDataSourceFactory: DataSource.Factory
    var pageViewListener: PageViewListener? = null
    private var counter = 0
    private var pressTime = 0L
    private var limit = 500L
    private var onVideoPrepared = false
    private val binding: ViewStoryBinding

    init {
        LayoutInflater.from(context).inflate(R.layout.view_story, this, true)
        binding = ViewStoryBinding.inflate(LayoutInflater.from(context), this, true)
        binding.storyDisplayVideo.useController = false
        setUpUi()
    }

    fun startStory() {
        //counter = restorePosition()
        storyUser?.let {
            simpleExoPlayer?.seekTo(5)
            simpleExoPlayer?.playWhenReady = true
            if (counter == 0) {
                binding.storiesProgressView.startStories()
            } else {
                // restart animation
                counter = MainActivity.progressState.get(position)
                binding.storiesProgressView.startStories(counter)
            }
        }
        updateStory()
    }

    fun stopStory() {
        simpleExoPlayer?.playWhenReady = false
        binding.storiesProgressView.leave()
        simpleExoPlayer?.release()
    }

    override fun onComplete() {
        simpleExoPlayer?.release()
        pageViewListener?.onNextPageView()
    }

    override fun onPrev() {
        if (counter - 1 < 0) return
        --counter
        savePosition(counter)
        updateStory()
    }

    override fun onNext() {
        storyUser?.let {
            if (it.storyList.size <= counter + 1) {
                return
            }
            ++counter
            savePosition(counter)
            updateStory()
        }
    }

    private fun updateStory() {
        simpleExoPlayer?.stop()
        storyUser?.let {
            binding.apply {
                if (it.storyList[counter].isVideo()) {
                    storyDisplayVideo.visible()
                    storyDisplayImage.gone()
                    storyDisplayVideoProgress.visible()
                    initializePlayer()
                } else {
                    storyDisplayVideo.gone()
                    storyDisplayVideoProgress.gone()
                    storyDisplayImage.visible()
                    storyDisplayImage.load(it.storyList[counter].url)
                }
                storyDisplayTime.text = it.storyList[counter].relativeTime
            }
        }
    }

    private fun initializePlayer() {
        if (simpleExoPlayer == null) {
            simpleExoPlayer = SimpleExoPlayer.Builder(context).build()
        } else {
            simpleExoPlayer?.release()
            simpleExoPlayer = null
            simpleExoPlayer = SimpleExoPlayer.Builder(context).build()
        }

        storyUser?.let {
            StoryApp.simpleCache?.let { cache ->
                val defaultHttpDataSourceFactory = DefaultHttpDataSourceFactory(
                    Util.getUserAgent(
                        context,
                        Util.getUserAgent(context, context.getString(R.string.app_name))
                    )
                )
                mediaDataSourceFactory = CacheDataSource.Factory().setCache(cache)
                    .setUpstreamDataSourceFactory(defaultHttpDataSourceFactory)
                val mediaSource =
                    ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(
                        MediaItem.fromUri(it.storyList[counter].url)
                    )
                simpleExoPlayer?.setMediaSource(mediaSource, false)
                simpleExoPlayer?.prepare()
                simpleExoPlayer?.playWhenReady = true
            }


            binding.apply {
                storyDisplayVideo.setShutterBackgroundColor(Color.BLACK)
                storyDisplayVideo.player = simpleExoPlayer

                simpleExoPlayer?.addListener(object : Player.EventListener {
                    override fun onPlayerError(error: ExoPlaybackException) {
                        super.onPlayerError(error)
                        storyDisplayVideoProgress.gone()
                        if (counter == it.storyList.size.minus(1)) {
                            pageViewListener?.onNextPageView()
                        } else {
                            storiesProgressView.skip()
                        }
                    }

                    override fun onIsLoadingChanged(isLoading: Boolean) {
                        super.onIsLoadingChanged(isLoading)
                        if (isLoading) {
                            storyDisplayVideoProgress.visible()
                            pressTime = System.currentTimeMillis()
                            pauseCurrentStory()
                        } else {
                            storyDisplayVideoProgress.gone()
                            storiesProgressView.getProgressWithIndex(counter)
                                .setDuration(simpleExoPlayer?.duration ?: 8000L)
                            onVideoPrepared = true
                            resumeCurrentStory()
                        }
                    }
                })
            }
        }
    }

    private fun setUpUi() {
        val touchListener = object : OnSwipeTouchListener(context) {
            override fun onSwipeTop() {
                Toast.makeText(context, "onSwipeTop", Toast.LENGTH_LONG).show()
            }

            override fun onSwipeBottom() {
                Toast.makeText(context, "onSwipeBottom", Toast.LENGTH_LONG).show()
            }

            override fun onClick(view: View) {
                when (view) {
                    binding.next -> {
                        if (counter == storyUser?.storyList?.size.orZero() - 1) {
                            pageViewListener?.onNextPageView()
                        } else {
                            binding.storiesProgressView.skip()
                        }
                    }
                    binding.previous -> {
                        if (counter == 0) {
                            pageViewListener?.onBackPageView()
                        } else {
                            binding.storiesProgressView.reverse()
                        }
                    }
                }
            }

            override fun onLongClick() {
                hideStoryOverlay()
            }

            override fun onTouchView(view: View, event: MotionEvent): Boolean {
                super.onTouchView(view, event)
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        pressTime = System.currentTimeMillis()
                        pauseCurrentStory()
                        return false
                    }
                    MotionEvent.ACTION_UP -> {
                        showStoryOverlay()
                        resumeCurrentStory()
                        return limit < System.currentTimeMillis() - pressTime
                    }
                }
                return false
            }
        }

        binding.apply {
            previous.setOnTouchListener(touchListener)
            next.setOnTouchListener(touchListener)
            storiesProgressView.setStoryCountDebug(
                storyUser?.storyList?.size.orZero(), position = position
            )
            storiesProgressView.setAllStoryDuration(4000L)
            storiesProgressView.setStoryListener(this@StoryView)
            storyUser?.let {
                storyDisplayProfilePicture.load(it.profileUrl) {
                    transformations(CircleCropTransformation())
                }
                storyDisplayNick.text = it.username
            }
        }
    }

    private fun showStoryOverlay() {
        binding.apply {
            if (storyOverlay.alpha != NUMBER_ZERO_FLOAT) return
            storyOverlay.animate()
                .setDuration(100)
                .alpha(1F)
                .start()
        }
    }

    private fun hideStoryOverlay() {
        binding.apply {
            if (storyOverlay.alpha != NUMBER_ONE_FLOAT) return
            storyOverlay.animate()
                .setDuration(200)
                .alpha(0F)
                .start()
        }
    }

    private fun savePosition(pos: Int) {
        MainActivity.progressState.put(position, pos)
    }

    private fun restorePosition(): Int {
        return MainActivity.progressState.get(position)
    }

    fun pauseCurrentStory() {
        simpleExoPlayer?.playWhenReady = false
        binding.storiesProgressView.pause()
    }

    fun resumeCurrentStory() {
        simpleExoPlayer?.playWhenReady = true
        showStoryOverlay()
        binding.storiesProgressView.resume()
    }
}