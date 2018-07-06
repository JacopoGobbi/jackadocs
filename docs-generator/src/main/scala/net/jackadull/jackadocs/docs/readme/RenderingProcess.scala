package net.jackadull.jackadocs.docs.readme

import net.jackadull.jackadocs.structure.Chapter

import scala.language.postfixOps
import scala.xml.{NodeSeq, Text}

object RenderingProcess extends Chapter {
  def id = "rendering_process"
  def title = Text("Rendering Process")

  def contents:NodeSeq =
<p>
  As explained before, the command <tt>jackadocs generateAt "../README.md" markdownFor ReadmeRoot</tt> renders the Markdown version of the root chapter into the given file.
</p>
<p>
  The source format of the data is always HTML, as can be seen when looking at the Scala sources that generate this document.
  In this case however, the output format is Markdown.
  This obviously means that some form of conversion from HTML to Markdown is going on as part of the rendering process.
</p>

  override def subChapters:Seq[Chapter] = Seq(
    Chapter("html_to_markdown_conversion", Text("HTML to Markdown Conversion"),
<p>
  When converting from source HTML to Markdown, a very pragmatic process is used.
  It recognizes specific patterns of HTML code and converts those into specific patterns of Markdown code.
  Everything that does not match those expected patterns gets ignored (or converted to plain text) and will therefore probably yield an unwanted result.
</p>
<p>
  That is to say, the HTML-to-Markdown conversion is far from trying to be smart.
  But if you follow certain simple rules when assembling the HTML, you will get the desired result.
</p>
<p>
  (Note:
  When speaking of Markdown within the scope of this document, this specifically refers to Github-Flavored Markdown.
  <a href="https://github.github.com/gfm/">Here is a link</a> to the specification.)
</p>
      ,subChapters=Seq(
        Chapter("markdown_basics_inlines_and_blocks", Text("Markdown Basics: Inlines and Blocks"),
<p>
  When composing HTML that translates well to Markdown, keep in mind one foundation of Github-Flavored Markdown:
  Every Markdown element is either a block or an inline.
  The concept is very similar to HTML/CSS block and inline rendering.
</p>
<p>
  Blocks are top-level constructs.
  They can not be nested arbitrarily.
</p>
<p>
  Some types of blocks can be nested.
  For example, a list item can contain another list, and one of the inner list's items can contain a blockquote.
  The details of this kind of nesting can be found in <a href="https://github.github.com/gfm/">the specification</a>, as mentioned before.
</p>
<p>
  Other types of blocks cannot be nested.
  For example, suppose there is a sequence of paragraphs that are nested inside one large paragraph.
  HTML rendering will keep the inner paragraphs as separate, i.e. they will be separated by paragraph breaks.
</p>
<p>
  However, when converting this kind of HTML to markdown, only the outer paragraph will be preserved.
  The inner paragraphs will be concatenated to just one large paragraph.
  This is because the conversion process does follow a bottom-up sequence, but rather it goes top down:
  When descending from the root of the HTML tree, the first paragraph tag encountered is "the" paragraph for the scope of its children.
  And in Markdown, paragraphs cannot be nested inside other paragraphs.
</p>
<p>
  Inlines in Markdown are everything else:
  plain text, bold or italic formatting, hyperlinks, inline code spans, etc.
</p>
        ),
        Chapter("list_of_supported_block_tags", Text("List of Supported Block Tags"),
<p>
  What follows is a list of supported HTML tags that get translated to Markdown blocks.
</p>
<ul>
  <li>
    <p>
      <tt>{<blockquote>…</blockquote> toString}</tt>:
      Gets translated into Markdown blockquote.
      Can contain other blocks.
    </p>
  </li>
  <li>
    <p>
      <tt>{<code>…</code> toString}</tt> or <tt>{<tt>…</tt> toString()}</tt>:
      Gets translated into a Markdown Code Fence.
    </p>
    <p>
      Can optionally contain the info string as a <tt>class</tt> attribute prefixed with <tt>language-</tt>, for example:
    </p>
    <p>
      <tt>{<code class="language-scala">…</code> toString}</tt>
    </p>
    <p>
      Contained text gets interpreted literaly.
      This block cannot contain other blocks or inlines.
    </p>
  </li>
  <li>
    <p>
      <tt>{<p>…</p> toString}</tt>:
      Gets translated into a Markdown paragraph.
      Can contain inlines.
    </p>
    <p>
      Blocks should rather not be nested inside a paragraph.
      For example, if you would like to include a list inside a paragraph, rather create the paragraph before the list, then create the list as sibling to it, and then as the next sibling a new paragraph with the remainder after the list.
    </p>
  </li>
  <li>
    <p>
      <tt>{<ul>…</ul> toString}</tt> or <tt>{<ol>…</ol> toString}</tt>:
      Gets translated into an unordered or ordered list, respectively.
      As with HTML, list items should be <tt>{<li>…</li> toString}</tt> child tags.
    </p>
    <p>
      List items can contain any kind of block.
      Children of a list that are not <tt>li</tt> are ignored.
    </p>
  </li>
</ul>
        ),
        Chapter("list_of_supported_inline_tags", Text("List of Supported Inline Tags"),
<p>
  These HTML tags get translated to Markdown inlines:
</p>
<ul>
  <li>
    <p>
      <tt>{<a href="…">…</a> toString}</tt>:
      Gets converted to a Markdown link.
      Can contain other inlines as children.
      Optionally, allows for a <tt>title</tt> attribute.
    </p>
  </li>
  <li>
    <p>
      <tt>{<b>…</b> toString}</tt>:
      Gets converted to Markdown strong emphasis.
      Can contain other inlines.
    </p>
  </li>
  <li>
    <p>
      <tt>{<code>…</code> toString}</tt> or <tt>{<tt>…</tt> toString}</tt>:
      Gets converted to a Markdown code span.
      Cannot contain other inlines, only plain text.
    </p>
    <p>
      Such a tag can be interpreted as a block (code fence, see above) or as an inline (code span).
      How it gets interpreted depends on its position:
      When the converter expects block-level elements, it gets interpreted as a code fence.
      Otherwise, it gets interpreted as code span.
    </p>
  </li>
  <li>
    <p>
      <tt>{<del>…</del> toString}</tt>:
      Gets converted to Markdown strikethrough (GFM extension).
      Can contain other inlines as children.
    </p>
  </li>
  <li>
    <p>
      <tt>{<em>…</em> toString}</tt> or <tt>{<i>…</i> toString}</tt>:
      Gets converted to Markdown emphasis.
    </p>
  </li>
  <li>
    <p>
      <tt>{<img src="..." alt="..." /> toString}</tt>:
      Gets translated into a Markdown image.
    </p>
  </li>
</ul>
        )
      )
    ),
    Chapter("chapter_numbering", Text("Chapter Numbering"),
<p>
  The rendering process also prefixes chapter numbers before the titles.
  The numbering strategy is passed into the process as an instance of the <tt>ChapterNumbering</tt> trait.
</p>
<p>
  <tt>ChapterNumbering</tt> is an immutable chapter number counter.
  It has a current state, i.e. the next chapter number that is to be generated.
  From that state, one can go in three directions:
</p>
<ul>
  <li>
    <p>
      To the next sibling, by calling <tt>count(Chapter)</tt>.
      This returns a tuple.
      The first element is the number for the given chapter as a string.
      The second element is the <tt>ChapterNumbering</tt> instance to use for the following chapters.
    </p>
    <p>
      For example, if the last returned chapter number is <tt>1.2.3</tt> and <tt>count</tt> is called on the <tt>ChapterNumbering</tt> instance, then <tt>1.2.4</tt> gets returned (along with the next <tt>ChapterNumbering</tt>).
    </p>
  </li>
  <li>
    <p>
      Descending into a sub-chapter, by calling <tt>subChapters</tt>.
      This returns another <tt>ChapterNumbering</tt> instance that enumerates the chapter numbers for sub-chapters.
    </p>
    <p>
      For example, if the last returned chapter number is <tt>1.2.3</tt> and <tt>subChapters</tt> is called, the returned <tt>ChapterNumbering</tt> returns the chapter number <tt>1.2.3.1</tt> on the next call to its <tt>count(Chapter)</tt> method.
    </p>
    <p>
      It is important to notice that the initial <tt>ChapterNumbering</tt> should not be reused when ascending back up from the sub-chapters.
      For this case, look at the next bullet point.
    </p>
  </li>
  <li>
    <p>
      Ascending back up from sub-chapters, by calling <tt>parent</tt>.
      This returns another <tt>ChapterNumbering</tt> instance to be used for the following chapters.
    </p>
    <p>
      For example, suppose that we start with a <tt>ChapterNumbering</tt> instance which last counted chapter <tt>1.2.3</tt>.
      Suppose <tt>subChapters</tt> was called on it, an subsequently, chapter numbers <tt>1.2.3.1</tt>, <tt>1.2.3.2</tt> and <tt>1.2.3.3</tt> have been generated with the sub-chapters <tt>ChapterNumbering</tt> instances.
      Afterwards, <tt>parent</tt> was called on the last <tt>ChapterNumbering</tt>.
      This returns a <tt>ChapterNumbering</tt> instance which will count the next chapter as <tt>1.2.4</tt>.
    </p>
  </li>
</ul>
<p>
  Keep two things in mind:
  1.) Never use any single <tt>ChapterNumbering</tt> instance to count more than one chapter. After counting, it also returns the next <tt>ChapterNumbering</tt> instance to be used for subsequent chapters.
  2.) Chapter numbers like <tt>1.2.3</tt> are just examples; other implementations can generate Roman numerals, alphabet letters or any other kind of counting, on various chapter depth levels.
</p>
<p>
  By default, <tt>ChapterNumbering.empty</tt> is used.
  This will leave chapter numbers empty.
  A simple alternative is <tt>ChapterNumbering.decimal</tt>.
</p>
    ),
    Chapter("debug_markdown_tree_output", Text("Debug Markdown Tree Output"),
<p>
  If anything goes bad with regards to Markdown rendering, you can always print a debug tree of the Markdown view.
  For example:
</p>
<pre><code class="language-scala">
RenderAsMarkdown(ReadmeRoot, ChapterNumbering.empty) foreach {{md ⇒ println(md.treeStructure())}}
</code></pre>
<p>
  <tt>RenderAsMarkdown</tt> returns a sequence of Markdown elements.
  On each of those elements, <tt>treeStructure()</tt> is called.
  This returns a string representation of this part of the Markdown tree, with all children.
</p>
    )
  )
}
