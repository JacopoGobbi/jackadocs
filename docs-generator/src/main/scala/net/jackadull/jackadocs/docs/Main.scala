package net.jackadull.jackadocs.docs

import net.jackadull.jackadocs.JackadocsInfo
import net.jackadull.jackadocs.docs.readme.ReadmeRoot
import net.jackadull.jackadocs.execution.Jackadocs
import net.jackadull.jackadocs.structure.DocsMetaData

import scala.language.postfixOps

object Main extends App with DocsMetaData {
  val jackadocs = Jackadocs fromArgs args

  jackadocs.requirePOMVersion("../pom.xml")(JackadocsInfo Version)

  jackadocs generateAt "../README.md" markdownFor ReadmeRoot

  def mavenArtifactID = "jackadocs_2.12"
  def mavenGroupID = "net.jackadull"
  def organizationName = "jackadull"
  def repoName = "jackadocs"
  def sourceRepoProvider = "github"
}
