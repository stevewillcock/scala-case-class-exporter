import java.io.File
import java.net.URLClassLoader
import java.util.jar.{JarEntry, JarFile}

import scala.collection.JavaConversions._
import scala.reflect.runtime.universe

object Main {

  case class Config(jars: Seq[File] = Nil, classes: Seq[String] = Nil, out: File = new File("."))

  val parser = new scopt.OptionParser[Config]("java -jar target/scala-2.11/scala-case-class-exporter-assembly-1.0.jar") {
    head("scala-case-class-exporter", "1.0")
    opt[Seq[File]]('j', "jars") valueName "<jar1>,<jar2>..." action { (a, c) => c.copy(jars = a) } text "jars to include"
    opt[Seq[String]]('c', "classes") valueName "<class1>,<class2>..." action { (a, c) => c.copy(classes = a) } text "classes to include"
  }

  // TODO - most of these names could probably be eliminated by looking at the declaring class on the methods
  val filteredMethodNames = List("hashCode", "copy", "productArity", "default", "1", "2", "3", "4", "5", "equals", "canEqual", "toString", "productPrefix", "productIterator", "productElement")

  def camelCase(s: String) = s match {
    case "" => ""
    case other => other.substring(0, 1).toUpperCase() + other.substring(1)
  }

  def generateClassString(jarEntry: JarEntry)(implicit classLoader: ClassLoader): String = {

    val fullClassName: String = jarEntry.getName.dropRight(6).replace('/', '.')
    val mirror = universe.runtimeMirror(classLoader)
    val members = mirror.staticClass(fullClassName).asType.typeSignature.members

    val caseClassValues = members.collect {
      case m if m.isTerm && m.asInstanceOf[universe.TermSymbol].isVal => m
    }

    def getSimpleName(fullName: String) = {
      val namePos = fullName.lastIndexOf('$').max(fullName.lastIndexOf('.'))
      fullName.substring(namePos + 1)
    }

    def typeNameForType(t: universe.Type) = {
      val typeName = getSimpleName(t.typeSymbol.fullName)

      typeName match {
        case "String" => "string"
        case "BigDecimal" => "decimal"
        case "Int" => "int"
        case other => other
      }
    }

    def buildTypeName(t: universe.Type): String = t.typeArgs match {
      case Nil => typeNameForType(t)
      case typeArgs => typeNameForType(t) + "<" + typeArgs.map(x => buildTypeName(x)).mkString(", ") + ">"
    }

    val caseClassValueTypeDescriptions = caseClassValues.map(x => (camelCase(getSimpleName(x.fullName)), buildTypeName(x.typeSignature.resultType)))

    val shortClassName = getSimpleName(fullClassName)

    val sb = new StringBuilder
    sb.append("public class " + shortClassName + "\n{\n")
    caseClassValueTypeDescriptions map { case (n, t) => s"    public $t $n {get; set;}\n" } foreach sb.append
    sb.append("}")

    sb.toString()
  }

  def runExport(config: Config): Unit = {
    val jarFiles = config.jars.map(new JarFile(_))
    implicit val classLoader = new URLClassLoader(config.jars.map(_.toURI.toURL).toArray)

    val classes = jarFiles.flatMap(_.entries().filter(entry => config.classes.exists(searchClassName => entry.getName.endsWith(s"$searchClassName.class"))))

    println(classes.map(generateClassString).mkString("\n \n"))
  }

  def main(args: Array[String]) {

    parser.parse(args, Config()) match {
      case Some(config) => runExport(config)
      case None =>
    }

  }

}
