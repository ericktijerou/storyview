import Dependencies.androidAppCompat
import Dependencies.androidConstraintLayout
import Dependencies.androidCore
import Dependencies.androidKtx
import Dependencies.kotlinCoroutinesAndroid
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
    compileSdkVersion(29)

    defaultConfig {
        applicationId = "erick.tijerou.socialapp"
        minSdkVersion(19)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
            isTestCoverageEnabled = true

            buildConfigField(
                "String",
                "API_URL",
                "\"https://randomuser.me/\""
            )
        }

        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            isZipAlignEnabled = true

            proguardFile(getDefaultProguardFile("proguard-android.txt"))
            proguardFile(file("proguard-rules.pro"))

            buildConfigField(
                "String",
                "API_URL",
                "\"https://randomuser.me/\""
            )
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

kapt {
    useBuildCache = true
}

dependencies {
    implementation(androidAppCompat)
    implementation(androidConstraintLayout)
    implementation(androidCore)
    implementation(androidKtx)
    implementation(materialDesign)
    implementation(kotlinStdLib)
    implementation(kotlinCoroutinesAndroid)
    implementation(kotlinCoroutinesCore)
    implementation(viewPager2)
}