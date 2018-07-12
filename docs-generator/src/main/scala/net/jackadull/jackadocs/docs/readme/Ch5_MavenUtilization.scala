package net.jackadull.jackadocs.docs.readme

import net.jackadull.jackadocs.structure.{Chapter, RootChapter}

import scala.xml.NodeSeq

object Ch5_MavenUtilization extends Chapter {
  def title = "Maven Utilization"

  def contents(root:RootChapter):NodeSeq =
<p>
  Maven utilization for Jackadocs has been added to:
</p>
<ul>
  <li>The <a href="https://github.com/jackadull/jackadocs/blob/release/latest/pom.xml">POM of the main project</a></li>
  <li>The <a href="https://github.com/jackadull/jackadocs/blob/release/latest/docs-generator/pom.xml">POM of indepdendent project in the <tt>docs-generator</tt> folder</a></li>
</ul>
<p>
  The utilization involves usage of the <tt>exec-maven-plugin</tt>.
</p>
<p>
  In the <tt>docs-generator</tt> project, the plugin is used for the <tt>exec:java</tt> goal, for calling the <tt>Main</tt> class.
  This re-generates the project documentation.
</p>
<p>
  A variant of this happens when the <tt>jackadull-release</tt> profile is active:
  In that case, documentation will not be overwritten.
  Instead, the program validates that all of the documentation files are at the latest state.
  If not, the program terminates with an error.
  This is to prevent that new releases are being produced with an outdated version of the documentation.
</p>
<p>
  In the main project, the plugin is used for the <tt>exec:exec</tt> goal, which executes a Maven child process in the <tt>docs-generator</tt> folder, re-generating documentation.
  It will be executed automatically after the <tt>install</tt> phase.
  This is because after main project version updates, the main project must first be installed locally, so it can be pulled as a depdendency in <tt>docs-generator</tt>.
</p>
<p>
  The main project will pass on the <tt>jackadull-release</tt> profile to <tt>docs-generator</tt> if active.
</p>
<p>
  All of this can be studied by the example of the Jackadull project.
</p>
}
