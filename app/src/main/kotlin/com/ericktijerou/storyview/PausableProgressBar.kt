package com.ericktijerou.storyview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout

class PausableProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var frontProgressView: View? = null
    private var maxProgressView: View? = null
    private var animation: PausableScaleAnimation? = null
    private var duration = DEFAULT_PROGRESS_DURATION
    private var listener: Listener? = null
    private var isStarted = false

    init {
        LayoutInflater.from(context).inflate(R.layout.pausable_progress, this)
        frontProgressView = findViewById(R.id.front_progress)
        maxProgressView = findViewById(R.id.max_progress)
    }

    fun setDuration(duration: Long) {
        this.duration = duration
        animation?.let {
            animation = null
            startProgress()
        }
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    fun setMax() {
        finishProgress(true)
    }

    fun setMin() {
        finishProgress(false)
    }

    fun setMinWithoutCallback() {
        maxProgressView?.setBackgroundResource(R.color.progress_dark)
        maxProgressView?.visible()
        animation?.let {
            it.setAnimationListener(null)
            it.cancel()
        }
    }

    fun setMaxWithoutCallback() {
        maxProgressView?.setBackgroundResource(R.color.progress_max_active)
        maxProgressView?.visible()
        animation?.let {
            it.setAnimationListener(null)
            it.cancel()
        }
    }

    private fun finishProgress(isMax: Boolean) {
        if (isMax) maxProgressView?.setBackgroundResource(R.color.progress_max_active)
        maxProgressView?.visible(isMax)
        animation?.let {
            it.setAnimationListener(null)
            it.cancel()
            listener?.onFinishProgress()
        }
    }

    fun startProgress() {
        maxProgressView?.gone()
        if (duration <= NUMBER_ZERO_FLOAT) duration = DEFAULT_PROGRESS_DURATION
        animation = PausableScaleAnimation(
            NUMBER_ZERO_FLOAT,
            NUMBER_ONE_FLOAT,
            NUMBER_ONE_FLOAT,
            NUMBER_ONE_FLOAT,
            Animation.ABSOLUTE,
            NUMBER_ZERO_FLOAT,
            Animation.RELATIVE_TO_SELF,
            NUMBER_ZERO_FLOAT
        ).apply {
            duration = duration
            interpolator = LinearInterpolator()
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {
                    if (isStarted) return
                    isStarted = true
                    frontProgressView?.visible()
                    listener?.onStartProgress()
                }

                override fun onAnimationEnd(animation: Animation) {
                    isStarted = false
                    listener?.onFinishProgress()
                }

                override fun onAnimationRepeat(animation: Animation) {
                    //Empty
                }
            })
            fillAfter = true
        }
        frontProgressView?.startAnimation(animation)
    }

    fun pauseProgress() {
            animation?.pause()
    }

    fun resumeProgress() {
            animation?.resume()
    }

    fun clear() {
        animation?.let {
            it.setAnimationListener(null)
            it.cancel()
            animation = null
        }
    }

    interface Listener {
        fun onStartProgress()
        fun onFinishProgress()
    }

    companion object {
        private const val DEFAULT_PROGRESS_DURATION = 5000L
    }
}