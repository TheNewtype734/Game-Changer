# GameChanger ProGuard / R8 rules

# Keep line numbers for crash reports
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# ---- Moshi ----
# Keep classes with @JsonClass annotation (Moshi codegen generates adapters at compile time)
-keep @com.squareup.moshi.JsonClass class * { *; }
# Keep Moshi generated adapter classes
-keep class **JsonAdapter { *; }
# Keep Json fields in data classes used with Moshi
-keepclassmembers class * {
    @com.squareup.moshi.Json <fields>;
}

# ---- Retrofit ----
# Keep Retrofit service interfaces
-keep interface com.squires.gamechanger.network.api.** { *; }
# Retrofit uses reflection on method annotations
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepclassmembernames,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
# Retrofit uses OkHttp internally
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }

# ---- OkHttp / Okio ----
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

# ---- Hilt / Dagger ----
# Hilt generated components
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ActivityComponentManager { *; }
-dontwarn dagger.**

# ---- Kotlin ----
-keep class kotlin.** { *; }
-keep class kotlinx.coroutines.** { *; }
-dontwarn kotlin.**

# ---- Coil ----
-dontwarn coil.**

# ---- Room ----
# Room generates code at compile time; entities need to be kept for reflection
-keep class androidx.room.** { *; }
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao class * { *; }
-keep @androidx.room.Database class * { *; }

# ---- Compose ----
-dontwarn androidx.compose.**
