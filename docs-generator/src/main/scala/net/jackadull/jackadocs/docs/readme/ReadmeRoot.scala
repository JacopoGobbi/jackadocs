package net.jackadull.jackadocs.docs.readme

import net.jackadull.jackadocs.structure.Chapter

import scala.xml.NodeSeq

object ReadmeRoot extends Chapter {
  def id = "readme_begin"
  def title = "Jackadocs"

  def contents:NodeSeq =
<p>
  Tool library for automated generation of tool documentation.
  Can be used for creating <tt>README.md</tt> files, but also for documentation books, with multiple files, in either HTML or Github-Flavored Markdown.
</p>

  override def subChapters:Seq[Chapter] = Seq(Motivation, IntentedUse, UsageExample, ChapterStructure,
    RenderingProcess, FurtherRemarks, Roadmap)
}
