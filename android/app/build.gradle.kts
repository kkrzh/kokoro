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