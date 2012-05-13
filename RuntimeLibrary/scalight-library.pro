# http://proguard.sourceforge.net/index.html#manual/examples.html

-dontwarn scala.**

-keepclassmembers class * {
	*** $init$(...);
	static <init>();
	<init>(...);
	
	*** unapply(***);
	*** apply(***);
	
    ** MODULE$;
}
-keep class scala.MatchError
-keep class scala.util.control.Breaks$
-keep class scala.util.control.Breaks {
	*** breakable(...);
	*** tryBreakable(...);
	*** break();
}
-keep class scala.util.control.Exception$ {
	public <methods>;
}
-keep class scala.util.control.ControlThrowable
-keep class scala.Unit$ {
	*** box(...);
	*** unbox(...);
}
-keep class scala.runtime.BoxedUnit {
	*** UNIT;
	*** TYPE;
}
-keep class scala.runtime.VolatileObjectRef {
	*** elem(...);
}
-keep class scala.Serializable

-keep class scala.Product {
	*** productElement(...);
	*** productArity(...);
	*** productIterator(...);
	*** productPrefix(...);
}
-keep class scala.Product* {
	*** _1(...);
	*** _2(...);
	*** _3(...);
	*** _4(...);
	*** _5(...);
	*** _6(...);
	*** _7(...);
	*** _8(...);
	*** _9(...);
	*** _10(...);
	*** _11(...);
	*** _12(...);
	*** _13(...);
	*** _14(...);
	*** _15(...);
	*** _16(...);
	*** _17(...);
	*** _18(...);
	*** _19(...);
	*** _20(...);
	*** _21(...);
	*** _22(...);
}
-keep class scala.Product2 {
	*** swap(...);
}

-keep class scala.Tuple*
-keep class scala.Function*
-keep class scala.Function$ {
	*** const(...);
	*** curried(...);
	*** uncurried(...);
	*** tupled(...);
	*** untupled(...);
}
-keep class scala.PartialFunction$ {
	*** apply(...);
	*** empty(...);
	*** cond(...);
	*** condOpt(...);
}
-keep class scala.PartialFunction { 
	*** isDefinedAt(...);
	*** apply(...);
	*** orElse(...);
	*** andThen(...);
	*** lift(...);
	*** applyOrElse(...);
	*** run(...);
	*** runWith(...);
}
-keep class scala.runtime.AbstractFunction*
-keep class scala.Option$ { 
	*** apply(...);
	*** empty(...);
}
-keep class scala.Option {
	*** isEmpty(...);
	*** isDefined(...);
	*** get(...);
	*** getOrElse(...);
	*** orNull(...);
	*** map(...);
	*** fold(...);
	*** flatMap(...);
	*** filter(...);
	*** filterNot(...);
	*** nonEmpty(...);
	*** exists(...);
	*** forall(...);
	*** foreach(...);
	*** collect(...);
	*** orElse(...);
	*** toRight(...);
	*** toLeft(...);
}
-keep class scala.Some$
-keep class scala.Some
-keep class scala.None$
-keep class scala.None
-keep class scala.Either$
-keep class scala.Either { 
	*** left(...);
	*** right(...);
	*** fold(...);
	*** swap(...);
	*** joinRight(...);
	*** joinLeft(...);
	*** isLeft(...);
	*** isRight(...);
}
-keep class scala.Left$
-keep class scala.Left
-keep class scala.Right$
-keep class scala.Right
-keep class scala.Predef$ { 
	*** print(...);
	*** println(...);
	#*** printf(...);
	#*** readLine(...);
	#*** readBoolean(...);
	#*** readByte(...);
	#*** readShort(...);
	#*** readChar(...);
	#*** readInt(...);
	#*** readLong(...);
	#*** readFloat(...);
	#*** readDouble(...);
	#*** readf(...);
	#*** readf1(...);
	#*** readf2(...);
	#*** readf3(...);
}
-keep class scala.runtime.BoxesRunTime { 
	public <methods>;
}
-keep class scala.runtime.ScalaRunTime$ { 
	*** Try(...);
	*** _equals(...);
	*** _hashCode(...);
	*** _toString(...);
	*** anyValClass(...);
	*** arrayClass(...);
	*** arrayElementClass(...);
	*** array_apply(...);
	*** array_clone(...);
	*** array_length(...);
	*** array_update(...);
	*** checkInitialized(...);
	*** ensureAccessible(...);
	*** isAnyVal(...);
	*** isArray(...);
	*** isArray(...);
	*** isTuple(...);
	*** isValueClass(...);
	*** map(...);
	*** mkString(...);
	*** replStringOf(...);
	*** sameElements(java.util.Iterator, java.util.Iterator);
	*** stringOf(Object);
	#** stringOf(Object, int); // references too many types !
	*** toObjectArray(...);
	*** typedProductIterator(...);
}

#-optimizationpasses 3
#-overloadaggressively
#-repackageclasses 'scala.private'
-allowaccessmodification

-dontobfuscate
-keeppackagenames **
