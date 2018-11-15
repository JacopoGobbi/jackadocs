package net.jackadull.jackadocs.docs.readme

import net.jackadull.jackadocs.structure.{Chapter, RootChapter}

import scala.xml.NodeSeq

object Ch1_Motivation extends Chapter {
  def title = "Motivation"

  def contents(root:RootChapter):NodeSeq =
<p>
  Jackadocs is useful for cases in which generation of documentation files should be partially dynamic.
  That is, the documentation contains parts that call Scala code for computing a part of the text.
</p>
<p>
  When writing and manually maintaining static Markdown or HTML files is sufficient for a certain project, Jackadocs is not needed.
</p>
<p>
  Examples for meaningful usecases of Jackadocs include:
</p>
<ul>
  <li>Calling code from the main SBT project in order to document the actual outcome of statements.</li>
  <li>Using the Maven resources plugin or SBT in order to include Maven properties in the documentation, such as library versions.</li>
  <li>Reusing certain documentation templates across several projects.</li>
  <li>Using loops for generating parts of the documentation.</li>
  <li>
    Referring to code identifiers, such as class names or constants in the code, in the documentation.
    Re-generating the documentation then always inserts the most recent values of those code identifiers.
  </li>
</ul>
}
