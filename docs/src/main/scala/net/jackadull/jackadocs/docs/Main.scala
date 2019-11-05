package net.jackadull.jackadocs.docs

import net.jackadull.jackadocs.JackadocsInfo
import net.jackadull.jackadocs.execution.{JackadocsMain, JackadocsProjectInfo}

object Main extends App with JackadocsMain {
  jackadocs generateAt s"$projectDir/docs/README.md" markdownFor ReadmeRoot

  override def organizationName:String = "jackadull"
  override def projectDir:String = "."
  override def projectInfo:JackadocsProjectInfo = new JackadocsProjectInfo {
    override def groupID:String = JackadocsInfo.organization
    override def artifactID:String = JackadocsInfo.name
    override def version:String = JackadocsInfo.version
  }
  override def sourceRepoProvider:String = "github"
}
