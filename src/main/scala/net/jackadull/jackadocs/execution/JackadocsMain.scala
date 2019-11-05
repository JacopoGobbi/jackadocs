package net.jackadull.jackadocs.execution

import net.jackadull.jackadocs.structure.DocsMetaData

trait JackadocsMain extends DocsMetaData {
  def projectDir:String
  def projectInfo:JackadocsProjectInfo

  protected def args:Array[String]
  lazy val jackadocs:Jackadocs = Jackadocs.fromArgs(args.toIndexedSeq)

  override def mavenArtifactID:String = projectInfo.artifactID
  override def mavenGroupID:String = projectInfo.groupID
  override def repoName:String = mavenArtifactID.replaceFirst("""_\d+\.\d+$""", "")
}
