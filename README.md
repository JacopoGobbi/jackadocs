# Jackadocs
[![Travis CI](https\:\/\/travis\-ci\.org\/jackadull\/jackadocs\.svg)](https\:\/\/travis\-ci\.org\/jackadull\/jackadocs) [![Maven Central](https\:\/\/img\.shields\.io\/maven\-central\/v\/net\.jackadull\/jackadocs\_2\.12\.svg)](https\:\/\/search\.maven\.org\/\#search\%7Cga\%7C1\%7Cg\%3A\%22net\.jackadull\%22\%20AND\%20a\%3A\%22jackadocs\_2\.12\%22) [![Coveralls](https\:\/\/coveralls\.io\/repos\/github\/jackadull\/jackadocs\/badge\.svg)](https\:\/\/coveralls\.io\/github\/jackadull\/jackadocs) [![Codefactor](https\:\/\/www\.codefactor\.io\/repository\/github\/jackadull\/jackadocs\/badge)](https\:\/\/www\.codefactor\.io\/repository\/github\/jackadull\/jackadocs) [![Snyk](https\:\/\/snyk\.io\/test\/github\/jackadull\/jackadocs\/badge\.svg)](https\:\/\/snyk\.io\/test\/github\/jackadull\/jackadocs)

* [1\. Motivation](\#1\-motivation)
* [2\. Intended Use \/ Basic Idea](\#2\-intended\-use\-\-basic\-idea)
* [3\. Usage Example](\#3\-usage\-example)
  * [3\.1\. How to Re\-Generate this Documentation](\#31\-how\-to\-re\-generate\-this\-documentation)
  * [3\.2\. The Main Class](\#32\-the\-main\-class)
* [4\. Chapter Structure](\#4\-chapter\-structure)
* [5\. Rendering Process](\#5\-rendering\-process)
  * [5\.1\. HTML to Markdown Conversion](\#51\-html\-to\-markdown\-conversion)
    * [5\.1\.1\. Markdown Basics\: Inlines and Blocks](\#511\-markdown\-basics\-inlines\-and\-blocks)
    * [5\.1\.2\. List of Supported Block Tags](\#512\-list\-of\-supported\-block\-tags)
    * [5\.1\.3\. List of Supported Inline Tags](\#513\-list\-of\-supported\-inline\-tags)
  * [5\.2\. Chapter Numbering](\#52\-chapter\-numbering)
  * [5\.3\. Debug Markdown Tree Output](\#53\-debug\-markdown\-tree\-output)
* [6\. Further Remarks](\#6\-further\-remarks)
* [7\. Roadmap](\#7\-roadmap)

Tool library for automated generation of tool documentation\. Can be used for creating `README.md` files\, but also for documentation books\, with multiple files\, in either HTML or [Github\-Flavored Markdown](https\:\/\/github\.github\.com\/gfm\/) \.

## 1\. Motivation
Jackadocs is useful for cases in which generation of documentation files should be partially dynamic\. That is\, the documentation contains parts that call Scala code for computing a part of the text\.

When writing and manually maintaining static Markdown or HTML files is sufficient for a certain project\, Jackadocs is not needed\.

Examples for meaningful usecases of Jackadocs include\:

* Using the Maven resources plugin in order to include Maven properties in the documentation\, such as library versions\.

* Reusing certain documentation templates across several projects\.

* Using loops for generating parts of the documentation\.

* Referring to code identifiers\, such as class names or constants in the code\, in the documentation\. Re\-generating the documentation then always inserts the most recent values of those code identifiers\.

## 2\. Intended Use \/ Basic Idea
There is no special magic in using Jackadocs\. The general approach is to create a class with a `main` method\, which overwrites all the documentation when called\. Jackadocs is just a library that gives some support with generating the documentation programmatically\.

Text fragments that will be composed into the documentation files are XML constants in the Scala code\. This XML is interpreted as HTML\. When writing documentation in Markdown format\, HTML will be converted to Markdown\. \(However\, the conversion process is not very smart\; don\'t expect miracles from it\. It will do just enough to translate simple documentation\, such as `README.md` files\.\)

[Here](https\:\/\/github\.com\/jackadull\/jackadocs\/blob\/master\/docs\-generator\/src\/main\/scala\/net\/jackadull\/jackadocs\/docs\/readme\/IntentedUse\.scala) is an example for the Scala source file from which this text is generated\.

In order to isolate documentation\-generating code from the actual module that is to be published\, it is advisable to keep the documentation generating code in a separate module\. This module can be kept in a sub\-folder of the main module\, but there should be no explicit relationship between the main module and the documentation generating module \(such as the Maven parent\/child relationship\)\. In fact\, the documentation generating module should never be published or deployed\. Its only purpose is to contain the utility code which gets called in order to \(re\-\)generate the main module\'s documentation\.

The recommended name for this module is `docs-generator` \. An example can be found in the [`docs-generator`](https\:\/\/github\.com\/jackadull\/jackadocs\/tree\/master\/docs\-generator) subfolder of the `jackadocs` project itself\.

Then\, every time before making a new release of the main module\, the `main` method of the `docs-generator` is to be called\. Documentation will be re\-generated\. By adhering to this general workflow\, documentation will always be up\-to date\.

This is the basic idea\. As written initially\, there is no further magic behind Jackadocs\. The following chapters will share some further advice on how to handle certain things\.

## 3\. Usage Example
As described\, there is no special magic in using Jackadocs\. Because there are many ways to use the tools presented by the Jackadocs library\, the best introduction is an example\. The reader can then make up his or her own way of preference of using Jackadocs\.

The sub\-module and code that generates this text serves as the example\. It can be found in the [`docs-generator`](https\:\/\/github\.com\/jackadull\/jackadocs\/tree\/master\/docs\-generator) subfolder of the `jackadocs` project\. In it\, you will find\:

* A separate `pom.xml` for the documentation\-generating project\.

* Scala souce code under `src/main/scala` that contains all the data for generating this documentation\.

### 3\.1\. How to Re\-Generate this Documentation
The documentation of Jackadocs \(i\.e\.\, the `README.md` in the `jackadocs` project root folder\) is \(re\-\)generated by choosing `docs-generator` as the current working directory and executing\:

```bash
mvn clean compile exec:java
```
Maven will then clean up the target folder\, compile the project\, and run the main class\. The details of this execution are defined in `docs-generator/pom.xml` \, in the configuration of the `exec-maven-plugin` \: As can bee seen there\, the main class \(i\.e\.\, the class that contains the `main` method\) is `net.jackadull.jackadocs.docs.Main` \. Also\, the first \(and only\) command\-line argument for the execution is configured as `${project.basedir}` \. This means that the path to the `docs-generator` project folder will be passed to the `main` method\.

### 3\.2\. The Main Class
The source code of the main class can be found [here](https\:\/\/github\.com\/jackadull\/jackadocs\/blob\/kickoff\/docs\-generator\/src\/main\/scala\/net\/jackadull\/jackadocs\/docs\/Main\.scala) \. The main class does very few things\:

* Create the main `Jackadocs` instance\:

  ```scala
  val jackadocs = Jackadocs fromArgs args
  ```
  The command\-line arguments are passed to the factory method\. This way\, the `Jackadocs` instance knows the project root folder\.

* Check if dependency for the main project has the right version\:

  ```scala
  jackadocs.requirePOMVersion("../pom.xml")(JackadocsInfo Version)
  ```
  Asserts that the nominal version of the parent library dependency is actually the same as the version declared in the `pom.xml` of the parent folder\. Every time the version of the main Jackadocs `pom.xml` is changed\, the dependency version in `docs-generator/pom.xml` must also be changed\. This line of code will ensure that this has happened\.

  `requirePOMVersion` reads the `pom.xml` at the given relative path\. \(That is\, relative to the `docs-generator` project folder\.\) It then uses a simple XML lookup to find the declared project version\. This must be a constant\, which is also encouraged by Maven\. After it found the POM\'s project version\, it compares the string with the second given argument\. If they do not match\, an exception is thrown\.

  In this case\, the version number to compare against is given as `JackadocsInfo.Version` \. This is possible because the parent project is configured in such a way that Maven will generate a static class called `JackadocsInfo` \, which contains the Maven module version as the constant named `Version` \.

  If you are interested in copying this behaviour\, take a look at the Jackadocs main `pom.xml` \. The `templating-maven-plugin` does the job of creating said static class\. The template can be found under `src/main/scala-templates` \.

* Generate the `README.md` file\:

  ```scala
  jackadocs generateAt "../README.md" markdownFor ReadmeRoot
  ```
  Tells Jackadocs to generate the Markdown for `ReadmeRoot` and write it into `"../README.md"` \, relative to the project base directory that was passed in as a command\-line argument\.

  `ReadmeRoot` can be found [here](https\:\/\/github\.com\/jackadull\/jackadocs\/blob\/master\/docs\-generator\/src\/main\/scala\/net\/jackadull\/jackadocs\/docs\/ReadmeRoot\.scala) \. The contents of this object follow the chapter structure\, which gets described next\.

## 4\. Chapter Structure
The contents of every Jackadocs\-based document are laid out in a hierarchy of chapters and sub\-chapters\. Chapters are Scala objects\. There are two major ways how to declare chapters in the code\:

1. As top\-level\, standalone Scala `object` types that extend the `Chapter` trait\. [The code for this very chapter](https\:\/\/github\.com\/jackadull\/jackadocs\/blob\/master\/docs\-generator\/src\/main\/scala\/net\/jackadull\/jackadocs\/docs\/readme\/ChapterStructure\.scala) is an example of this\.

2. As inline constants\, similar to case class instances\, within the code\. [The`UsageExample`object](https\:\/\/github\.com\/jackadull\/jackadocs\/blob\/master\/docs\-generator\/src\/main\/scala\/net\/jackadull\/jackadocs\/docs\/readme\/UsageExample\.scala) contains several examples\, which can be found when looking at its `subChapters` method\.

The root\-level object for every Jackadocs\-based documents is an instance of `Chapter` \. The title of the root chapter is the title of the document\. Its contents are the top\-level text of the document\. The sub\-chapters of the root\-level chapter are the chapters of the document\.

Of course\, the structure is recursive\. Sub\-chapters can have sub\-chapters of their own\, and so on\. In this way\, a tree\-like chapter structure gets assembled\.

Looking at [the source code of`ReadmeRoot`](https\:\/\/github\.com\/jackadull\/jackadocs\/blob\/master\/docs\-generator\/src\/main\/scala\/net\/jackadull\/jackadocs\/docs\/ReadmeRoot\.scala) can be a helpful illustration of these abstract descriptions\.

## 5\. Rendering Process
As explained before\, the command `jackadocs generateAt "../README.md" markdownFor ReadmeRoot` renders the Markdown version of the root chapter into the given file\.

The source format of the data is always HTML\, as can be seen when looking at the Scala sources that generate this document\. In this case however\, the output format is Markdown\. This obviously means that some form of conversion from HTML to Markdown is going on as part of the rendering process\.

### 5\.1\. HTML to Markdown Conversion
When converting from source HTML to Markdown\, a very pragmatic process is used\. It recognizes specific patterns of HTML code and converts those into specific patterns of Markdown code\. Everything that does not match those expected patterns gets ignored \(or converted to plain text\) and will therefore probably yield an unwanted result\.

That is to say\, the HTML\-to\-Markdown conversion is far from trying to be smart\. But if you follow certain simple rules when assembling the HTML\, you will get the desired result\.

\(Note\: When speaking of Markdown within the scope of this document\, this specifically refers to Github\-Flavored Markdown\. [Here is a link](https\:\/\/github\.github\.com\/gfm\/) to the specification\.\)

#### 5\.1\.1\. Markdown Basics\: Inlines and Blocks
When composing HTML that translates well to Markdown\, keep in mind one foundation of Github\-Flavored Markdown\: Every Markdown element is either a block or an inline\. The concept is very similar to HTML\/CSS block and inline rendering\.

Blocks are top\-level constructs\. They can not be nested arbitrarily\.

Some types of blocks can be nested\. For example\, a list item can contain another list\, and one of the inner list\'s items can contain a blockquote\. The details of this kind of nesting can be found in [the specification](https\:\/\/github\.github\.com\/gfm\/) \, as mentioned before\.

Other types of blocks cannot be nested\. For example\, suppose there is a sequence of paragraphs that are nested inside one large paragraph\. HTML rendering will keep the inner paragraphs as separate\, i\.e\. they will be separated by paragraph breaks\.

However\, when converting this kind of HTML to markdown\, only the outer paragraph will be preserved\. The inner paragraphs will be concatenated to just one large paragraph\. This is because the conversion process does follow a bottom\-up sequence\, but rather it goes top down\: When descending from the root of the HTML tree\, the first paragraph tag encountered is \"the\" paragraph for the scope of its children\. And in Markdown\, paragraphs cannot be nested inside other paragraphs\.

Inlines in Markdown are everything else\: plain text\, bold or italic formatting\, hyperlinks\, inline code spans\, etc\.

#### 5\.1\.2\. List of Supported Block Tags
What follows is a list of supported HTML tags that get translated to Markdown blocks\.

* `<blockquote>…</blockquote>` \: Gets translated into Markdown blockquote\. Can contain other blocks\.

* `<code>…</code>` or `<tt>…</tt>` \: Gets translated into a Markdown Code Fence\.

  Can optionally contain the info string as a `class` attribute prefixed with `language-` \, for example\:

  `<code class="language-scala">…</code>`

  Contained text gets interpreted literaly\. This block cannot contain other blocks or inlines\.

* `<p>…</p>` \: Gets translated into a Markdown paragraph\. Can contain inlines\.

  Blocks should rather not be nested inside a paragraph\. For example\, if you would like to include a list inside a paragraph\, rather create the paragraph before the list\, then create the list as sibling to it\, and then as the next sibling a new paragraph with the remainder after the list\.

* `<ul>…</ul>` or `<ol>…</ol>` \: Gets translated into an unordered or ordered list\, respectively\. As with HTML\, list items should be `<li>…</li>` child tags\.

  List items can contain any kind of block\. Children of a list that are not `li` are ignored\.

#### 5\.1\.3\. List of Supported Inline Tags
These HTML tags get translated to Markdown inlines\:

* `<a href="…">…</a>` \: Gets converted to a Markdown link\. Can contain other inlines as children\. Optionally\, allows for a `title` attribute\.

* `<b>…</b>` \: Gets converted to Markdown strong emphasis\. Can contain other inlines\.

* `<code>…</code>` or `<tt>…</tt>` \: Gets converted to a Markdown code span\. Cannot contain other inlines\, only plain text\.

  Such a tag can be interpreted as a block \(code fence\, see above\) or as an inline \(code span\)\. How it gets interpreted depends on its position\: When the converter expects block\-level elements\, it gets interpreted as a code fence\. Otherwise\, it gets interpreted as code span\.

* `<del>…</del>` \: Gets converted to Markdown strikethrough \(GFM extension\)\. Can contain other inlines as children\.

* `<em>…</em>` or `<i>…</i>` \: Gets converted to Markdown emphasis\.

* `<img src="..." alt="..."/>` \: Gets translated into a Markdown image\.

### 5\.2\. Chapter Numbering
The rendering process also prefixes chapter numbers before the titles\. The numbering strategy is passed into the process as an instance of the `ChapterNumbering` trait\.

`ChapterNumbering` is an immutable chapter number counter\. It has a current state\, i\.e\. the next chapter number that is to be generated\. From that state\, one can go in three directions\:

* To the next sibling\, by calling `count(Chapter)` \. This returns a tuple\. The first element is the number for the given chapter as a string\. The second element is the `ChapterNumbering` instance to use for the following chapters\.

  For example\, if the last returned chapter number is `1.2.3` and `count` is called on the `ChapterNumbering` instance\, then `1.2.4` gets returned \(along with the next `ChapterNumbering` \)\.

* Descending into a sub\-chapter\, by calling `subChapters` \. This returns another `ChapterNumbering` instance that enumerates the chapter numbers for sub\-chapters\.

  For example\, if the last returned chapter number is `1.2.3` and `subChapters` is called\, the returned `ChapterNumbering` returns the chapter number `1.2.3.1` on the next call to its `count(Chapter)` method\.

  It is important to notice that the initial `ChapterNumbering` should not be reused when ascending back up from the sub\-chapters\. For this case\, look at the next bullet point\.

* Ascending back up from sub\-chapters\, by calling `parent` \. This returns another `ChapterNumbering` instance to be used for the following chapters\.

  For example\, suppose that we start with a `ChapterNumbering` instance which last counted chapter `1.2.3` \. Suppose `subChapters` was called on it\, an subsequently\, chapter numbers `1.2.3.1` \, `1.2.3.2` and `1.2.3.3` have been generated with the sub\-chapters `ChapterNumbering` instances\. Afterwards\, `parent` was called on the last `ChapterNumbering` \. This returns a `ChapterNumbering` instance which will count the next chapter as `1.2.4` \.

Keep two things in mind\: 1\.\) Never use any single `ChapterNumbering` instance to count more than one chapter\. After counting\, it also returns the next `ChapterNumbering` instance to be used for subsequent chapters\. 2\.\) Chapter numbers like `1.2.3` are just examples\; other implementations can generate Roman numerals\, alphabet letters or any other kind of counting\, on various chapter depth levels\.

By default\, `ChapterNumbering.empty` is used\. This will leave chapter numbers empty\. A simple alternative is `ChapterNumbering.decimal` \.

### 5\.3\. Debug Markdown Tree Output
If anything goes bad with regards to Markdown rendering\, you can always print a debug tree of the Markdown view\. For example\:

```scala
RenderAsMarkdown(ReadmeRoot, ChapterNumbering.empty) foreach {md ⇒ println(md.treeStructure())}
```
`RenderAsMarkdown` returns a sequence of Markdown elements\. On each of those elements\, `treeStructure()` is called\. This returns a string representation of this part of the Markdown tree\, with all children\.

## 6\. Further Remarks
* This project\, which is also used as an example\, uses Maven as build tool\. But the same techniques as described here should also work with other tools\, such as SBT or Gradle\. No Maven plugins are necessary\, and all the procedures rely solely on standard JVM functionality\.

* When using a tool similar to Maven\, the version of the `docs-generator` project can always remain a snapshot version\. It will never be released\.

* The implementation goes only as far as required for the author of Jackadocs\. If you are missing any feature\, consider requesting it politely\, or implementing it yourself\. You can offer a merge request\.

## 7\. Roadmap
Here are some features that are considered or planned for future implementation\:

* Links to other chapters

* Migrate the whole Markdown to [Jackadull GFM](https\:\/\/github\.com\/jackadull\/gfm)

