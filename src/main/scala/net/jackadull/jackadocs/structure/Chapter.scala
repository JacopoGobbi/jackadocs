package net.jackadull.jackadocs.structure

import scala.language.{implicitConversions, postfixOps}
import scala.xml.{NodeSeq, Text}

trait Chapter {
  def title:NodeSeq
  def contents(root:RootChapter):NodeSeq

  def id(root:RootChapter):String = (root idOfChapter) getOrElse (this, titleAsIDBase)
  def subChapters:Seq[Chapter] = Seq()
  def titleAsIDBase:String = title.text.toLowerCase.replaceAll("""[^\w\- ]""", "").replace(' ', '-')

  protected implicit def _stringToNodeSeq(str:String):NodeSeq = Text(str)
}
object Chapter {
  def apply(title:NodeSeq, contents:NodeSeq, subChapters:Seq[Chapter]=Seq()):Chapter =
    Impl(title, contents, subChapters)

  private final case class Impl(title:NodeSeq, _contents:NodeSeq, override val subChapters:Seq[Chapter]) extends Chapter {
    def contents(root:RootChapter) = _contents
  }
}
