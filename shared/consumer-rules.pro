# =============================================================================
# Consumer ProGuard Rules for Shared KMP Module
# These rules are automatically applied to consuming modules
# =============================================================================

# =============================================================================
# Shared Module - Core Data Models
# =============================================================================

# Keep all data models in shared module
-keep class com.yumedev.seijakulistkmp.**.model.** { *; }
-keep class com.yumedev.seijakulistkmp.**.domain.** { *; }
-keep class com.yumedev.seijakulistkmp.**.data.** { *; }

# Keep all DTOs and API models
-keep class com.yumedev.seijakulistkmp.**.dto.** { *; }
-keep class com.yumedev.seijakulistkmp.**.response.** { *; }
-keep class com.yumedev.seijakulistkmp.**.request.** { *; }

# =============================================================================
# Apollo GraphQL - Shared Module Generated Code
# =============================================================================

# Keep all GraphQL generated files
-keep class com.yumedev.seijakulistkmp.**.graphql.** { *; }

# Keep specific GraphQL types
-keep class com.yumedev.seijakulistkmp.**.type.** { *; }
-keep class com.yumedev.seijakulistkmp.**.fragment.** { *; }
-keep class com.yumedev.seijakulistkmp.**.query.** { *; }
-keep class com.yumedev.seijakulistkmp.**.mutation.** { *; }

# =============================================================================
# Room Database - Shared Module Entities
# =============================================================================

# Keep database entities
-keep class com.yumedev.seijakulistkmp.**.database.entity.** { *; }
-keep class com.yumedev.seijakulistkmp.**.entity.** { *; }

# Keep DAOs
-keep interface com.yumedev.seijakulistkmp.**.dao.** { *; }
-keep class com.yumedev.seijakulistkmp.**.dao.**$* { *; }

# =============================================================================
# Kotlin Serialization - Shared Module
# =============================================================================

# Keep all serializable classes and their serializers
-keep @kotlinx.serialization.Serializable class com.yumedev.seijakulistkmp.** {
    *;
}

-keep class com.yumedev.seijakulistkmp.**$$serializer {
    *;
}

# =============================================================================
# ViewModel & Presentation Layer
# =============================================================================

# Keep all ViewModels and ScreenModels
-keep class com.yumedev.seijakulistkmp.**.presentation.** { *; }
-keep class * extends cafe.adriel.voyager.core.model.ScreenModel {
    <init>(...);
}

# =============================================================================
# Repository & Use Cases
# =============================================================================

# Keep repositories
-keep class com.yumedev.seijakulistkmp.**.repository.** { *; }
-keep interface com.yumedev.seijakulistkmp.**.repository.** { *; }

# Keep use cases
-keep class com.yumedev.seijakulistkmp.**.usecase.** { *; }
-keep class com.yumedev.seijakulistkmp.**.use_case.** { *; }

# =============================================================================
# DI Module
# =============================================================================

# Keep Koin modules
-keep class com.yumedev.seijakulistkmp.di.** { *; }
-keep class com.yumedev.seijakulistkmp.*Module { *; }
-keep class com.yumedev.seijakulistkmp.*ModuleKt { *; }

# =============================================================================
# Utility Classes
# =============================================================================

# Keep extension functions
-keep class com.yumedev.seijakulistkmp.**.util.**Kt { *; }
-keep class com.yumedev.seijakulistkmp.**.extension.**Kt { *; }

# Keep constants
-keep class com.yumedev.seijakulistkmp.**.Constants { *; }
-keep class com.yumedev.seijakulistkmp.**.Constants$* { *; }
