# Jackadocs
[![Travis CI](https\:\/\/travis\-ci\.org\/jackadull\/jackadocs\.svg)](https\:\/\/travis\-ci\.org\/jackadull\/jackadocs) [![Maven Central](https\:\/\/img\.shields\.io\/maven\-central\/v\/net\.jackadull\/jackadocs\.svg)](https\:\/\/search\.maven\.org\/\#search\%7Cga\%7C1\%7Cg\%3A\%22net\.jackadull\%22\%20AND\%20a\%3A\%22jackadocs\%22) [![Scaladoc](http\:\/\/javadoc\-badge\.appspot\.com\/net\.jackadull\/jackadocs\.svg\?label\=scaladoc)](http\:\/\/javadoc\-badge\.appspot\.com\/net\.jackadull\/jackadocs) [![Coveralls](https\:\/\/coveralls\.io\/repos\/github\/jackadull\/jackadocs\/badge\.svg)](https\:\/\/coveralls\.io\/github\/jackadull\/jackadocs) [![Codefactor](https\:\/\/www\.codefactor\.io\/repository\/github\/jackadull\/jackadocs\/badge)](https\:\/\/www\.codefactor\.io\/repository\/github\/jackadull\/jackadocs) [![Snyk](https\:\/\/snyk\.io\/test\/github\/jackadull\/jackadocs\/badge\.svg)](https\:\/\/snyk\.io\/test\/github\/jackadull\/jackadocs)

* [1\. Motivation](\#1\-motivation)
* [2\. Intended Use \/ Basic Idea](\#2\-intended\-use\-\-basic\-idea)
* [3\. Usage Example](\#3\-usage\-example)
  * [3\.1\. How to Re\-Generate this Documentation](\#31\-how\-to\-re\-generate\-this\-documentation)
    * [3\.1\.1\. Automation in the Main Project](\#311\-automation\-in\-the\-main\-project)
  * [3\.2\. The Main Class](\#32\-the\-main\-class)
* [4\. Chapter Structure](\#4\-chapter\-structure)
  * [4\.1\. TOC Options](\#41\-toc\-options)
  * [4\.2\. GitHub Readme Badges](\#42\-github\-readme\-badges)
* [5\. Rendering Process](\#5\-rendering\-process)
  * [5\.1\. HTML to Markdown Conversion](\#51\-html\-to\-markdown\-conversion)
    * [5\.1\.1\. Markdown Basics\: Inlines and Blocks](\#511\-markdown\-basics\-inlines\-and\-blocks)
    * [5\.1\.2\. List of Supported Block Tags](\#512\-list\-of\-supported\-block\-tags)
    * [5\.1\.3\. List of Supported Inline Tags](\#513\-list\-of\-supported\-inline\-tags)
  * [5\.2\. Chapter Numbering](\#52\-chapter\-numbering)
    * [5\.2\.1\. ChapterNumbering Sequencing](\#521\-chapternumbering\-sequencing)
  * [5\.3\. Debug Markdown Tree Output](\#53\-debug\-markdown\-tree\-output)
* [6\. Programming Considerations](\#6\-programming\-considerations)
  * [6\.1\. Implicit String to NodeSeq Conversion](\#61\-implicit\-string\-to\-nodeseq\-conversion)
  * [6\.2\. Chapter Type Naming](\#62\-chapter\-type\-naming)
  * [6\.3\. Further Remarks](\#63\-further\-remarks)
* [7\. Jackadull Recipe](\#7\-jackadull\-recipe)

Tool library for automated generation of tool documentation\. Can be used for creating `README.md` files\, but also for documentation books\, with multiple files\, in either HTML or [Github\-Flavored Markdown](https\:\/\/github\.github\.com\/gfm\/) \.

## 1\. Motivation
Jackadocs is useful for cases in which generation of documentation files should be partially dynamic\. That is\, the documentation contains parts that call Scala code for computing a part of the text\.

When writing and manually maintaining static Markdown or HTML files is sufficient for a certain project\, Jackadocs is not needed\.

Examples for meaningful usecases of Jackadocs include\:

* Calling code from the main SBT project in order to document the actual outcome of statements\.

* Using the Maven resources plugin or SBT in order to include Maven properties in the documentation\, such as library versions\.

* Reusing certain documentation templates across several projects\.

* Using loops for generating parts of the documentation\.

* Referring to code identifiers\, such as class names or constants in the code\, in the documentation\. Re\-generating the documentation then always inserts the most recent values of those code identifiers\.

## 2\. Intended Use \/ Basic Idea
There is no special magic in using Jackadocs\. The general approach is to create a class with a `main` method\, which overwrites all the documentation when called\. Jackadocs is just a library that gives some support with generating the documentation programmatically\.

This documentation also shows some more ideas that are common amongst Jackadull projects\. They include a certain use of Maven for including the generation of documentation in the build cycle\.

Every developer can make up their own best practices\. This document can be seen as a source of inspiration\.

Source fragments that will be composed into documentation files are XML constants in the Scala code\. This XML is interpreted as HTML\. When writing documentation in Markdown format\, HTML will be converted to Markdown\. \(However\, the conversion process is not very smart\; don\'t expect miracles from it\. It will do just enough to translate simple documentation\, such as `README.md` files\.\)

[Here](https\:\/\/github\.com\/jackadull\/jackadocs\/blob\/release\/latest\/docs\/src\/main\/scala\/net\/jackadull\/jackadocs\/docs\/readme\/IntentedUse\.scala) is an example for the Scala source file from which this text is generated\.

In order to isolate documentation\-generating code from the actual module that is to be published\, it is advisable to keep the documentation generating code in a separate module \(or SBT project\)\. This module can be kept in a sub\-folder of the main module\, but there should be no explicit relationship between the main module and the documentation generating module \(such as the Maven parent\/child relationship\)\. In SBT\, the documentation module should have `dependsOn(LocalRootProject)` \, and the root project should have \`aggregates\(docs\)\`\.

The documentation generating module should never be published or deployed\. Its only purpose is to contain the utility code which gets called in order to \(re\-\)generate the main module\'s documentation\.

The recommended name for this module is `docs` \. An example can be found in the [`docs`](https\:\/\/github\.com\/jackadull\/jackadocs\/tree\/release\/latest\/docs) subfolder of the `jackadocs` project itself\.

Then\, every time before making a new release of the main module\, the `main` method of the `docs` is to be called\. Documentation will be re\-generated\. By adhering to this general workflow\, documentation will always be up\-to date\. This part can be automated using Maven or SBT\, as shown below\.

This is the basic idea\. As written initially\, there is no further magic behind Jackadocs\. The following chapters will share some further advice on how to handle certain things\.

## 3\. Usage Example
As described\, there is no special magic in using Jackadocs\. Because there are many ways to use the tools presented by the Jackadocs library\, the best introduction is an example\. The reader can then make up his or her own way of preference of using Jackadocs\.

The sub\-module and code that generates this text serves as the example\. It can be found in the [`docs`](https\:\/\/github\.com\/jackadull\/jackadocs\/tree\/release\/latest\/docs) subfolder of the `jackadocs` project\. In it\, you will find Scala souce code under `src/main/scala` that contains all the data for generating this documentation\.

### 3\.1\. How to Re\-Generate this Documentation
The documentation of Jackadocs \(i\.e\.\, the `README.md` in the `jackadocs/docs` root folder\) is \(re\-\)generated by calling `sbt jackadocsGenerate` \. This is effectively defined as\:

```bash
jackadocsGenerate := (runMain in Compile).toTask(s" ${projectInfo basePackage}.docs.Main .").value
```
SBT will then compile the project\, and run the main class of `docs` \. The main class \(i\.e\.\, the class that contains the `main` method\) is `net.jackadull.jackadocs.docs.Main` \. The command\-line argument for the execution is fixed as `.` \. This is the path to the project root folder\.

#### 3\.1\.1\. Automation in the Main Project
The process of re\-generating the documentation has been automated by aliasing `sbt build` to `;compile ;jackadocsGenerate` \.

The SBT file also contains another option\: When calling `sbt jackadocsVerify` \, then the option `-V` is added to the command\-line parameters of the `docs` main\. This does not re\-generate the documentaton\, but just verifies that the current version has all the latest information\. If there is any difference\, an exception is thrown\, effectively cancelling the build\. More on that further below\.

### 3\.2\. The Main Class
The source code of the main class can be found [here](https\:\/\/github\.com\/jackadull\/jackadocs\/blob\/release\/latest\/docs\/src\/main\/scala\/net\/jackadull\/jackadocs\/docs\/Main\.scala) \. As can be seen\, it inherits from `JackadocsMain` and `App` \. `JackadocsMain` is really not a requirement\; it can be extended optionally for some utility value\. Its source code is quite short\, so you may want to look it up\.

Here is what it does\:

* It creates a `Jackadocs` instance\, stored in the variable `jackadocs` \. This class contains the main functions used for generating the documentation\, and some other things\.

  The `Jackadocs` factory method receives the command\-line arguments\. The most important \(and required\) argument is the path to the project root\.

  The other optional argument is `-V` \. When specified\, files will not be overwritten\, but their contents will only be verified\. When `-V` is specified and one of the documentation files is not exactly as it _would_ be written\, the program fails\.

The `Main` object of Jackadocs\' `docs` also does the following\:

* Generate the `README.md` file\:

  ```scala
  jackadocs generateAt s"$projectDir/docs/README.md" markdownFor ReadmeRoot
  ```
  Tells Jackadocs to generate the Markdown for `ReadmeRoot` and write it into `README.md` \, relative to the project base directory that was passed in as a command\-line argument\.

  `ReadmeRoot` can be found [here](https\:\/\/github\.com\/jackadull\/jackadocs\/blob\/release\/latest\/docs\/src\/main\/scala\/net\/jackadull\/jackadocs\/docs\/ReadmeRoot\.scala) \. The contents of this object follow the chapter structure\, which gets described below\.

* Define the minimum set of methods required by `JackadocsMain` \:

  * ```scala
    def organizationName = "jackadull"
    ```
    This is the name of the organization\, as it usually appears in URLs of services like GitHub\, Travis\-CI etc\.

  * ```scala
    def projectDir = "."
    ```
    The path to the main project\, relative to the command\-line argument\.

  * ```scala
    def projectInfo = new JackadocsInfo { ... }
    ```
    Makes the Maven artifact data \(group\/artifact ID and version\) accessible to `JackadocsMain` \. This is used for a couple of features\.

    In this case\, the data is copied over dynamically from an auto\-generated build info file\. This is achieved using [this plugin](https\:\/\/github\.com\/sbt\/sbt\-buildinfo) \.

  * ```scala
    def sourceRepoProvider = "github"
    ```
    Used for GitHub readme badges that require the source repo provider as part of the URL\.

When you take a look at [`JackadocsMain`](https\:\/\/github\.com\/jackadull\/jackadocs\/blob\/release\/latest\/src\/main\/scala\/net\/jackadull\/jackadocs\/execution\/JackadocsMain\.scala) and its super type [`DocsMetaData`](https\:\/\/github\.com\/jackadull\/jackadocs\/blob\/release\/latest\/src\/main\/scala\/net\/jackadull\/jackadocs\/structure\/DocsMetaData\.scala) \, you will find many more properties with reasonable default implementations that can be overridden when necessary\.

## 4\. Chapter Structure
The contents of every Jackadocs\-based document are laid out in a hierarchy of chapters and sub\-chapters\. Chapters are Scala objects\. There are two major ways how to declare chapters in the code\:

1. As top\-level\, standalone Scala `object` types that extend the `Chapter` trait\. [The code for this very chapter](https\:\/\/github\.com\/jackadull\/jackadocs\/blob\/release\/latest\/docs\/src\/main\/scala\/net\/jackadull\/jackadocs\/docs\/readme\/Ch4\_ChapterStructure\.scala) is an example of this\.

2. As inline constants\, similar to case class instances\, within the code\. [The`UsageExample`object](https\:\/\/github\.com\/jackadull\/jackadocs\/blob\/release\/latest\/docs\/src\/main\/scala\/net\/jackadull\/jackadocs\/docs\/readme\/Ch3\_UsageExample\.scala) contains several examples\, which can be found when looking at its `subChapters` method\.

The root\-level object for every Jackadocs\-based documents is an instance of [`RootChapter`](https\:\/\/github\.com\/jackadull\/jackadocs\/blob\/release\/latest\/src\/main\/scala\/net\/jackadull\/jackadocs\/structure\/RootChapter\.scala) \. The title of the root chapter is the title of the document\. Its contents are the top\-level text of the document\. The sub\-chapters of the root\-level chapter are the chapters of the document\.

Of course\, the structure is recursive\. Chapters below `RootChapter` are instances of `Chapter` \, which can have sub\-chapters of their own\, and so on\. In this way\, a tree\-like chapter structure gets assembled\.

Looking at [the source code of`ReadmeRoot`](https\:\/\/github\.com\/jackadull\/jackadocs\/blob\/release\/latest\/docs\/src\/main\/scala\/net\/jackadull\/jackadocs\/docs\/ReadmeRoot\.scala) can be a helpful illustration of these abstract descriptions\.

### 4\.1\. TOC Options
Here are some optional features of `Chapter` instances\:

* Table of Contents\: For adding one\, simply define\:

  ```scala
  override def toc = true
  ```
  Adds a table of contents with links to sub\-chapters between the chapter title heading and its contents\.

* Contents before TOC\: Optional contents to be placed before the table of contents\. Used in this documentation for placing the GitHub readme badges before the main TOC\.

### 4\.2\. GitHub Readme Badges
An example can be seen at the top of this very document\. Those badges do not really have anything to do with GitHub itself\. It\'s only that they became popular on GitHub projects\.

Badges can supply more up\-to\-date information on a project\, such as the latest build status\, coverage etc\. They are usually simple images with links\. Some standard kinds of badges are defined in Jackadocs\. See an example by looking into `ReadmeRoot:`

```scala
override def contentsBeforeTOC(root:RootChapter):NodeSeq =
<p>{travisCIBadge} {mavenCentralBadge} {coverallsBadge} {codeFactorBadge} {snykBadge}</p>
```
This is enabled by adding the `BadgeGenerators` trait\, which requires only one further method to be defined\:

```scala
def docsMetaData:DocsMetaData = Main
```
Assigning the `Main` object is possible in this case because `Main` extends `JackadocsMain` \, which in turn extends `DocsMetaData` \. This is where all the badges get their URL components from\, and you are free to override all available properties if needed\, or leave them at their \(usually reasonable\) defaults\.

## 5\. Rendering Process
As explained before\, the command `jackadocs generateAt "$projectDir/docs/README.md" markdownFor ReadmeRoot` renders the Markdown version of the root chapter into the given file\.

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
The rendering process also prefixes chapter numbers before the titles\. The numbering strategy is passed into the process as an instance of the `ChapterNumbering` trait\, defined in the `RootChapter` \.

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

#### 5\.2\.1\. ChapterNumbering Sequencing
`ChapterNumbering` instances can be combined with each other\, so that different levels of chapter nesting are numbered differently\. For example\, the `RootChapter` of this documentation is defined as\:

```scala
override def chapterNumbering = ChapterNumbering(ChapterNumbering empty, ChapterNumbering decimal)
```
This defines that top\-level chapters will not be numbered \( `empty` \)\, while the level below that is numbered decimally \( `decimal` \)\. As there is only one top\-level chapter\, and it serves as title of the whole document\, it is isually advisable to use `empty` as top\-level numbering\.

The last defined chapter numbering level in the list gets repeated for every following level\. In this case\, all further nesting levels will be `decimal` \. As an example\, if only two levels of chapters should be numbered\, and everything at chapter level 3 and below should have no numbering\, this would look like this\:

```scala
ChapterNumbering(ChapterNumbering empty, ChapterNumbering decimal, ChapterNumbering decimal, ChapterNumbering empty)
```
Other types and conditions for chapter numbering are also possible\. For example\, appendices are usually numbered alphabetically\.

However\, neither the special case for appendices not alphabetic numbering are implemented in Jackadocs\. It would be easy to do though\. Alphabetic numbering can be done as a variant of the `decimal` implementation\. Special cases for appendices can be handled because the `ChapterNumbering` instance gets passed the chapter that is to be numbered\. So\, special chapter\-dependendent numbering cases can be implemented easily\.

### 5\.3\. Debug Markdown Tree Output
If anything goes bad with regards to Markdown rendering\, you can always print a debug tree of the Markdown view\. For example\:

```scala
RenderAsMarkdown(ReadmeRoot, ChapterNumbering.empty) foreach {md ⇒ println(md.treeStructure())}
```
`RenderAsMarkdown` returns a sequence of Markdown elements\. On each of those elements\, `treeStructure()` is called\. This returns a string representation of this part of the Markdown tree\, with all children\.

## 6\. Programming Considerations
### 6\.1\. Implicit String to NodeSeq Conversion
The `Chapter` trait contains an implicit conversion from `String` to `NodeSeq` \. It is automatically in scope for all `Chapter` sub\-types\.

This is especially useful for chapter titles\. They are of type `NodeSeq` \, but it is usually more comfortable to enter a simple string for the title\.

### 6\.2\. Chapter Type Naming
When defining `Chapter` instances as Scala `object` types\, the developer is free to choose a proper name\. Everyone should make up their own conventions here\, as suitable for the project at hand\.

In this documentation\, the developer found it useful to name the chapter objects like the chapter titles\, only in camel case\. Additionally\, the chapter number is prepended with a prefix\, for example `Ch3_ExampleChapter` \. This helps working with the code\, as the IDE will automatically display chapters in proper order\.

### 6\.3\. Further Remarks
* This project\, which is also used as an example\, uses SBT as build tool\. But the same techniques as described here should also work with other tools\, such as Maven or Gradle\. No SBT plugins are strictly necessary\, and all the procedures rely solely on standard JVM functionality\.

* When using a tool similar to SBT or Maven\, the version of the `docs` project can always remain a snapshot version\. It will never be released\.

* The implementation goes only as far as required for the author of Jackadocs\. If you are missing any feature\, consider requesting it politely\, or implementing it yourself\. You can offer a merge request\.

## 7\. Jackadull Recipe
All elements required for running Jackadocs as part of a project have been described in the previous chapters\. For tying things up\, here is a little list of bullet points that describe the common practice when used in Jackadull projects\. If you wish\, you can use it as a checklist or an inspiration for your own project\.

* In the `docs-generator` sub\-folder\:

  * Main class that extends `App` and `JackadocsMain`

  * `ReadmeRoot` as entry point for rendering\, extending `RootChapter`

  * Main class rendering the readme file\:

    ```scala
    jackadocs generateAt s"$projectDir/README.md" markdownFor ReadmeRoot
    ```
  * `docs-generator` POM\:

    * Version number obviously not made for releasing\, such as `0-SNAPSHOT`

    * Dependency on main project and Jackadocs

    * Configuration of `exec-maven-plugin` for calling the main class

    * When `jackadull-release` profile is active\, also pass `-V` to the main class

* In the main project\:

  * A template for the project runtime information type\, under `src/main/scala-templates` \, following [this example](https\:\/\/github\.com\/jackadull\/jackadocs\/blob\/release\/latest\/src\/main\/scala\-templates\/net\/jackadull\/jackadocs\/JackadocsInfo\.scala)

  * In the main project POM\:

    * Inclusion of `templating-maven-plugin` \, following the Jackadocs example

    * Usage of `exec-maven-plugin` for calling `mvn clean compile exec:java` in the `docs-generator` sub\-folder after the `install` phase of the main project

    * When the `jackadull-release` profile is active\, pass it on to `docs-generator` via `exec-maven-plugin`

When all of this is in place\, simply call `mvn install` in the main project folder for \(re\-\)generating the documentation\.

