package net.jackadull.jackadocs.docs.readme

import net.jackadull.jackadocs.structure.{Chapter, RootChapter}

import scala.xml.NodeSeq

object Ch4_ChapterStructure extends Chapter {
  override def title:NodeSeq = "Chapter Structure"

  override def contents(root:RootChapter):NodeSeq =
<p>
  The contents of every Jackadocs-based document are laid out in a hierarchy of chapters and sub-chapters.
  Chapters are Scala objects.
  There are two major ways how to declare chapters in the code:
</p>
<ol>
  <li>
    As top-level, standalone Scala <tt>object</tt> types that extend the <tt>Chapter</tt> trait.
    <a href="https://github.com/jackadull/jackadocs/blob/release/latest/docs/src/main/scala/net/jackadull/jackadocs/docs/readme/Ch4_ChapterStructure.scala">The code for this very chapter</a> is an example of this.
  </li>
  <li>
    As inline constants, similar to case class instances, within the code.
    <a href="https://github.com/jackadull/jackadocs/blob/release/latest/docs/src/main/scala/net/jackadull/jackadocs/docs/readme/Ch3_UsageExample.scala">The <tt>UsageExample</tt> object</a> contains several examples, which can be found when looking at its <tt>subChapters</tt> method.
  </li>
</ol>
<p>
  The root-level object for every Jackadocs-based documents is an instance of <a href="https://github.com/jackadull/jackadocs/blob/release/latest/src/main/scala/net/jackadull/jackadocs/structure/RootChapter.scala"><tt>RootChapter</tt></a>.
  The title of the root chapter is the title of the document.
  Its contents are the top-level text of the document.
  The sub-chapters of the root-level chapter are the chapters of the document.
</p>
<p>
  Of course, the structure is recursive.
  Chapters below <tt>RootChapter</tt> are instances of <tt>Chapter</tt>, which can have sub-chapters of their own, and so on.
  In this way, a tree-like chapter structure gets assembled.
</p>
<p>
  Looking at <a href="https://github.com/jackadull/jackadocs/blob/release/latest/docs/src/main/scala/net/jackadull/jackadocs/docs/ReadmeRoot.scala">the source code of <tt>ReadmeRoot</tt></a> can be a helpful illustration of these abstract descriptions.
</p>

  override def subChapters:Seq[Chapter] = Seq(
    Chapter("TOC Options",
<p>Here are some optional features of <tt>Chapter</tt> instances:</p>
<ul>
  <li>
    <p>
      <b>Table of Contents:</b>
      For adding one, simply define:
    </p>
<pre><code class="language-scala">
override def toc = true
</code></pre>
    <p>Adds a table of contents with links to sub-chapters between the chapter title heading and its contents.</p>
  </li>
  <li>
    <p>
      <b>Contents before TOC:</b>
      <p>
        Optional contents to be placed before the table of contents.
        Used in this documentation for placing the GitHub readme badges before the main TOC.
      </p>
    </p>
  </li>
</ul>),
    Chapter("GitHub Readme Badges",
<p>
  An example can be seen at the top of this very document.
  Those badges do not really have anything to do with GitHub itself.
  It's only that they became popular on GitHub projects.
</p>
<p>
  Badges can supply more up-to-date information on a project, such as the latest build status, coverage etc.
  They are usually simple images with links.
  Some standard kinds of badges are defined in Jackadocs.
  See an example by looking into <tt>ReadmeRoot:</tt>
</p>
<pre><code class="language-scala">
override def contentsBeforeTOC(root:RootChapter):NodeSeq =
{"<p>"}{{travisCIBadge}} {{mavenCentralBadge}} {{coverallsBadge}} {{codeFactorBadge}} {{snykBadge}}{"</p>"}
</code></pre>
<p>
  This is enabled by adding the <tt>BadgeGenerators</tt> trait, which requires only one further method to be defined:
</p>
<pre><code class="language-scala">
def docsMetaData:DocsMetaData = Main
</code></pre>
<p>
  Assigning the <tt>Main</tt> object is possible in this case because <tt>Main</tt> extends <tt>JackadocsMain</tt>, which in turn extends <tt>DocsMetaData</tt>.
  This is where all the badges get their URL components from, and you are free to override all available properties if needed, or leave them at their (usually reasonable) defaults.
</p>
    )
  )
}
