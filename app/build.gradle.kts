plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    id("kotlin-parcelize")
    kotlin("plugin.serialization") version "2.0.21"
//    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.grinstitute.quiz"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.grinstitute.quiz"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}