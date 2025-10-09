plugins {
    id("com.android.application")
}

android {
    namespace = "io.github.vvb2060.ims"
    defaultConfig {
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            vcsInfo.include = false
            proguardFiles("proguard-rules.pro")
            signingConfig = signingConfigs["debug"]
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    packaging {
        resources {
            excludes += "**"
        }
    }
    lint {
        checkReleaseBuilds = false
    }
    dependenciesInfo {
        includeInApk = false
    }
}

dependencies {
    compileOnly(projects.stub)
    implementation(libs.shizuku.provider)
    implementation(libs.shizuku.api)
    implementation(libs.hiddenapibypass)
}
