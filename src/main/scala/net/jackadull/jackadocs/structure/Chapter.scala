package net.jackadull.jackadocs.structure

import scala.language.{implicitConversions, postfixOps}
import scala.xml.{NodeSeq, Text}

trait Chapter {
  def title:NodeSeq
  def contents(root:RootChapter):NodeSeq

  def contentsBeforeTOC(root:RootChapter):NodeSeq = NodeSeq.Empty
  def id(root:RootChapter):String = (root idOfChapter) getOrElse (this, title.text.toLowerCase.replaceAll("""[^\w\- ]""", "").replace(' ', '-'))
  def subChapters:Seq[Chapter] = Seq()
  def titleWithNumber(root:RootChapter):NodeSeq = {
    val num = (root numberOfChapter) getOrElse (this, "")
    if(num isEmpty) title else title match {
      case Text(txt) ⇒ Text(s"$num $txt")
      case _ ⇒ Text(s"$num ") ++ title
    }
  }
  def toc:Boolean = false

  protected implicit def _stringToNodeSeq(str:String):NodeSeq = Text(str)
}
object Chapter {
  def apply(title:NodeSeq, contents:NodeSeq, subChapters:Seq[Chapter]=Seq()):Chapter =
    Impl(title, contents, subChapters)

  private final case class Impl(title:NodeSeq, _contents:NodeSeq, override val subChapters:Seq[Chapter]) extends Chapter {
    def contents(root:RootChapter) = _contents
  }
}
