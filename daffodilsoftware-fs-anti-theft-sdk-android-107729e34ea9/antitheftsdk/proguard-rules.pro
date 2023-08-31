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
-keepattributes *Annotation*

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepattributes InnerClasses
-keep class **.R
-keep class **.R$* {
    <fields>;
}

# These options let obfuscated applications or libraries produce stack traces that can still be deciphered later on
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable

# Enable proguard with Cordova
-keep class org.apache.cordova.** { *; }
-keep public class * extends org.apache.cordova.CordovaPlugin

-keep class com.worklight.androidgap.push.** { *; }
-keep class com.worklight.wlclient.push.** { *; }
-keep class com.worklight.common.security.AppAuthenticityToken { *; }

# Enable proguard with Google libs
-keep class com.google.** { *;}
-dontwarn com.google.common.**
-dontwarn com.google.ads.**

# apache.http
-keep class org.apache.http.** { *; }
-dontwarn org.apache.http.**
-optimizations !class/merging/vertical*,!class/merging/horizontal*,!code/simplification/arithmetic,!field/*,!code/allocation/variable

-keep class net.sqlcipher.** { *; }
-dontwarn net.sqlcipher.**

-keep class org.codehaus.** { *; }
-keepattributes *Annotation*,EnclosingMethod

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keepclassmembers class * implements javax.net.ssl.SSLSocketFactory {
   private  javax.net.ssl.SSLSocketFactory delegate;
}


# These classes contain references to external jars which are not included in the default MobileFirst project.
-dontwarn com.worklight.common.internal.WLTrusteerInternal*
-dontwarn com.worklight.jsonstore.**
-dontwarn org.codehaus.jackson.map.ext.*
-dontwarn com.worklight.androidgap.push.GCMIntentService
-dontwarn com.worklight.androidgap.plugin.WLInitializationPlugin
-dontwarn com.worklight.wlclient.push.GCMIntentService
-dontwarn org.bouncycastle.**
-dontwarn com.worklight.androidgap.jsonstore.security.SecurityManager

-dontwarn com.worklight.wlclient.push.WLBroadcastReceiver
-dontwarn com.worklight.wlclient.push.common.*
-dontwarn com.worklight.wlclient.api.WLPush
-dontwarn com.worklight.wlclient.api.SecurityUtils

-dontwarn android.support.v4.**
-dontwarn android.net.SSLCertificateSocketFactory
-dontwarn android.net.http.*
-dontwarn okio.**
-dontwarn com.squareup.okhttp.**

-keep class com.google.android.gms.tasks** {*;}
