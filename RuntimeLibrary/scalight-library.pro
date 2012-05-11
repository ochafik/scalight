# http://proguard.sourceforge.net/index.html#manual/examples.html

-dontwarn scala.**

-keepclassmembers class * {
    ** MODULE$;
}

-keep class scala.Product*
-keep class scala.Tuple*
-keep class scala.Function*
-keep class scala.PartialFunction*
-keep class scala.Option*
-keep class scala.Some*
-keep class scala.None*
-keep class scala.Either*
-keep class scala.Predef*
-keep class scala.runtime.BoxesRunTime

-optimizationpasses 3
-overloadaggressively
-repackageclasses ''
-allowaccessmodification
