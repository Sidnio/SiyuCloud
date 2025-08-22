plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

val appVersionCode = 1
val appVersionName = "1.0"



android {
    signingConfigs {
        create("tv") {
            storeFile = file("android.keystore")
            keyAlias = "siyucloud"
            storePassword = "siyucloud123456"
            keyPassword = "siyucloud123456"
        }

        create("tablet") {
            storeFile = file("android.keystore")
            keyAlias = "siyucloud"
            storePassword = "siyucloud123456"
            keyPassword = "siyucloud123456"
        }
        create("phone") {
            storeFile = file("android.keystore")
            keyAlias = "siyucloud"
            storePassword = "siyucloud123456"
            keyPassword = "siyucloud123456"
        }
    }

    namespace = "com.sidnio.siyucloud"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.sidnio.siyucloud"
        minSdk = 24
        targetSdk = 36
        versionCode = appVersionCode
        versionName = appVersionName

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

    flavorDimensions += "device"


    productFlavors {
        create("tv") {
            dimension = "device"
            applicationIdSuffix =".standalone"
            applicationId = "com.sidnio.siyucloud.tv"

            signingConfig = signingConfigs.getByName("tv")
        }
        create("tablet") {
            dimension = "device"
            applicationId = "com.sidnio.siyucloud.tablet"
            signingConfig = signingConfigs.getByName("tablet")
        }
        create("phone") {
            dimension = "device"
            applicationId = "com.sidnio.siyucloud.phone"
            signingConfig = signingConfigs.getByName("phone")
        }
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    "tvImplementation"(project(":ui:tv"))
    "phoneImplementation"(project(":ui:phone"))
    "tabletImplementation"(project(":ui:tablet"))
}