package net.jackadull.jackadocs.docs

import net.jackadull.jackadocs.JackadocsInfo
import net.jackadull.jackadocs.execution.{JackadocsMain, JackadocsProjectInfo}

import scala.language.postfixOps

object Main extends App with JackadocsMain {
  jackadocs generateAt s"$projectDir/docs/README.md" markdownFor ReadmeRoot

  def organizationName = "jackadull"
  def projectDir = "."
  def projectInfo = new JackadocsProjectInfo {
    def groupID = JackadocsInfo organization
    def artifactID = JackadocsInfo name
    def version = JackadocsInfo version
  }
  def sourceRepoProvider = "github"
}
