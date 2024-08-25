plugins {
    id("maven-publish")
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "hr.dorianpavetic.ui_transferable_text"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
}

configure<PublishingExtension> {
    publications.create<MavenPublication>("ui-transferable-text") {
        groupId = "com.github.dorianpavetic"
        artifactId = "ui-transferable-text"
        version = "v1.0.0"

    }
    repositories {
        mavenLocal()
    }
}