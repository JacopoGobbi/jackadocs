package net.jackadull.jackadocs.structure

import scala.xml.NodeSeq

trait Chapter {
  def id:String
  def title:NodeSeq
  def contents:NodeSeq

  def subChapters:Seq[Chapter] = Seq()
}
