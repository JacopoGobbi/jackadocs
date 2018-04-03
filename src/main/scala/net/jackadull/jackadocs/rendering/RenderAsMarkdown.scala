package net.jackadull.jackadocs.rendering

import net.jackadull.jackadocs.structure.Chapter

import scala.language.postfixOps
import scala.xml._

object RenderAsMarkdown {
  def apply(chapter:Chapter, chapterNumbering:ChapterNumbering):String = {
    def recurse(toRender:Seq[Chapter], cn:ChapterNumbering, depth:Int, soFar:Vector[MarkdownCommand]):(Vector[MarkdownCommand],ChapterNumbering) =
      toRender match {
        case Seq() ⇒ (soFar, cn)
        case Seq(chapter1, moreChapters@_*) ⇒
          val (chapterNumber, cn2) = cn count chapter1
          ???
      }
    ???
  }

  private def toCommands(ns:NodeSeq):Vector[MarkdownCommand] = ns match {
    case doc:Document ⇒ doc.children.toVector flatMap toCommands
    case e:Elem if e.label == "p" ⇒ Vector(ParagraphCommand(toCommands(e child)))
      // TODO more HTML tags
    case e:Elem ⇒ toCommands(e child)
    case Group(child) ⇒ toCommands(child)
    case p:PCData ⇒ Vector(TextCommand(p data))
    case t:Text ⇒ Vector(TextCommand(t data))
    case a:Atom[_] ⇒ Vector(TextCommand(s"${a data}"))
    case _ ⇒ Vector(HTMLCommand(ns))
  }

  private sealed trait MarkdownCommand
  private final case class HTMLCommand(contents:NodeSeq) extends MarkdownCommand
  private final case class ParagraphCommand(contents:Vector[MarkdownCommand]) extends MarkdownCommand
  private final case class TextCommand(contents:String) extends MarkdownCommand
}
