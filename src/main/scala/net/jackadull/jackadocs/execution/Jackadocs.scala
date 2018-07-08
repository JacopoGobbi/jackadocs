package net.jackadull.jackadocs.execution

import java.nio.file.{FileSystems, Files, Path}

import net.jackadull.jackadocs.execution.Jackadocs.{Generate, GenerateImpl}
import net.jackadull.jackadocs.rendering.RenderAsMarkdown
import net.jackadull.jackadocs.rendering.markdown.MDWrite
import net.jackadull.jackadocs.structure.RootChapter

import scala.language.postfixOps
import scala.xml.XML

object Jackadocs {
  def apply(baseDirectory:String):Jackadocs = apply(path(baseDirectory))
  def apply(baseDirectory:Path):Jackadocs =
    if(baseDirectory isAbsolute) new Jackadocs(baseDirectory)
    else apply(baseDirectory toAbsolutePath)

  def fromArgs(args:Seq[String]):Jackadocs = args.length match {
    case 1 ⇒
      val baseDirectory = path(args head)
      if(!(Files exists baseDirectory)) sys error s"given base directory not found: $baseDirectory"
      else if(!(Files isDirectory baseDirectory)) sys error s"given base directory is not a directory: $baseDirectory"
      else apply(baseDirectory)
    case n ⇒ sys error s"expecting exactly one argument, containing the generator project base directory, but got: $n"
  }

  private def path(p:String):Path = (FileSystems getDefault) getPath p

  trait Generate {
    def markdownFor(chapter:RootChapter):Jackadocs
  }
  private final class GenerateImpl(target:Path, jackadocs:Jackadocs) extends Generate {
    def markdownFor(chapter:RootChapter):Jackadocs =
      output(MDWrite(RenderAsMarkdown(chapter), _))

    private def output(f:Appendable⇒Unit):Jackadocs = {
      val out = Files newBufferedWriter target
      try {f(out)} finally {out close()}
      jackadocs
    }
  }
}
final class Jackadocs private(baseDirectory:Path) {
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
