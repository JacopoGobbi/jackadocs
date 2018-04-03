package net.jackadull.jackadocs.docs.readme

import net.jackadull.jackadocs.structure.Chapter

import scala.xml.{NodeSeq, Text}

object ReadmeRoot extends Chapter {
  def id:String = "readme_begin"
  def title:NodeSeq = Text("Jackadocs")
  def contents:NodeSeq =
    <p>A little tool library for creating project documentation.</p>
}
