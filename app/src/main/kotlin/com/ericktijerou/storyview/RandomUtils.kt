package com.ericktijerou.storyview

private const val RANDOM_ALPHANUMERIC_CHARS = "abcdefghiklmnopqrstuvwxyz0123456789"

internal fun getRandomAlphaNumericString(length: Int): String {
    return (1..length)
        .map { RANDOM_ALPHANUMERIC_CHARS.random() }
        .joinToString("")
}