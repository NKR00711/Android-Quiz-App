plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    id("kotlin-parcelize")
    kotlin("plugin.serialization") version "2.0.21"
    alias(libs.plugins.google.firebase.crashlytics)
//    id("androidx.navigation.safeargs.kotlin")
}

android {
    signingConfigs {
        create("nkr") {
            storeFile = file("/Users/nkr/Documents/nkr007.jks")
            storePassword = "123456"
            keyAlias = "key0"
            keyPassword = "123456"
            enableV3Signing = true
            enableV4Signing = true
        }
    }
    namespace = "com.grinstitute.quiz"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.grinstitute.quiz"
        minSdk = 24
        targetSdk = 35
        versionCode = 3
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
//            applicationIdSuffix = ".debug"
            isDebuggable = true
//            isMinifyEnabled = true
//            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("nkr")
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("nkr")
        }
//        release {
//            isMinifyEnabled = false
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
//            )
//        }
    }
    viewBinding {
        enable = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    //Update
    implementation("com.google.android.play:app-update:2.1.0")

    // For Kotlin users also import the Kotlin extensions library for Play In-App Update:
    implementation("com.google.android.play:app-update-ktx:2.1.0")
    //Image
    implementation(libs.coil)
//    implementation(libs.coil.network.okhttp)
    //sdp & ssp
    implementation(libs.sdp.android)
    //Firebase
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.database)
    //Network
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    //chart
    implementation("ir.mahozad.android:pie-chart:0.7.0")
    // Views/Fragments integration
//    implementation("androidx.navigation:navigation-fragment:$nav_version")
//    implementation("androidx.navigation:navigation-ui:$nav_version")
    // For SSP, use this:
//    implementation("com.intuit.ssp:ssp-android:1.1.1")
    //shimmer
    implementation(libs.shimmer)
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.9.0") // For LiveData
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.0") // For ViewModel
    implementation(libs.firebase.crashlytics)
//    implementation(libs.firebase.inappmessaging.display)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}