package net.jackadull.jackadocs.docs

import net.jackadull.jackadocs.JackadocsInfo
import net.jackadull.jackadocs.docs.readme.ReadmeRoot
import net.jackadull.jackadocs.execution.Jackadocs
import net.jackadull.jackadocs.rendering.{ChapterNumbering, RenderAsMarkdown}

import scala.language.postfixOps

object Main extends App {
  val jackadocs = Jackadocs fromArgs args

  jackadocs.requirePOMVersion("../pom.xml")(JackadocsInfo Version)

  jackadocs generateAt "../README.md" markdownFor ReadmeRoot

  RenderAsMarkdown(ReadmeRoot, ChapterNumbering.empty) foreach {md ⇒ println(md.treeStructure())}
}
