package net.jackadull.jackadocs

trait JackadocsInfo {
  def artifactID:String = "${project.artifactId}"
  def groupID:String = "${project.groupId}"
  def version:String = "${project.version}"
}
object JackadocsInfo extends JackadocsInfo
