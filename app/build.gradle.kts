plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    //id("com.google.gms.google-services")
}

android {
    namespace = "com.example.madcamp_wk3"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.madcamp_wk3"
        minSdk = 24
        targetSdk = 33
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {


    implementation ("com.squareup.okhttp3:okhttp:4.9.3")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.3")
    //implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.6")
    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation ("androidx.cardview:cardview:1.0.0")
    implementation ("androidx.core:core:1.12.0") // 최신 버전 확인
    implementation ("androidx.multidex:multidex:2.0.1")

    implementation ("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.1")

//    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
//    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    implementation ("com.android.volley:volley:1.2.1")

//
//    implementation ("com.google.firebase:firebase-analytics:21.4.0")
//    implementation ("com.google.firebase:firebase-auth:21.5.0")
//    implementation ("com.google.firebase:firebase-database:20.3.0")
//    implementation ("com.google.firebase:firebase-storage:20.2.0")
//    implementation ("com.google.firebase:firebase-firestore:24.7.0")
}