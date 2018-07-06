package net.jackadull.jackadocs.structure

import scala.xml.NodeSeq

trait Chapter {
  def id:String
  def title:NodeSeq
  def contents:NodeSeq

  def subChapters:Seq[Chapter] = Seq()
}
object Chapter {
  def apply(id:String, title:NodeSeq, contents:NodeSeq, subChapters:Seq[Chapter]=Seq()):Chapter =
    Impl(id, title, contents, subChapters)

  private final case class Impl(id:String, title:NodeSeq, contents:NodeSeq, override val subChapters:Seq[Chapter]) extends Chapter
}
