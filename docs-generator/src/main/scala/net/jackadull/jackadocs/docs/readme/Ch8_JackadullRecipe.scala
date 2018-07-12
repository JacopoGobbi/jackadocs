package net.jackadull.jackadocs.docs.readme

import net.jackadull.jackadocs.structure.{Chapter, RootChapter}

import scala.xml.NodeSeq

object Ch8_JackadullRecipe extends Chapter {
  def title = "Jackadull Recipe"

  def contents(root:RootChapter):NodeSeq =
<p>
  All elements required for running Jackadocs as part of a project have been described in the previous chapters.
  For tying things up, here is a little list of bullet points that describe the common practice when used in Jackadull projects.
  If you wish, you can use it as a checklist or an inspiration for your own project.
</p>
<ul>
  <li>
    <p>In the <tt>docs-generator</tt> sub-folder:</p>
    <ul>
      <li>Main class that extends <tt>App</tt> and <tt>JackadocsMain</tt></li>
      <li><p><tt>ReadmeRoot</tt> as entry point for rendering, extending <tt>RootChapter</tt></p></li>
      <li>
        <p>Main class rendering the readme file:</p>
<pre><code class="language-scala">
jackadocs generateAt s"$projectDir/README.md" markdownFor ReadmeRoot
</code></pre>
      </li>
      <li>
        <p><tt>docs-generator</tt> POM:</p>
        <ul>
          <li>Version number obviously not made for releasing, such as <tt>0-SNAPSHOT</tt></li>
          <li>Dependency on main project and Jackadocs</li>
          <li>Configuration of <tt>exec-maven-plugin</tt> for calling the main class</li>
          <li>When <tt>jackadull-release</tt> profile is active, also pass <tt>-V</tt> to the main class</li>
        </ul>
      </li>
    </ul>
  </li>
  <li>
    <p>In the main project:</p>
    <ul>
      <li>
        <p>A template for the project runtime information type, under <tt>src/main/scala-templates</tt>, following <a href="https://github.com/jackadull/jackadocs/blob/release/latest/src/main/scala-templates/net/jackadull/jackadocs/JackadocsInfo.scala">this example</a></p>
      </li>
      <li>
        <p>In the main project POM:</p>
        <ul>
          <li>Inclusion of <tt>templating-maven-plugin</tt>, following the Jackadocs example</li>
          <li>Usage of <tt>exec-maven-plugin</tt> for calling <tt>mvn clean compile exec:java</tt> in the <tt>docs-generator</tt> sub-folder after the <tt>install</tt> phase of the main project</li>
          <li>When the <tt>jackadull-release</tt> profile is active, pass it on to <tt>docs-generator</tt> via <tt>exec-maven-plugin</tt></li>
        </ul>
      </li>
    </ul>
  </li>
</ul>
<p>
  When all of this is in place, simply call <tt>mvn install</tt> in the main project folder for (re-)generating the documentation.
</p>
}
