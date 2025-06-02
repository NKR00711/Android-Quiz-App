# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keep class javax.management.** { *; }
-keep class java.lang.management.** { *; }
-keep class com.grinstitute.quiz.database.model.** { *; }

-allowaccessmodification
-optimizations !code/simplification/arithmetic
-keepattributes InnerClasses
-keepattributes EnclosingMethod
#-keep class com.google.firebase.auth.zza { *(...); }
-optimizations
-forceprocessing
-optimizationpasses 5
-overloadaggressively
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

#-keepattributes SourceFile,LineNumberTable
#-renamesourcefileattribute SourceFile
# If you want to enable optimization, you should include the
# following:
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
# Removing logging code
-assumenosideeffects class android.util.Log {
public static *** d(...);
public static *** v(...);
public static *** i(...);
public static *** w(...);
public static *** e(...);
}

-dontwarn java.lang.management.ManagementFactory
-dontwarn javax.management.MBeanServer
-dontwarn javax.management.ObjectInstance
-dontwarn javax.management.ObjectName
-dontwarn javax.management.StandardMBean

-packageobfuscationdictionary /Users/nkr/Documents/RandomDictMaker0o.txt
-obfuscationdictionary /Users/nkr/Documents/RandomDictMaker0o.txt
-classobfuscationdictionary /Users/nkr/Documents/RandomDictMaker0o.txt