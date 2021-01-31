object Dependencies {
    const val androidAppCompat = "androidx.appcompat:appcompat:${Versions.androidAppCompat}"
    const val androidConstraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.androidConstraintLayout}"
    const val androidCore = "androidx.core:core:${Versions.androidCore}"
    const val androiGradlePlugin = "com.android.tools.build:gradle:${Versions.androidGradlePlugin}"
    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlinStdLib}"
    const val kotlinGradlePlugin =
        "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlinStdLib}"
    const val kotlinCoroutinesAndroid =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinCoroutines}"
    const val kotlinCoroutinesCore =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}"
    const val kotlinCoroutinesTest =
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.kotlinCoroutines}"
    const val kotlinSerializationGradlePugin =
        "org.jetbrains.kotlin:kotlin-serialization:${Versions.kotlinStdLib}"
    const val materialDesign = "com.google.android.material:material:${Versions.materialDesign}"
    const val androidKtx = "androidx.core:core-ktx:${Versions.androidKtxVersion}"
    const val viewPager2 = "androidx.viewpager2:viewpager2:${Versions.viewPager2}"
}

object Versions {
    internal const val androidAppCompat = "1.2.0"
    internal const val androidConstraintLayout = "2.0.0-beta6"
    internal const val androidCore = "1.0.2"
    internal const val androidGradlePlugin = "3.6.2"
    internal const val kotlinStdLib = "1.4.21"
    internal const val kotlinCoroutines = "1.3.9"
    internal const val materialDesign = "1.2.1"
    internal const val androidKtxVersion = "0.1"
    internal const val viewPager2 = "1.1.0"
}
