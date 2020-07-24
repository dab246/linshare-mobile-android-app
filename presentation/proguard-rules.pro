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

-printconfiguration ../build/proguard-merged-config.txt

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable
-keep class com.linagora.android.linshare.model.** { *; }

-keep enum com.linagora.android.linshare.view.Navigation$LoginFlow
-keep enum com.linagora.android.linshare.view.Navigation$UploadType
-keep enum com.linagora.android.linshare.view.Navigation$FolderType
-keep enum com.linagora.android.linshare.view.Navigation$MainNavigationType
-keep enum com.linagora.android.linshare.view.Event$DestinationPickerEvent

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
