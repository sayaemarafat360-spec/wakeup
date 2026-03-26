# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.

# Keep Application class for Hilt
-keep public class * extends android.app.Application

# Keep Hilt generated classes
-keep class dagger.hilt.android.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }

# Keep Room entities and DAOs
-keep class com.wakeupwarrior.data.local.entity.** { *; }
-keep class com.wakeupwarrior.data.local.dao.** { *; }

# Keep data models
-keep class com.wakeupwarrior.data.model.** { *; }
-keep class com.wakeupwarrior.domain.model.** { *; }

# Keep Enum classes
-keepclassmembers enum com.wakeupwarrior.** {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep serializable classes
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Keep Lottie animations
-keep class com.airbnb.lottie.** { *; }

# Keep CameraX
-keep class androidx.camera.** { *; }

# Keep ML Kit
-keep class com.google.mlkit.** { *; }

# Keep AdMob
-keep public class com.google.android.gms.ads.** { *; }
-keep public class com.google.ads.** { *; }

# Keep Billing
-keep class com.android.vending.billing.** { *; }

# Remove logging in release
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
}
