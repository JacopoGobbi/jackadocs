package net.jackadull.jackadocs.docs.readme

import net.jackadull.jackadocs.structure.{Chapter, RootChapter}

import scala.xml.NodeSeq

object ChapterStructure extends Chapter {
  def title = "Chapter Structure"

  def contents(root:RootChapter):NodeSeq =
<p>
  The contents of every Jackadocs-based document are laid out in a hierarchy of chapters and sub-chapters.
  Chapters are Scala objects.
  There are two major ways how to declare chapters in the code:
</p>
<ol>
  <li>
    As top-level, standalone Scala <tt>object</tt> types that extend the <tt>Chapter</tt> trait.
    <a href="https://github.com/jackadull/jackadocs/blob/master/docs-generator/src/main/scala/net/jackadull/jackadocs/docs/readme/ChapterStructure.scala">The code for this very chapter</a> is an example of this.
  </li>
  <li>
    As inline constants, similar to case class instances, within the code.
    <a href="https://github.com/jackadull/jackadocs/blob/master/docs-generator/src/main/scala/net/jackadull/jackadocs/docs/readme/UsageExample.scala">The <tt>UsageExample</tt> object</a> contains several examples, which can be found when looking at its <tt>subChapters</tt> method.
  </li>
</ol>
<p>
  The root-level object for every Jackadocs-based documents is an instance of <tt>Chapter</tt>.
  The title of the root chapter is the title of the document.
  Its contents are the top-level text of the document.
  The sub-chapters of the root-level chapter are the chapters of the document.
</p>
<p>
  Of course, the structure is recursive.
  Sub-chapters can have sub-chapters of their own, and so on.
  In this way, a tree-like chapter structure gets assembled.
</p>
<p>
  Looking at <a href="https://github.com/jackadull/jackadocs/blob/master/docs-generator/src/main/scala/net/jackadull/jackadocs/docs/ReadmeRoot.scala">the source code of <tt>ReadmeRoot</tt></a> can be a helpful illustration of these abstract descriptions.
</p>
}
