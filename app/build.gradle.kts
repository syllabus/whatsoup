plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "net.syllabus.whatsoup"
    compileSdk = 34

    defaultConfig {
        applicationId = "net.syllabus.whatsoup"
        minSdk = 24
        targetSdk = 34
        versionCode = 4
        versionName = "0.4"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
//    implementation(".androidx.lifecycle.livedata.ktx)
//    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation("androidx.navigation:navigation-ui-ktx:2.6.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.6.0")
    implementation ("androidx.recyclerview:recyclerview:1.2.1")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.9.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.9.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
/*
[versions]
agp = "8.6.0"
kotlin = "1.9.0"
coreKtx = "1.10.1"
junit = "4.13.2"
junitVersion = "1.1.5"
espressoCore = "3.5.1"
appcompat = "1.6.1"
material = "1.10.0"
constraintlayout = "2.1.4"
lifecycleLivedataKtx = "2.6.1"
lifecycleViewmodelKtx = "2.6.1"
navigationFragmentKtx = "2.6.0"
navigationUiKtx = "2.6.0"

[libraries]
androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "coreKtx" }
junit = { group = "junit", name = "junit", version.ref = "junit" }
androidx-junit = { group = "androidx.test.ext", name = "junit", version.ref = "junitVersion" }
androidx-espresso-core = { group = "androidx.test.espresso", name = "espresso-core", version.ref = "espressoCore" }
androidx-appcompat = { group = "androidx.appcompat", name = "appcompat", version.ref = "appcompat" }
material = { group = "com.google.android.material", name = "material", version.ref = "material" }
androidx-constraintlayout = { group = "androidx.constraintlayout", name = "constraintlayout", version.ref = "constraintlayout" }
androidx-lifecycle-livedata-ktx = { group = "androidx.lifecycle", name = "lifecycle-livedata-ktx", version.ref = "lifecycleLivedataKtx" }
androidx-lifecycle-viewmodel-ktx = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-ktx", version.ref = "lifecycleViewmodelKtx" }
androidx-navigation-fragment-ktx = { group = "androidx.navigation", name = "navigation-fragment-ktx", version.ref = "navigationFragmentKtx" }
androidx-navigation-ui-ktx = { group = "androidx.navigation", name = "navigation-ui-ktx", version.ref = "navigationUiKtx" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }


 */