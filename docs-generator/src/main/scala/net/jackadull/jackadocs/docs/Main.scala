package net.jackadull.jackadocs.docs

import net.jackadull.jackadocs.docs.readme.ReadmeRoot
import net.jackadull.jackadocs.rendering.{ChapterNumbering, RenderAsHTML}

import scala.language.postfixOps

object Main extends App {
  println(RenderAsHTML(ReadmeRoot, ChapterNumbering empty))
}
