plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "cn.xj.kokoro.mobile"
    compileSdk = 36
    ndkVersion = "28.1.13356709"


    defaultConfig {
        applicationId = "cn.xj.kokoro.mobile"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        ndk {
            abiFilters.clear()
            abiFilters.add("arm64-v8a")
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {

        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }


    buildFeatures {
        viewBinding =  true
        dataBinding = true
    }
    externalNativeBuild {
        cmake {
            path("src/main/jni/CMakeLists.txt")
            version = "3.22.1"
        }
    }


}

dependencies {
    implementation("com.google.guava:guava:27.1-android")


    // Selfie segmentation
    implementation ("com.google.mlkit:segmentation-selfie:16.0.0-beta6")
    // Object detection feature with bundled default classifier
    implementation ("com.google.mlkit:object-detection:17.0.2")

    // Object detection feature with custom classifier support
    implementation ("com.google.mlkit:object-detection-custom:17.0.2")

    // CameraX core library using the camera2 implementation
    val camerax_version = "1.5.0-rc01"
    // The following line is optional, as the core library is included indirectly by camera-camera2
    implementation("androidx.camera:camera-core:${camerax_version}")
    implementation("androidx.camera:camera-camera2:${camerax_version}")
    // If you want to additionally use the CameraX Lifecycle library
    implementation("androidx.camera:camera-lifecycle:${camerax_version}")
    // If you want to additionally use the CameraX VideoCapture library
    implementation("androidx.camera:camera-video:${camerax_version}")
    // If you want to additionally use the CameraX View class
    implementation("androidx.camera:camera-view:${camerax_version}")
    // If you want to additionally add CameraX ML Kit Vision Integration
    implementation("androidx.camera:camera-mlkit-vision:${camerax_version}")
    // If you want to additionally use the CameraX Extensions library
    implementation("androidx.camera:camera-extensions:${camerax_version}")


    implementation(project(":album"))
    implementation("com.github.bumptech.glide:glide:4.13.2")

    //一般用于刷新的外层滑动处理库
    //使用教程：https://github.com/scwang90/SmartRefreshLayout
    implementation  ("io.github.scwang90:refresh-layout-kernel:2.0.5")
    implementation  ("io.github.scwang90:refresh-header-classics:2.0.5")
    implementation  ("io.github.scwang90:refresh-footer-classics:2.0.5")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}