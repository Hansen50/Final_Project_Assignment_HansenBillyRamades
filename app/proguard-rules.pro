### Add project specific ProGuard rules here.
### You can control the set of applied configuration files using the
### proguardFiles setting in build.gradle.
###
### For more details, see
###   http://developer.android.com/guide/developing/tools/proguard.html
##
### If your project uses WebView with JS, uncomment the following
### and specify the fully qualified class name to the JavaScript interface
### class:
###-keepclassmembers class fqcn.of.javascript.interface.for.webview {
###   public *;
###}
##
### Uncomment this to preserve the line number information for
### debugging stack traces.
###-keepattributes SourceFile,LineNumberTable
##
### If you keep the line number information, uncomment this to
### hide the original source file name.
###-renamesourcefileattribute SourceFile
#
# Jaga agar semua class Google Sign-In tetap ada
-keep class com.google.android.gms.** { *; }
-keep class com.google.firebase.** { *; }
-keep class com.google.android.libraries.identity.** { *; }

# Jangan obfuscate class LoginActivity atau method di dalamnya
-keep class com.example.final_project_assignment_hansenbillyramades.presentation.ui.activity.LoginActivity { *; }

# Hindari penghapusan anotasi pada Parcelable atau Hilt
-keepattributes *Annotation*

# Jaga agar Parcelable tetap utuh
-keepclassmembers class ** implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

#######################
# Default Android Rules
#######################

# Android Classes
-keep public class * extends android.app.Application { *; }
-keep public class * extends android.app.Activity { *; }
-keep public class * extends android.app.Service { *; }
-keep public class * extends android.app.Fragment { *; }
-keep public class * extends androidx.fragment.app.Fragment { *; }
-keep public class * extends android.content.BroadcastReceiver { *; }
-keep public class * extends android.content.ContentProvider { *; }

# Android Views
-keepclassmembers class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(***);
}

# Keep Parcelable Classes
-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}

# Keep methods called by reflection in native code
-keepclassmembers class * {
    native <methods>;
}

########################
# Retrofit + Gson Rules
########################

# Retrofit and Gson
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn retrofit2.Platform$Java8
-keep class retrofit2.** { *; }
-keep class com.google.gson.** { *; }
-keep class * implements retrofit2.Call
-keepclassmembers class * {
    @retrofit2.http.* <methods>;
}

# Keep fields annotated with @SerializedName
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

########################
# Hilt/Dagger Rules
########################

# Hilt (Dagger) generated code
-keep class dagger.hilt.** { *; }
-dontwarn dagger.hilt.internal.**
-keep class **_Factory { *; }
-keep class **_MembersInjector { *; }

########################
# Data Binding Rules
########################

# Keep DataBinding classes
-keep class **.databinding.*Binding { *; }
-keep class **.BR { *; }

########################
# Moshi (Jika Digunakan)
########################


########################
# Coroutines Rules
########################

# Prevent obfuscation of Kotlin coroutine classes
-dontwarn kotlinx.coroutines.**
-keep class kotlinx.coroutines.** { *; }

########################
# Prevent Reflection Issues
########################

# Keep all annotations
-keepattributes *Annotation*

# Prevent issues with reflection in your app
-keepclassmembers class * {
    *;
}

########################
# General Optimization
########################

# Optimization settings (optional)
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

