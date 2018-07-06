package net.jackadull.jackadocs.docs

import net.jackadull.jackadocs.JackadocsInfo
import net.jackadull.jackadocs.execution.{JackadocsMain, JackadocsProjectInfo}

import scala.language.postfixOps

object Main extends App with JackadocsMain {
  jackadocs generateAt s"$projectDir/README.md" markdownFor ReadmeRoot

  def organizationName = "jackadull"
  def projectDir = ".."
  def projectInfo = new JackadocsInfo with JackadocsProjectInfo
  def sourceRepoProvider = "github"
}
