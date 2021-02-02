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
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.ericktijerou.storyview.databinding.FragmentStoryBinding
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
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
    private lateinit var mediaDataSourceFactory: DataSource.Factory
    private var pageViewListener: PageViewListener? = null
    private var counter = 0
    private var pressTime = 0L
    private var limit = 500L
    private var onResumeCalled = false
    private var onVideoPrepared = false

    private var _binding: FragmentStoryBinding? = null
    private val binding get() = _binding!!

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
        binding.storyDisplayVideo.useController = false
        updateStory()
        setUpUi()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.pageViewListener = context as PageViewListener
    }

    override fun onStart() {
        super.onStart()
        counter = restorePosition()
    }

    override fun onResume() {
        super.onResume()
        onResumeCalled = true
        if (stories[counter].isVideo() && !onVideoPrepared) {
            simpleExoPlayer?.playWhenReady = false
            return
        }

        simpleExoPlayer?.seekTo(5)
        simpleExoPlayer?.playWhenReady = true
        if (counter == 0) {
            binding.storiesProgressView.startStories()
        } else {
            counter = MainActivity.progressState.get(arguments?.getInt(POSITION) ?: 0)
            binding.storiesProgressView.startStories(counter)
        }
    }

    override fun onPause() {
        super.onPause()
        simpleExoPlayer?.playWhenReady = false
        binding.storiesProgressView.leave()
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
        if (stories.size <= counter + 1) {
            return
        }
        ++counter
        savePosition(counter)
        updateStory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        simpleExoPlayer?.release()
    }

    private fun updateStory() {
        simpleExoPlayer?.stop()
        with(binding) {
            if (stories[counter].isVideo()) {
                storyDisplayVideo.visible()
                storyDisplayImage.gone()
                storyDisplayVideoProgress.visible()
                initializePlayer()
            } else {
                storyDisplayVideo.gone()
                storyDisplayVideoProgress.gone()
                storyDisplayImage.visible()
                storyDisplayImage.load(stories[counter].url)
                binding.storyDisplayTime.text = stories[counter].relativeTime
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
            mediaDataSourceFactory = CacheDataSource.Factory().setCache(cache)
                .setUpstreamDataSourceFactory(defaultHttpDataSourceFactory)
            val mediaSource =
                ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(
                    MediaItem.fromUri(stories[counter].url)
                )
            simpleExoPlayer?.setMediaSource(mediaSource, false)
            simpleExoPlayer?.prepare()
            simpleExoPlayer?.playWhenReady = true
        }

        binding.storyDisplayVideo.setShutterBackgroundColor(Color.BLACK)
        binding.storyDisplayVideo.player = simpleExoPlayer

        simpleExoPlayer?.addListener(object : Player.EventListener {
            override fun onPlayerError(error: ExoPlaybackException) {
                super.onPlayerError(error)
                binding.storyDisplayVideoProgress.gone()
                if (counter == stories.size.minus(1)) {
                    pageViewListener?.onNextPageView()
                } else {
                    binding.storiesProgressView.skip()
                }
            }

            override fun onIsLoadingChanged(isLoading: Boolean) {
                super.onIsLoadingChanged(isLoading)
                if (isLoading) {
                    binding.storyDisplayVideoProgress.visible()
                    pressTime = System.currentTimeMillis()
                    pauseCurrentStory()
                } else {
                    binding.storyDisplayVideoProgress.gone()
                    binding.storiesProgressView.getProgressWithIndex(counter)
                        .setDuration(simpleExoPlayer?.duration ?: 8000L)
                    onVideoPrepared = true
                    resumeCurrentStory()
                }
            }
        })
    }

    private fun setUpUi() {
        val touchListener = object : OnSwipeTouchListener(activity!!) {
            override fun onSwipeTop() {
                Toast.makeText(activity, "onSwipeTop", Toast.LENGTH_LONG).show()
            }

            override fun onSwipeBottom() {
                Toast.makeText(activity, "onSwipeBottom", Toast.LENGTH_LONG).show()
            }

            override fun onClick(view: View) {
                when (view) {
                    binding.next -> {
                        if (counter == stories.size - 1) {
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

        with(binding) {
            previous.setOnTouchListener(touchListener)
            next.setOnTouchListener(touchListener)

            storiesProgressView.setStoryCountDebug(
                stories.size, position = arguments?.getInt(POSITION) ?: -1
            )
            storiesProgressView.setAllStoryDuration(4000L)
            storiesProgressView.setStoryListener(this@StoryFragment)
            storyDisplayProfilePicture.load(storyUser.profileUrl) {
                transformations(CircleCropTransformation())
            }
            storyDisplayNick.text = storyUser.username
        }
    }

    private fun showStoryOverlay() {
        if (binding.storyOverlay.alpha != 0F) return
        binding.storyOverlay.animate()
            .setDuration(100)
            .alpha(1F)
            .start()
    }

    private fun hideStoryOverlay() {
        if (binding.storyOverlay.alpha != 1F) return
        binding.storyOverlay.animate()
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
        binding.storiesProgressView.pause()
    }

    fun resumeCurrentStory() {
        if (onResumeCalled) {
            simpleExoPlayer?.playWhenReady = true
            showStoryOverlay()
            binding.storiesProgressView.resume()
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