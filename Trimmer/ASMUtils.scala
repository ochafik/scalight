import org.objectweb.asm._ 
  
/**
 * MUST COMPILE SCALA WITH -no-specialization first !
 * http://asm.ow2.org/asm40/javadoc/user/
 */
object ASMUtils {
  
  def removeAnnotations(b: Array[Byte]) = //if (true) b else 
  {
    val w = new ClassWriter(0)
    val f = new ClassVisitor(0, w) {
      override def visitMethod(access: Int, name: String, desc: String, signature: String, exceptions: Array[String]) = {
        new MethodVisitor(Opcodes.ASM4, super.visitMethod(access, name, desc, signature, exceptions)) {
          override def visitAnnotation(desc: String, visible: Boolean) = 
            null
          override def visitAnnotationDefault() =
            null
          override def visitParameterAnnotation(parameter: Int, desc: String, visible: Boolean) = 
            null
          override def visitAttribute(a: Attribute) = {}
        }
      }
      override def visitAnnotation(desc: String, visible: Boolean) =
        null
        
      override def visitAttribute(a: Attribute) = {}
    }
    new ClassReader(b).accept(f, 0)
    w.toByteArray
  }
  
  def getReferencedClasses(b: Array[Byte]) = {
    val s = collection.mutable.HashSet[String]()
    
    val r = new ClassReader(b)
    val n = r.getClassName
    s += r.getSuperName
    
    r.accept(
      new ClassVisitor(0) {
        def add(t: Type) = {
          if (t.getSort == Type.OBJECT)
            s += t.getInternalName
        }
        override def visitOuterClass(owner: String, name: String, desc: String) = {
          s += owner
          super.visitOuterClass(owner, name, desc)
        }
        override def visitMethod(access: Int, name: String, desc: String, signature: String, exceptions: Array[String]) = {
          val t = Type.getMethodType(desc)
          t.getArgumentTypes().map(add(_))
          add(t.getReturnType())
          
          new MethodVisitor(Opcodes.ASM4, super.visitMethod(access, name, desc, signature, exceptions)) {
            override def visitFieldInsn(opcode: Int, owner: String, name: String, desc: String) = {
              s += owner
              super.visitFieldInsn(opcode, owner, name, desc)
            }
            override def visitMethodInsn(opcode: Int, owner: String, name: String, desc: String) = {
              s += owner
              super.visitMethodInsn(opcode, owner, name, desc)
            }
            override def visitTypeInsn(opcode: Int, `type`: String) = {
              s += `type`
              super.visitTypeInsn(opcode, `type`)
            }
            override def visitMultiANewArrayInsn(desc: String, dims: Int) = {
              add(Type.getType(desc).getElementType)
              super.visitMultiANewArrayInsn(desc, dims)
            }
          }
        }
        override def visitField(access: Int, name: String, desc: String, signature: String, value: AnyRef) = {
          add(Type.getType(desc))
          super.visitField(access, name, desc, signature, value)
        }
      }, 
      0
    )
    (n, s.toSet)
  }
}
