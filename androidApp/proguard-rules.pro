# =============================================================================
# ProGuard Rules for Seijaku List KMP
# Optimized for size and performance while maintaining functionality
# =============================================================================

# =============================================================================
# General Android & Kotlin Rules
# =============================================================================

# Keep source file names and line numbers for better crash reports
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Keep annotations for reflection
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod

# Keep generic signatures for Kotlin coroutines and collections
-keepattributes Signature

# Preserve parameter names for debugging
-keepparameternames

# Keep Kotlin metadata
-keep class kotlin.Metadata { *; }

# Keep Kotlin coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}

# =============================================================================
# Apollo GraphQL (Critical - Generated Models & Queries)
# =============================================================================

# Keep all Apollo generated models and their fields
-keep class com.yumedev.seijakulistkmp.**.graphql.** { *; }
-keep class com.apollographql.apollo.api.** { *; }
-keep class com.apollographql.apollo.exception.** { *; }

# Keep Apollo operation classes
-keep class * implements com.apollographql.apollo.api.Operation { *; }
-keep class * implements com.apollographql.apollo.api.Fragment { *; }
-keep class * implements com.apollographql.apollo.api.Mutation { *; }
-keep class * implements com.apollographql.apollo.api.Query { *; }
-keep class * implements com.apollographql.apollo.api.Subscription { *; }

# Keep Apollo adapters
-keep class **.*_ResponseAdapter { *; }
-keep class **.*_VariablesAdapter { *; }

# Apollo cache
-keep class com.apollographql.apollo.cache.** { *; }
-keep class com.apollographql.apollo.normalized.** { *; }

# Keep Apollo runtime
-keepclassmembers class com.apollographql.apollo.** {
    public <init>(...);
}

# =============================================================================
# Kotlin Serialization (Critical - JSON/XML Serialization)
# =============================================================================

# Keep serializer classes
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep @Serializable classes
-keep,includedescriptorclasses class com.yumedev.seijakulistkmp.**$$serializer { *; }
-keepclassmembers class com.yumedev.seijakulistkmp.** {
    *** Companion;
}
-keepclasseswithmembers class com.yumedev.seijakulistkmp.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep serializers for all @Serializable classes
-keep class **$$serializer {
    *;
}

# Keep JSON serialization
-keep class kotlinx.serialization.** { *; }
-keepclassmembers class * {
    *** Companion;
}

# =============================================================================
# Room 3 Database (Critical - Entities & DAOs)
# =============================================================================

# Keep Room database classes
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao class * { *; }
-keep @androidx.room.Database class * { *; }

# Keep Room generated classes
-keep class **_Impl { *; }
-keep class **.*_Impl { *; }

# Keep database entities with all fields
-keep class com.yumedev.seijakulistkmp.**.database.** { *; }
-keep class com.yumedev.seijakulistkmp.**.entity.** { *; }

# Room runtime
-dontwarn androidx.room.paging.**

# =============================================================================
# Firebase (Critical - Auth & Firestore Models)
# =============================================================================

# Firebase Authentication
-keep class com.google.firebase.auth.** { *; }
-keep class dev.gitlive.firebase.auth.** { *; }

# Firebase Firestore
-keep class com.google.firebase.firestore.** { *; }
-keep class dev.gitlive.firebase.firestore.** { *; }

# Keep Firestore model classes (data classes used with Firestore)
-keepclassmembers class com.yumedev.seijakulistkmp.**.model.** {
    *;
}

# Firebase common
-keep class com.google.firebase.** { *; }
-keep class dev.gitlive.firebase.** { *; }
-keepclassmembers class com.google.firebase.** { *; }

# =============================================================================
# Koin Dependency Injection (Required - DI Framework)
# =============================================================================

# Keep Koin modules and definitions
-keep class org.koin.** { *; }
-keep class org.koin.core.** { *; }
-keep class org.koin.android.** { *; }

# Keep classes used in Koin modules
-keep class com.yumedev.seijakulistkmp.di.** { *; }

# Keep ViewModel classes used with Koin
-keep class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}

# =============================================================================
# Ktor Client (Required - HTTP Networking)
# =============================================================================

# Ktor client
-keep class io.ktor.** { *; }
-keepclassmembers class io.ktor.** { volatile <fields>; }

# Ktor serialization
-keep class io.ktor.client.** { *; }
-keep class io.ktor.http.** { *; }
-keep class io.ktor.util.** { *; }

# OkHttp (used by Ktor)
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

# =============================================================================
# Voyager Navigation (Required - Screen Models)
# =============================================================================

# Keep Voyager screens
-keep class cafe.adriel.voyager.** { *; }

# Keep Screen implementations
-keep class * implements cafe.adriel.voyager.core.screen.Screen {
    <init>(...);
}

# Keep ScreenModel implementations
-keep class * extends cafe.adriel.voyager.core.model.ScreenModel {
    <init>(...);
}

# Keep screen classes in features
-keep class com.yumedev.seijakulistkmp.**.presentation.** { *; }

# =============================================================================
# Jetpack Compose (Generally handled by defaults, but extra safety)
# =============================================================================

# Keep Composable functions
-keep @androidx.compose.runtime.Composable class * { *; }
-keepclassmembers class * {
    @androidx.compose.runtime.Composable *;
}

# Compose runtime
-keep class androidx.compose.runtime.** { *; }
-keep class androidx.compose.ui.** { *; }

# Keep State objects
-keepclassmembers class * {
    androidx.compose.runtime.State *;
}

# =============================================================================
# Coil 3 Image Loading (Recommended)
# =============================================================================

# Coil
-keep class coil3.** { *; }
-keep interface coil3.** { *; }

# Keep image loaders
-keep class * implements coil3.ImageLoader { *; }

# =============================================================================
# Multiplatform Settings (Required)
# =============================================================================

-keep class com.russhwolf.settings.** { *; }

# =============================================================================
# Lottie Animations (Recommended)
# =============================================================================

-keep class com.airbnb.lottie.** { *; }
-dontwarn com.airbnb.lottie.**

# =============================================================================
# WorkManager (For Notifications)
# =============================================================================

-keep class * extends androidx.work.Worker
-keep class * extends androidx.work.ListenableWorker {
    public <init>(...);
}
-keep class androidx.work.** { *; }

# =============================================================================
# Google Credentials & Sign-In
# =============================================================================

-keep class androidx.credentials.** { *; }
-keep class com.google.android.gms.auth.** { *; }
-dontwarn com.google.android.gms.**

# =============================================================================
# Data Classes & Models (Keep all application models)
# =============================================================================

# Keep all data classes
-keep class com.yumedev.seijakulistkmp.**.model.** { *; }
-keep class com.yumedev.seijakulistkmp.**.domain.model.** { *; }
-keep class com.yumedev.seijakulistkmp.**.data.model.** { *; }

# Keep enums
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# =============================================================================
# Parcelable (Android)
# =============================================================================

-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class * implements android.os.Parcelable {
    public <fields>;
    private <fields>;
}

# =============================================================================
# Optimization Settings
# =============================================================================

# Optimization settings for R8
-optimizationpasses 5
-allowaccessmodification

# Remove logging in production (optional - uncomment if desired)
# -assumenosideeffects class android.util.Log {
#     public static *** d(...);
#     public static *** v(...);
#     public static *** i(...);
# }

# =============================================================================
# Suppress Warnings (Clean build output)
# =============================================================================

-dontwarn org.slf4j.**
-dontwarn org.jetbrains.annotations.**
-dontwarn javax.annotation.**
-dontwarn java.lang.instrument.ClassFileTransformer
