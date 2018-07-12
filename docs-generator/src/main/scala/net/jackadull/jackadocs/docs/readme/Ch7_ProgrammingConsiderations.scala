package net.jackadull.jackadocs.docs.readme

import net.jackadull.jackadocs.structure.{Chapter, RootChapter}

import scala.xml.NodeSeq

object Ch7_ProgrammingConsiderations extends Chapter {
  def title = "Programming Considerations"

  def contents(root:RootChapter):NodeSeq = NodeSeq.Empty

  override def subChapters:Seq[Chapter] = Seq(
    Chapter("Implicit String to NodeSeq Conversion",
<p>
  The <tt>Chapter</tt> trait contains an implicit conversion from <tt>String</tt> to <tt>NodeSeq</tt>.
  It is automatically in scope for all <tt>Chapter</tt> sub-types.
</p>
<p>
  This is especially useful for chapter titles.
  They are of type <tt>NodeSeq</tt>, but it is usually more comfortable to enter a simple string for the title.
</p>
    ),
    Chapter("Chapter Type Naming",
<p>
  When defining <tt>Chapter</tt> instances as Scala <tt>object</tt> types, the developer is free to choose a proper name.
  Everyone should make up their own conventions here, as suitable for the project at hand.
</p>
<p>
  In this documentation, the developer found it useful to name the chapter objects like the chapter titles, only in camel case.
  Additionally, the chapter number is prepended with a prefix, for example <tt>Ch3_ExampleChapter</tt>.
  This helps working with the code, as the IDE will automatically display chapters in proper order.
</p>
    ),
    Chapter("Further Remarks",
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
    )
  )
}
