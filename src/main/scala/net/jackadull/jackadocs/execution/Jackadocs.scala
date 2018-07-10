package net.jackadull.jackadocs.execution

import java.io.IOException
import java.nio.file.{FileSystems, Files, Path}

import net.jackadull.jackadocs.execution.Jackadocs.{Generate, GenerateImpl}
import net.jackadull.jackadocs.rendering.RenderAsMarkdown
import net.jackadull.jackadocs.rendering.markdown.MDWrite
import net.jackadull.jackadocs.structure.RootChapter

import scala.annotation.tailrec
import scala.language.postfixOps
import scala.xml.XML

object Jackadocs {
  def apply(baseDirectory:String, validationOnly:Boolean):Jackadocs = apply(path(baseDirectory), validationOnly)
  def apply(baseDirectory:Path, validationOnly:Boolean):Jackadocs =
    if((baseDirectory normalize()) != baseDirectory) apply(baseDirectory normalize(), validationOnly)
    else if(!(baseDirectory isAbsolute)) apply(baseDirectory toAbsolutePath, validationOnly)
    else new Jackadocs(baseDirectory, validationOnly)

  def fromArgs(args:Seq[String], validationOnly:Boolean=false):Jackadocs =
    if(args contains "-V") fromArgs(args filterNot {_ == "-V"}, validationOnly=true)
    else args.length match {
      case 1 ⇒
        val baseDirectory = path(args head)
        if(!(Files exists baseDirectory)) sys error s"given base directory not found: $baseDirectory"
        else if(!(Files isDirectory baseDirectory)) sys error s"given base directory is not a directory: $baseDirectory"
        else apply(baseDirectory, validationOnly)
      case n ⇒ sys error s"expecting exactly one argument (except for an optional -V), containing the generator project base directory, but got: $n"
    }

  private def path(p:String):Path = ((FileSystems getDefault) getPath p).toAbsolutePath.normalize()

  trait Generate {
    def markdownFor(chapter:RootChapter):Jackadocs
  }
  private final class GenerateImpl(target:Path, jackadocs:Jackadocs) extends Generate {
    def markdownFor(chapter:RootChapter):Jackadocs = {
      val f_write:Appendable⇒Unit = MDWrite(RenderAsMarkdown(chapter), _)
      if(jackadocs validationOnly) validateOutput(f_write) else output(f_write)
    }

    private def output(f:Appendable⇒Unit):Jackadocs = {
      val out = Files newBufferedWriter target
      try {f(out)} finally {out close()}
      jackadocs
    }

    private def validateOutput(f:Appendable⇒Unit):Jackadocs = {
      val reader =
        try {Files newBufferedReader target}
        catch {case iox:IOException ⇒ throw validationError_expectedFileNotFound(iox)}
      try {f(new Appendable {
        final def append(csq:CharSequence):Appendable = append(csq, 0, csq length)
        @tailrec final def append(csq:CharSequence, start:Int, end:Int):Appendable =
          if(start<end) {append(csq charAt start); append(csq, start+1, end)} else this
        final def append(c:Char):Appendable = reader read() match {
          case -1 ⇒ throw validationError_unexpectedEndOfFile()
          case expected ⇒ if((expected toChar) != c) throw validationError_differentFileContents() else this
        }
      })} finally {reader close()}
      jackadocs
    }

    private def validationError_differentFileContents():Throwable = new DocsOutOfSync(s"different file contents for: $target")
    private def validationError_expectedFileNotFound(cause:IOException):Throwable = new DocsOutOfSync(s"expected file not found: $target", cause)
    private def validationError_unexpectedEndOfFile():Throwable = validationError_differentFileContents()
  }

  private class DocsOutOfSync(message:String, cause:Throwable=null) extends RuntimeException(message, cause) {
    System.err println "***"
    System.err println "*** docs-generator validation error"
    System.err println "***"
    System.err println s"***   docs-generator is out of sync ($message)."
    System.err println "***   Consider running 'mvn install' for re-generating."
    System.err println "***"
  }
}
final class Jackadocs private(baseDirectory:Path, val validationOnly:Boolean) {
  import Jackadocs.path

  def generateAt(pathStr:String):Generate = generateAt(path(pathStr))
  def generateAt(path:Path):Generate = new GenerateImpl(relPath(path), this)

  def pomVersion(pathStr:String):String = pomVersion(path(pathStr))
  def pomVersion(path:Path):String = {
    val reader = Files newBufferedReader relPath(path)
    try {(XML.load(reader) \ "version") text}
    finally {reader close()}
  }

  def requirePOMVersion(pathStr:String)(requiredVersion:String):Jackadocs =
    requirePOMVersion(path(pathStr))(requiredVersion)
  def requirePOMVersion(path:Path)(requiredVersion:String):Jackadocs = {
    val actualVersion = pomVersion(path).trim
    if(requiredVersion.trim != actualVersion)
      sys error s"required version $requiredVersion in '$path', but found: $actualVersion (maybe you bumped the main project version, but forgot to update the POM of the docs-generator dependency)"
    this
  }

  private def relPath(rel:Path):Path = if(rel isAbsolute) rel else baseDirectory resolve rel
}
