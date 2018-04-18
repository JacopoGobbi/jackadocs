# jackadocs
Tool library for automated generation of tool documentation\. Can be used for creating `README.md` files\, but also for documentation books\, with multiple files\, in either HTML or Github\-Flavored Markdown\.

## Motivation
 `jackadocs` is useful for cases in which generation of documentation files should be partially dynamic\. That is\, the documentation contains parts that call Scala code for computing a part of the text\.

When writing and manually maintaining static Markdown or HTML files is sufficient for a certain project\, `jackadocs` is not needed\.

Examples for meaningful usecases of `jackadocs` include\:

* Using the Maven resources plugin in order to include Maven properties in the documentation\, such as library versions\.

* Reusing certain documentation templates across several projects\.

* Using loops for generating parts of the documentation\.

* Referring to code identifiers\, such as class names or constants in the code\, in the documentation\. Re\-generating the documentation then always inserts the most recent values of those code identifiers\.

## Intended Use
There is no special magic in using `jackadocs` \. The general approach is to create a class with a `main` method\, which overwrites all the documentation when called\. `jackadocs` is just a library that gives some support with generating the documentation programmatically\.

The text fragments that will be composed into the documentation files are XML constants in the Scala code\. This XML is interpreted as HTML\. When writing documentation in Markdown format\, the HTML will be converted to Markdown\. \(However\, the conversion process is not very smart\; don\'t expect miracles from it\. It will do just enough to translate simple documentation\, such as `README.md` files\.\)

In order to isolate documentation\-generating code from the actual module that is to be published\, it is advisable to keep the documentation generating code in a separate module\. This module can be kept in a sub\-folder of the main module\, but there should be no explicit relationship between the main module and the documentation generating module \(such as the Maven parent\/child relationship\)\. In fact\, the documentation generating module should never be published or deployed\. Its only purpose is to contain the utility code which gets called in order to \(re\-\)generate the main module\'s documentation\.

The recommended name for this module is `docs-generator` \. An example can be found in the [`docs-generator`](https\:\/\/github\.com\/jackadull\/jackadocs\/tree\/master\/docs\-generator) subfolder of the `jackadocs` project itself\.

Then\, every time before making a new release of the main module\, the `main` method of the `docs-generator` is to be called\. Documentation will be re\-generated\. By adhering to this general workflow\, documentation will always be up\-to date\.

This is the basic idea\. As written initially\, there is no further magic behind `jackadocs` \. The following chapters will share some further advice on how to handle certain things\.

