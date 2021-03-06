package net.jackadull.jackadocs.docs.readme

import net.jackadull.jackadocs.structure.{Chapter, RootChapter}

import scala.xml.NodeSeq

object Ch2_IntentedUse extends Chapter {
  def title = "Intended Use / Basic Idea"

  def contents(root:RootChapter):NodeSeq =
<p>
  There is no special magic in using Jackadocs.
  The general approach is to create a class with a <tt>main</tt> method, which overwrites all the documentation when called.
  Jackadocs is just a library that gives some support with generating the documentation programmatically.
</p>
<p>
  This documentation also shows some more ideas that are common amongst Jackadull projects.
  They include a certain use of Maven for including the generation of documentation in the build cycle.
</p>
<p>
  Every developer can make up their own best practices.
  This document can be seen as a source of inspiration.
</p>
<p>
  Source fragments that will be composed into documentation files are XML constants in the Scala code.
  This XML is interpreted as HTML.
  When writing documentation in Markdown format, HTML will be converted to Markdown.
  (However, the conversion process is not very smart; don't expect miracles from it.
  It will do just enough to translate simple documentation, such as <tt>README.md</tt> files.)
</p>
<p>
  <a href="https://github.com/jackadull/jackadocs/blob/release/latest/docs/src/main/scala/net/jackadull/jackadocs/docs/readme/IntentedUse.scala">Here</a> is an example for the Scala source file from which this text is generated.
</p>
<p>
  In order to isolate documentation-generating code from the actual module that is to be published, it is advisable to keep the documentation generating code in a separate module (or SBT project).
  This module can be kept in a sub-folder of the main module, but there should be no explicit relationship between the main module and the documentation generating module (such as the Maven parent/child relationship).
  In SBT, the documentation module should have <tt>dependsOn(LocalRootProject)</tt>, and the root project should have `aggregates(docs)`.
</p>
<p>
  The documentation generating module should never be published or deployed.
  Its only purpose is to contain the utility code which gets called in order to (re-)generate the main module's documentation.
</p>
<p>
  The recommended name for this module is <tt>docs</tt>.
  An example can be found in the <a href="https://github.com/jackadull/jackadocs/tree/release/latest/docs"><tt>docs</tt></a> subfolder of the <tt>jackadocs</tt> project itself.
</p>
<p>
  Then, every time before making a new release of the main module, the <tt>main</tt> method of the <tt>docs</tt> is to be called.
  Documentation will be re-generated.
  By adhering to this general workflow, documentation will always be up-to date.
  This part can be automated using Maven or SBT, as shown below.
</p>
<p>
  This is the basic idea.
  As written initially, there is no further magic behind Jackadocs.
  The following chapters will share some further advice on how to handle certain things.
</p>
}
