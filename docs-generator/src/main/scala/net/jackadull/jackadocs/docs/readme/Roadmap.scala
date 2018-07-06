package net.jackadull.jackadocs.docs.readme

import net.jackadull.jackadocs.structure.{Chapter, RootChapter}

import scala.xml.NodeSeq

object Roadmap extends Chapter {
  def title = "Roadmap"

  def contents(root:RootChapter):NodeSeq =
<p>
  Here are some features that are considered or planned for future implementation:
</p>
<ul>
  <li>Table of contents</li>
  <li>Links to other chapters</li>
  <li><p><tt>ChapterNumbering</tt> with support for different kinds of numberings on different levels</p></li>
  <li>Migrate the whole Markdown to <a href="https://github.com/jackadull/gfm">Jackadull GFM</a></li>
</ul>
}
