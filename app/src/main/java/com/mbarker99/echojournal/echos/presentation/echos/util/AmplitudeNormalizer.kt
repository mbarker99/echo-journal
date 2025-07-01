package com.mbarker99.echojournal.echos.presentation.echos.util

import kotlin.math.roundToInt

object AmplitudeNormalizer {

    private const val AMPLITUDE_MIN_OUTPUT_THRESHOLD = 0.1f
    private const val MIN_OUTPUT = 0.25f
    private const val MAX_OUTPUT = 1f

    fun normalize(
        sourceAmplitudes: List<Float>,
        trackWidth: Float,
        barWidth: Float,
        spacing: Float
    ): List<Float> {
        require(trackWidth >= 0f) {
            "Track width must be positive"
        }
        require(trackWidth >= barWidth + spacing) {
            "Track width too small"
        }
        if (sourceAmplitudes.isEmpty()) {
            return emptyList()
        }

        val barsCount = (trackWidth / (barWidth + spacing)).roundToInt()
        val resampled = resampleAmplitudes(sourceAmplitudes, barsCount)
        val remapped = remapAmplitudes(resampled)

        return remapped
    }

    private fun remapAmplitudes(amplitudes: List<kotlin.Float>): List<Float> {
        val outputRange = MAX_OUTPUT - MIN_OUTPUT
        val scaleFactor = MAX_OUTPUT - AMPLITUDE_MIN_OUTPUT_THRESHOLD

        return amplitudes.map { amplitude ->
            if (amplitude <= AMPLITUDE_MIN_OUTPUT_THRESHOLD) {
                MIN_OUTPUT
            } else {
                val amplitudeRange = amplitude - AMPLITUDE_MIN_OUTPUT_THRESHOLD

                MIN_OUTPUT + (amplitudeRange * outputRange / scaleFactor)
            }
        }
    }

    private fun resampleAmplitudes(sourceAmplitudes: List<Float>, targetSize: Int): List<Float> {
        return when {
            targetSize == sourceAmplitudes.size -> sourceAmplitudes
            targetSize <= sourceAmplitudes.size -> downsample(sourceAmplitudes, targetSize)
            else -> downsample(sourceAmplitudes, targetSize)
        }
    }


    private fun downsample(sourceAmplitudes: List<Float>, targetSize: Int): List<Float> {
        val ratio = sourceAmplitudes.size.toFloat() / targetSize
        return List(targetSize) { index ->
            val start = (index * ratio).toInt()
            val end = ((index + 1) * ratio).toInt().coerceAtMost(sourceAmplitudes.size)

            sourceAmplitudes.subList(start, end).max()
        }
    }

    private fun upsample(sourceAmplitudes: List<Float>, targetSize: Int): List<Float> {
        val result = mutableListOf<Float>()

        val step = (sourceAmplitudes.size - 1).toFloat() / (targetSize - 1)
        for (i in 0 until targetSize) {
            val pos = i * step
            val index = pos.toInt()

            val fraction = pos - index

            val value = if (index + 1 < sourceAmplitudes.size) {
                (1 - fraction) * sourceAmplitudes[index] + fraction * sourceAmplitudes[index + 1]
            } else {
                sourceAmplitudes[index]
            }
            result.add(value)
        }
        return result.toList()
    }
}