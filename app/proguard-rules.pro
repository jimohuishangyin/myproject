# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\android_sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-ignorewarnings
-verbose

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

#百度推送
-keep class com.baidu.android.** { *; }
-keep class com.baidu.frontia.** { *; }
-keep class com.baidu.** { *; }
-keep class com.xmyunyou.bbbuy.baidu.** { *; }

-dontwarn com.nostra13.universalimageloader.**
-keep class com.nostra13.universalimageloader.** { *; }

-keepattributes **

-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep class com.google.**{*;}
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.widget.ListView
-keep interface android.support.v4.app.** { *; }

#GSON混淆
-keep class com.google.code.gson.** { *; }
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }

-keep public class com.xmyunyou.wcd.model
-keep public class com.xmyunyou.wcd.model.** { *; }
-keep public class com.xmyunyou.wcd.model.json.** { *; }
-keepclassmembers class com.xmyunyou.wcd.model {
	public *;
}
-keepclasseswithmembernames class com.xmyunyou.wcd.model {
	public *;
}
-keepclassmembers class com.xmyunyou.wcd.model.json {
	public *;
}
-keepclasseswithmembernames class com.xmyunyou.wcd.model.json {
	public *;
}

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.webkit.WebView

-keepattributes *Annotation*


-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keep class com.tencent.mm.sdk.openapi.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.openapi.** implements com.tencent.mm.sdk.openapi.WXMediaMessage$IMediaObject {*;}