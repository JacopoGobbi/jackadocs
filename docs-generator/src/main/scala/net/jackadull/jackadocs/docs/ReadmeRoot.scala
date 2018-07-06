package net.jackadull.jackadocs.docs

import net.jackadull.jackadocs.docs.readme._
import net.jackadull.jackadocs.structure.badges.BadgeGenerators
import net.jackadull.jackadocs.structure.{Chapter, DocsMetaData}

import scala.xml.{NodeSeq, Text}

object ReadmeRoot extends Chapter with BadgeGenerators {
  def id = "readme_begin"
  def title = Text("Jackadocs")

  def contents:NodeSeq =
<p>{travisCIBadge} {mavenCentralBadge} {coverallsBadge} {codeFactorBadge} {snykBadge}</p>
<p>
  Tool library for automated generation of tool documentation.
  Can be used for creating <tt>README.md</tt> files, but also for documentation books, with multiple files, in either HTML or <a href="https://github.github.com/gfm/">Github-Flavored Markdown</a>.
</p>

  override def subChapters:Seq[Chapter] = Seq(Motivation, IntentedUse, UsageExample, ChapterStructure,
    RenderingProcess, FurtherRemarks, Roadmap)

  def docsMetaData:DocsMetaData = Main
}
