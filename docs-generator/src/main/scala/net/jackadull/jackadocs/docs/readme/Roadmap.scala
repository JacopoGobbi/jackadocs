package net.jackadull.jackadocs.docs.readme

import net.jackadull.jackadocs.structure.Chapter

import scala.xml.NodeSeq

object Roadmap extends Chapter {
  def id = "roadmap"
  def title = "Roadmap"

  def contents:NodeSeq =
<p>
  Here are some features that are considered or planned for future implementation:
</p>
<ul>
  <li>Table of contents</li>
  <li>Automatic generation of chapter IDs, compatible with Github Markdown header anchors</li>
  <li>Links to other chapters</li>
  <li><p><tt>ChapterNumbering</tt> with support for different kinds of numberings on different levels</p></li>
  <li>Moving the whole Markdown model into a separate project (and extending Markdown support there)</li>
</ul>
}
