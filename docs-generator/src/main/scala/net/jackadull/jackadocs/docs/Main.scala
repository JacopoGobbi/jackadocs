package net.jackadull.jackadocs.docs

import java.io.{FileOutputStream, OutputStreamWriter}
import java.nio.charset.StandardCharsets.UTF_8

import net.jackadull.jackadocs.docs.readme.ReadmeRoot
import net.jackadull.jackadocs.rendering.markdown.MDWrite
import net.jackadull.jackadocs.rendering.{ChapterNumbering, RenderAsMarkdown}

import scala.language.postfixOps

object Main extends App {
  val out = new OutputStreamWriter(new FileOutputStream("README.md"), UTF_8)
  try {
    MDWrite(RenderAsMarkdown(ReadmeRoot, ChapterNumbering empty), out)
  } finally {out close()}
}
