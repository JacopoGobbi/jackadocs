package net.jackadull.jackadocs.execution

import net.jackadull.jackadocs.structure.DocsMetaData

import scala.language.postfixOps

trait JackadocsMain extends DocsMetaData {
  def projectDir:String
  def projectInfo:JackadocsProjectInfo

  protected def args:Array[String]
  lazy val jackadocs:Jackadocs = {
    val jd = Jackadocs fromArgs args
    jd.requirePOMVersion(s"$projectDir/pom.xml")(projectInfo version)
    jd
  }

  def mavenArtifactID:String = projectInfo artifactID
  def mavenGroupID:String = projectInfo groupID
  def repoName:String = mavenArtifactID.replaceFirst("""_\d+\.\d+$""", "")
}
