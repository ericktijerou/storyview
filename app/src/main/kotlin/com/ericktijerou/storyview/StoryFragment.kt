package com.ericktijerou.storyview

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import coil.load
import coil.transform.CircleCropTransformation
import com.ericktijerou.storyview.databinding.FragmentStoryBinding
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.util.Util

class StoryFragment : Fragment(),
    StoryProgressView.StoryListener {

    private val position: Int by
    lazy { arguments?.getInt(POSITION) ?: 0 }

    private val storyUser: StoryUserModel by lazy {
        (arguments?.getParcelable<StoryUserModel>(
            STORY_USER
        ) as StoryUserModel)
    }

    private val stories: List<StoryModel> by lazy { storyUser.storyList }

    private var simpleExoPlayer: SimpleExoPlayer? = null
    private var pageListener: PageListener? = null
    private var subStoryPosition = 0
    private var pressTime = 0L
    private var limit = 500L
    private var onResumeCalled = false
    private var onVideoPrepared = false

    private var _binding: FragmentStoryBinding? = null
    private val binding get() = _binding!!
    private val headerBinding get() = binding.storyHeader

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pvVideo.useController = false
        startStory()
        initView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.pageListener = context as PageListener
    }

    override fun onStart() {
        super.onStart()
        subStoryPosition = restorePosition()
    }

    override fun onResume() {
        super.onResume()
        onResumeCalled = true
        if (stories[subStoryPosition].isVideo() && !onVideoPrepared) {
            simpleExoPlayer?.playWhenReady = false
            return
        }

        simpleExoPlayer?.seekTo(5)
        simpleExoPlayer?.playWhenReady = true
        if (subStoryPosition == 0) {
            headerBinding.storiesProgressView.startStories()
        } else {
            subStoryPosition = MainActivity.progressState.get(arguments?.getInt(POSITION) ?: 0)
            headerBinding.storiesProgressView.startStories(subStoryPosition)
        }
    }

    override fun onPause() {
        super.onPause()
        simpleExoPlayer?.playWhenReady = false
        headerBinding.storiesProgressView.leave()
    }

    override fun onComplete() {
        simpleExoPlayer?.release()
        pageListener?.onNextPageView()
    }

    override fun onPrev() {
        if (subStoryPosition - 1 < 0) return
        --subStoryPosition
        savePosition(subStoryPosition)
        startStory()
    }

    override fun onNext() {
        if (stories.size <= subStoryPosition + 1) {
            return
        }
        ++subStoryPosition
        savePosition(subStoryPosition)
        startStory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        simpleExoPlayer?.release()
    }

    private fun startStory() {
        simpleExoPlayer?.stop()
        with(binding) {
            if (stories[subStoryPosition].isVideo()) {
                pvVideo.visible()
                ivImage.gone()
                progress.visible()
                initializePlayer()
            } else {
                pvVideo.gone()
                progress.gone()
                ivImage.visible()
                ivImage.load(stories[subStoryPosition].url)
                headerBinding.tvTime.text = stories[subStoryPosition].relativeTime
            }
        }
    }

    private fun initializePlayer() {
        simpleExoPlayer?.release()
        simpleExoPlayer = SimpleExoPlayer.Builder(requireContext()).build()
        StoryApp.simpleCache?.let { cache ->
            val defaultHttpDataSourceFactory = DefaultHttpDataSourceFactory(
                Util.getUserAgent(
                    requireContext(),
                    Util.getUserAgent(
                        requireContext(),
                        requireContext().getString(R.string.app_name)
                    )
                )
            )
            val mediaDataSourceFactory = CacheDataSource.Factory().setCache(cache)
                .setUpstreamDataSourceFactory(defaultHttpDataSourceFactory)
            val mediaSource =
                ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(
                    MediaItem.fromUri(stories[subStoryPosition].url)
                )
            simpleExoPlayer?.setMediaSource(mediaSource, false)
            simpleExoPlayer?.prepare()
            simpleExoPlayer?.playWhenReady = true
        }

        binding.pvVideo.setShutterBackgroundColor(Color.BLACK)
        binding.pvVideo.player = simpleExoPlayer

        simpleExoPlayer?.addListener(object : Player.EventListener {
            override fun onPlayerError(error: ExoPlaybackException) {
                super.onPlayerError(error)
                binding.progress.gone()
                if (subStoryPosition == stories.size.minus(1)) {
                    pageListener?.onNextPageView()
                } else {
                    headerBinding.storiesProgressView.skip()
                }
            }

            override fun onIsLoadingChanged(isLoading: Boolean) {
                super.onIsLoadingChanged(isLoading)
                if (isLoading) {
                    binding.progress.visible()
                    pressTime = System.currentTimeMillis()
                    pauseCurrentStory()
                } else {
                    binding.progress.gone()
                    headerBinding.storiesProgressView.getProgressWithIndex(subStoryPosition)
                        .setDuration(simpleExoPlayer?.duration ?: 8000L)
                    onVideoPrepared = true
                    resumeCurrentStory()
                }
            }
        })
    }

    private fun initView() {
        val touchListener = object : OnSwipeTouchListener(activity!!) {
            override fun onSwipeTop() {
                Toast.makeText(activity, "onSwipeTop", Toast.LENGTH_LONG).show()
            }

            override fun onSwipeBottom() {
                Toast.makeText(activity, "onSwipeBottom", Toast.LENGTH_LONG).show()
            }

            override fun onClick(view: View) {
                when (view) {
                    binding.vNext -> {
                        if (subStoryPosition == stories.size - 1) {
                            pageListener?.onNextPageView()
                        } else {
                            headerBinding.storiesProgressView.skip()
                        }
                    }
                    binding.vPrevious -> {
                        if (subStoryPosition == 0) {
                            pageListener?.onBackPageView()
                        } else {
                            headerBinding.storiesProgressView.reverse()
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

        with(headerBinding) {
            binding.vPrevious.setOnTouchListener(touchListener)
            binding.vNext.setOnTouchListener(touchListener)

            storiesProgressView.setStoryCountDebug(
                stories.size, position = arguments?.getInt(POSITION) ?: -1
            )
            storiesProgressView.setAllStoryDuration(4000L)
            storiesProgressView.setStoryListener(this@StoryFragment)
            ivPicture.load(storyUser.profileUrl) {
                transformations(CircleCropTransformation())
            }
            tvUsername.text = storyUser.username
        }
    }

    private fun showStoryOverlay() {
        if (headerBinding.storyHeader.alpha != 0F) return
        headerBinding.storyHeader.animate()
            .setDuration(100)
            .alpha(1F)
            .start()
    }

    private fun hideStoryOverlay() {
        if (headerBinding.storyHeader.alpha != 1F) return
        headerBinding.storyHeader.animate()
            .setDuration(200)
            .alpha(0F)
            .start()
    }

    private fun savePosition(pos: Int) {
        MainActivity.progressState.put(position, pos)
    }

    private fun restorePosition(): Int {
        return MainActivity.progressState.get(position)
    }

    fun pauseCurrentStory() {
        simpleExoPlayer?.playWhenReady = false
        headerBinding.storiesProgressView.pause()
    }

    fun resumeCurrentStory() {
        if (onResumeCalled) {
            simpleExoPlayer?.playWhenReady = true
            showStoryOverlay()
            headerBinding.storiesProgressView.resume()
        }
    }

    companion object {
        private const val POSITION = "EXTRA_POSITION"
        private const val STORY_USER = "EXTRA_STORY_USER"
        fun newInstance(position: Int, story: StoryUserModel) = StoryFragment().withArguments(
            POSITION to position,
            STORY_USER to story
        )
    }
}