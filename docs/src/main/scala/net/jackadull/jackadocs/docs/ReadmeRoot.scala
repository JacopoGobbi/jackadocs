package net.jackadull.jackadocs.docs

import net.jackadull.jackadocs.docs.readme._
import net.jackadull.jackadocs.rendering.ChapterNumbering
import net.jackadull.jackadocs.structure.badges.BadgeGenerators
import net.jackadull.jackadocs.structure.{Chapter, DocsMetaData, RootChapter}

import scala.xml.{NodeSeq, Text}

object ReadmeRoot extends RootChapter with BadgeGenerators {
  override def title:NodeSeq = Text("Jackadocs")

  override def chapterNumbering:ChapterNumbering = ChapterNumbering(ChapterNumbering.empty, ChapterNumbering.decimal)

  override def contentsBeforeTOC(root:RootChapter):NodeSeq =
<p>{travisCIBadge} {mavenCentralBadge} {scaladocBadge} {coverallsBadge} {codeFactorBadge} {snykBadge}</p>

  override def toc:Boolean = true

  override def contents(root:RootChapter):NodeSeq =
<p>
  Tool library for automated generation of tool documentation.
  Can be used for creating <tt>README.md</tt> files, but also for documentation books, with multiple files, in either HTML or <a href="https://github.github.com/gfm/">Github-Flavored Markdown</a>.
</p>

  override def subChapters:Seq[Chapter] = Seq(Ch1_Motivation, Ch2_IntentedUse, Ch3_UsageExample, Ch4_ChapterStructure,
    Ch5_RenderingProcess, Ch6_ProgrammingConsiderations, Ch7_JackadullRecipe)

  override def docsMetaData:DocsMetaData = Main
}
