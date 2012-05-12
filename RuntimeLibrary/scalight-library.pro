# http://proguard.sourceforge.net/index.html#manual/examples.html

-dontwarn scala.**

-keepclassmembers class * {
    ** MODULE$;
}

-keep class scala.Serializable { *; }
-keep class scala.Product* { *; }
-keep class scala.Tuple* { *; }
-keep class scala.Function* { *; }
-keep class scala.PartialFunction* { *; }
-keep class scala.runtime.AbstractFunction* { *; }
-keep class scala.Option* { *; }
-keep class scala.Some* { *; }
-keep class scala.None* { *; }
-keep class scala.Either* { *; }
-keep class scala.Predef* { 
	public <methods>; 
}
-keep class scala.runtime.BoxesRunTime* { 
	public <methods>; 
}
-keep class scala.runtime.ScalaRunTime* { 
	public <methods>; 
}
-keep class scala.runtime.VolatileObjectRef* { *; }

-optimizationpasses 3
-overloadaggressively
-repackageclasses 'scala.private'
-allowaccessmodification
