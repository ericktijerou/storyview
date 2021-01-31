package com.ericktijerou.storyview

import android.view.animation.ScaleAnimation
import android.view.animation.Transformation

class PausableScaleAnimation(
    fromX: Float, toX: Float, fromY: Float,
    toY: Float, pivotXType: Int, pivotXValue: Float, pivotYType: Int,
    pivotYValue: Float
) : ScaleAnimation(fromX, toX, fromY, toY, pivotXType, pivotXValue, pivotYType, pivotYValue) {

    private var elapsedAtPause = DEFAULT_ELAPSED
    private var isPaused = false

    override fun getTransformation(
        currentTime: Long,
        outTransformation: Transformation?,
        scale: Float
    ): Boolean {
        if (isPaused) {
            if (elapsedAtPause == DEFAULT_ELAPSED) elapsedAtPause = currentTime - startTime
            startTime = currentTime - elapsedAtPause
        }
        return super.getTransformation(currentTime, outTransformation, scale)
    }

    fun pause() {
        if (isPaused) return
        elapsedAtPause = DEFAULT_ELAPSED
        isPaused = true
    }

    fun resume() {
        isPaused = false
    }

    companion object {
        const val DEFAULT_ELAPSED: Long = 0
    }
}