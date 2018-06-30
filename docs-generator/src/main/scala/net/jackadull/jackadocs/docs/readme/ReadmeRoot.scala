package net.jackadull.jackadocs.docs.readme

import net.jackadull.jackadocs.structure.Chapter

import scala.xml.NodeSeq

object ReadmeRoot extends Chapter {
  def id = "readme_begin"
  def title = "Jackadocs"

  def contents:NodeSeq =
<p>
  <a href={s"https://travis-ci.org/$orgName/$projectName"}><img src={shield(s"travis/$orgName/$projectName")} alt="Travis" /></a>
  <img src={shield(s"maven-central/v/$organization/$artifactID")} alt="Maven Central" />
  <a href={s"https://coveralls.io/github/$orgName/$projectName"}><img src={shield(s"coveralls/github/$orgName/$projectName")} alt="Coveralls" /></a>
  <a href={s"https://www.codefactor.io/repository/github/$orgName/$projectName"}><img src={s"https://www.codefactor.io/repository/github/$orgName/$projectName/badge?style=$shieldStyle"} alt="Codefactor" /></a>
  <a href={s"https://snyk.io/test/github/$orgName/$projectName"}><img src={s"https://snyk.io/test/github/$orgName/$projectName/badge.svg?style=$shieldStyle"} alt="Snyk" /></a>
</p>
<p>
  Tool library for automated generation of tool documentation.
  Can be used for creating <tt>README.md</tt> files, but also for documentation books, with multiple files, in either HTML or Github-Flavored Markdown.
</p>

  override def subChapters:Seq[Chapter] = Seq(Motivation, IntentedUse, UsageExample, ChapterStructure,
    RenderingProcess, FurtherRemarks, Roadmap)

  private def shield(path:String):String = s"$shieldsURL/$path.svg?style=$shieldStyle"
  private def shieldStyle = "plastic"
  private def shieldsURL = "https://img.shields.io"
  private def orgName = "jackadull"
  private def projectName = "jackadocs"
  private def organization = "net.jackadull"
  private def artifactID = s"${projectName}_2.12"
}
