package net.jackadull.jackadocs.docs.readme

import net.jackadull.jackadocs.structure.{Chapter, RootChapter}

import scala.xml.NodeSeq

object FurtherRemarks extends Chapter {
  def title = "Further Remarks"

  def contents(root:RootChapter):NodeSeq =
<ul>
  <li>
    <p>
      This project, which is also used as an example, uses Maven as build tool.
      But the same techniques as described here should also work with other tools, such as SBT or Gradle.
      No Maven plugins are necessary, and all the procedures rely solely on standard JVM functionality.
    </p>
  </li>
  <li>
    <p>
      When using a tool similar to Maven, the version of the <tt>docs-generator</tt> project can always remain a snapshot version.
      It will never be released.
    </p>
  </li>
  <li>
    <p>
      The implementation goes only as far as required for the author of Jackadocs.
      If you are missing any feature, consider requesting it politely, or implementing it yourself.
      You can offer a merge request.
    </p>
  </li>
</ul>
}
