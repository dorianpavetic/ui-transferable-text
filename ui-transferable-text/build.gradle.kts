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
        version = "1.0.5"
        pom {
            name.set("ui-transferable-text")
            description.set(
                "Library which enables passing localized text between Android context-aware and contextless components - e.g. between ViewModel and Fragment",
            )
            url.set("https://github.com/dorianpavetic/ui-transferable-text")
            developers {
                developer {
                    id.set("dorianpavetic")
                    name.set("Dorian-Filip Pavetic")
                    email.set("dorian.pavetic1508@gmail.com")
                }
            }
            scm {
                connection.set("https://github.com/dorianpavetic/ui-transferable-text.git")
                developerConnection.set("https://github.com/dorianpavetic/ui-transferable-text.git")
                url.set("https://github.com/dorianpavetic/ui-transferable-text")
            }
        }
        afterEvaluate {
            from(components["release"])
        }
    }
    repositories {
        mavenLocal()
    }
}
