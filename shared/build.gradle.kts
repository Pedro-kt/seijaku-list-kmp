import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room3)
    alias(libs.plugins.apollo)
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    jvm()

    android {
       namespace = "com.yumedev.seijakulistkmp.shared"
       compileSdk = libs.versions.android.compileSdk.get().toInt()
       minSdk = libs.versions.android.minSdk.get().toInt()

       compilerOptions {
           jvmTarget = JvmTarget.JVM_17
       }
       androidResources {
           enable = true
       }
       withHostTest {
           isIncludeAndroidResources = true
       }
       withDeviceTestBuilder {
           sourceSetTreeName = "test"
       }.configure {
           instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
       }
    }

    sourceSets {
        commonMain.dependencies {
            // Compose Multiplatform
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.compose.animation)

            // Lifecycle
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            // Kotlinx Libraries
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
            implementation(libs.xmlutil.serialization)

            // Room 3 KMP
            implementation(libs.room3.runtime)
            implementation(libs.sqlite.bundled)

            // Koin - Dependency Injection
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            // Apollo GraphQL
            implementation(libs.apollo.runtime)
            implementation(libs.apollo.normalized.cache)

            // Ktor Client
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.logging)

            // Coil 3
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor3)

            // Voyager
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.screenmodel)
            implementation(libs.voyager.transitions)
            implementation(libs.voyager.koin)

            // Tabler Icons
            implementation("dev.seyfarth:tabler-icons-kmp:1.0.0")

            // Multiplatform Settings
            implementation(libs.multiplatform.settings)
            implementation(libs.multiplatform.settings.coroutines)
            implementation(libs.multiplatform.settings.no.arg)

            implementation(libs.firebase.auth)
            implementation(libs.firebase.firestore)
            implementation(libs.firebase.common)

            // TODO: OAuth 2.0 - Descomentar cuando implementemos login AniList/MAL
            // Requiere: manifestPlaceholders["oidcRedirectScheme"] en androidMain
            // implementation(libs.oidc.core)
            // implementation(libs.oidc.appsupport)
        }

        // Android Main - Android specific implementations
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.compose.uiTooling)

            // AndroidX Activity Compose
            implementation(libs.androidx.activity.compose)

            // Kotlinx Coroutines Android
            implementation(libs.kotlinx.coroutines.android)

            // Koin Android
            implementation(libs.koin.android)

            // WorkManager for notifications
            implementation("androidx.work:work-runtime-ktx:2.9.0")

            // Ktor OkHttp Engine
            implementation(libs.ktor.client.okhttp)

            // Apollo SQLite Cache
            implementation(libs.apollo.normalized.cache.sqlite)

            // Firebase Android Native SDKs (required by GitLive Firebase)
            implementation("com.google.firebase:firebase-auth:23.1.0")
            implementation("com.google.firebase:firebase-firestore:25.1.1")
            implementation("com.google.firebase:firebase-common:21.0.0")

            // Google Credential Manager for Google Sign-In
            implementation("androidx.credentials:credentials:1.3.0")
            implementation("androidx.credentials:credentials-play-services-auth:1.3.0")
            implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")

            // Android-only Libraries
            implementation(libs.lottie.compose)
            implementation(libs.androidx.palette.ktx)
        }

        // iOS Main - iOS specific implementations
        iosMain.dependencies {
            // Ktor Darwin Engine
            implementation(libs.ktor.client.darwin)
        }

        // JVM Main - Desktop specific implementations
        jvmMain.dependencies {
            // Ktor Java Engine
            implementation(libs.ktor.client.java)

            // Coroutines Swing
            implementation(libs.kotlinx.coroutinesSwing)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

// Room 3
room3 {
    schemaDirectory("$projectDir/schemas")
}

// Apollo GraphQL Configuration
apollo {
    service("anilist") {
        packageName.set("com.yumedev.seijakulistkmp.data.remote.graphql")
        srcDir("src/commonMain/graphql")
        schemaFile.set(file("src/commonMain/graphql/schema.json"))
        generateKotlinModels.set(true)

        // Apollo cache configuration
        generateFragmentImplementations.set(true)
        generateQueryDocument.set(true)
    }
}

// KSP Configuration for Room 3
dependencies {
    // Room 3 Compiler - KSP for all platforms
    add("kspAndroid", libs.room3.compiler)
    add("kspIosArm64", libs.room3.compiler)
    add("kspIosSimulatorArm64", libs.room3.compiler)
    add("kspJvm", libs.room3.compiler)

    // Android Runtime Classpath
    androidRuntimeClasspath(libs.compose.uiTooling)
}

// Consumer ProGuard Rules for Android library consumers
afterEvaluate {
    extensions.findByType<com.android.build.gradle.LibraryExtension>()?.apply {
        defaultConfig {
            consumerProguardFiles("consumer-rules.pro")
        }
    }
}
