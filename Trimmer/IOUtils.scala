object IOUtils {
  import java.io._

  def readBytes(in: InputStream) = {
    val o = new ByteArrayOutputStream
    val b = new Array[Byte](1024)
    var l = 0
    while ({ l = in.read(b) ; l > 0 })
      o.write(b, 0, l)
    o.toByteArray
  }
}
