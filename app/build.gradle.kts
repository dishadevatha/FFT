plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.project.fft"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.project.fft"
        minSdk = 34
        //noinspection OldTargetApi
        targetSdk = 34
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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.legacy.support.v4)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    //noinspection GradleDependency,UseTomlInstead
    implementation(platform("com.google.firebase:firebase-bom:33.2.0"))
    //noinspection UseTomlInstead
    implementation("com.google.firebase:firebase-analytics")
    //noinspection UseTomlInstead,GradleDependency
    implementation("com.google.firebase:firebase-firestore-ktx:24.4.0") // Use the latest version
    //noinspection UseTomlInstead
    implementation("com.github.bumptech.glide:glide:4.12.0")
    //noinspection UseTomlInstead,GradleDependency
    implementation("com.google.android.material:material:1.9.0")
    //noinspection UseTomlInstead
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    //noinspection GradleDependency,UseTomlInstead
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    // https://mvnrepository.com/artifact/com.razorpay/checkout
    implementation(libs.checkout)


}