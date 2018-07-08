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
  <li>Links to other chapters</li>
  <li>Migrate the whole Markdown to <a href="https://github.com/jackadull/gfm">Jackadull GFM</a></li>
</ul>
}
