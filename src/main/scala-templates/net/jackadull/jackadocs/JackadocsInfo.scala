package net.jackadull.jackadocs

import net.jackadull.jackadocs.execution.JackadocsProjectInfo

object JackadocsInfo extends JackadocsProjectInfo {
  def artifactID:String = "${project.artifactId}"
  def groupID:String = "${project.groupId}"
  def version:String = "${project.version}"
}
