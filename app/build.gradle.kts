import Dependencies.androidAppCompat
import Dependencies.androidConstraintLayout
import Dependencies.androidKtx
import Dependencies.coil
import Dependencies.exoplayer
import Dependencies.kotlinCoroutinesCore
import Dependencies.kotlinStdLib
import Dependencies.materialDesign
import Dependencies.viewPager2

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlinx-serialization")
}

android {
    compileSdkVersion(30)

    defaultConfig {
        applicationId = "erick.tijerou.storyview"
        minSdkVersion(19)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
            isTestCoverageEnabled = true
        }

        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            isZipAlignEnabled = true
            proguardFile(getDefaultProguardFile("proguard-android.txt"))
            proguardFile(file("proguard-rules.pro"))
        }
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }

    compileOptions {
        targetCompatibility = JavaVersion.VERSION_1_8
        sourceCompatibility = JavaVersion.VERSION_1_8
    }

    sourceSets {
        val commonTest = "src/commonTest/java"
        getByName("androidTest").java.srcDirs(commonTest)
        getByName("test").java.srcDirs(commonTest)
    }
}

dependencies {
    implementation(androidAppCompat)
    implementation(androidConstraintLayout)
    implementation(androidKtx)
    implementation(materialDesign)
    implementation(kotlinStdLib)
    implementation(kotlinCoroutinesCore)
    implementation(viewPager2)
    implementation(coil)
    implementation(exoplayer)
}