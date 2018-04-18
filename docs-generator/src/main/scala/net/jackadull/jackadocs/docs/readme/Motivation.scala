package net.jackadull.jackadocs.docs.readme

import net.jackadull.jackadocs.structure.Chapter

import scala.xml.{NodeSeq, Text}

object Motivation extends Chapter {
  def id:String = "motivation"
  def title:NodeSeq = Text("Motivation")

  def contents:NodeSeq =
<p>
  <tt>jackadocs</tt> is useful for cases in which generation of documentation files should be partially dynamic.
  That is, the documentation contains parts that call Scala code for computing a part of the text.
</p>
<p>
  When writing and manually maintaining static Markdown or HTML files is sufficient for a certain project, <tt>jackadocs</tt> is not needed.
</p>
<p>
  Examples for meaningful usecases of <tt>jackadocs</tt> include:
</p>
<ul>
  <li>Using the Maven resources plugin in order to include Maven properties in the documentation, such as library versions.</li>
  <li>Reusing certain documentation templates across several projects.</li>
  <li>Using loops for generating parts of the documentation.</li>
  <li>
    Referring to code identifiers, such as class names or constants in the code, in the documentation.
    Re-generating the documentation then always inserts the most recent values of those code identifiers.
  </li>
</ul>
}
