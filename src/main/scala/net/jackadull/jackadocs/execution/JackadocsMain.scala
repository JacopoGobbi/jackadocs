package net.jackadull.jackadocs.execution

import net.jackadull.jackadocs.structure.DocsMetaData

import scala.language.postfixOps

trait JackadocsMain extends DocsMetaData {
  def projectDir:String
  def projectInfo:JackadocsProjectInfo

  protected def args:Array[String]
  lazy val jackadocs:Jackadocs = Jackadocs fromArgs args

  def mavenArtifactID:String = projectInfo artifactID
  def mavenGroupID:String = projectInfo groupID
  def repoName:String = mavenArtifactID.replaceFirst("""_\d+\.\d+$""", "")
}
