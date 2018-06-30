package net.jackadull.jackadocs.docs

import net.jackadull.jackadocs.JackadocsInfo
import net.jackadull.jackadocs.docs.readme.ReadmeRoot
import net.jackadull.jackadocs.execution.JackadocsMain

import scala.language.postfixOps

object Main extends App with JackadocsMain {
  jackadocs generateAt s"$projectDir/README.md" markdownFor ReadmeRoot

  def organizationName = "jackadull"
  def projectDir = ".."
  def projectInfo = JackadocsInfo
  def sourceRepoProvider = "github"
}
