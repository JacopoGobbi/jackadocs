package net.jackadull.jackadocs.structure.badges

import net.jackadull.jackadocs.structure.DocsMetaData

import scala.xml.NodeSeq

trait BadgeGenerator {
  def badge:NodeSeq = {
    val image = <img src={imageURL} alt={name} />
    linkURL match {
      case None ⇒ image
      case Some(href) ⇒ <a href={href}>{image}</a>
    }
  }

  def data:DocsMetaData
  def imageURL:String
  def linkURL:Option[String]
  def name:String
}
object BadgeGenerator {
  class CodeFactorBadgeGenerator(val data:DocsMetaData) extends BadgeGenerator {
    import data._
    def imageURL:String = s"https://www.codefactor.io/repository/$codeFactorRepoProvider/$codeFactorOrgName/$codeFactorRepoName/badge"
    def linkURL:Option[String] = Some(s"https://www.codefactor.io/repository/$codeFactorRepoProvider/$codeFactorOrgName/$codeFactorRepoName")
    def name:String = "Codefactor"
  }

  class CoverallsBadgeGenerator(val data:DocsMetaData) extends BadgeGenerator {
    import data._
    def imageURL:String = s"https://coveralls.io/repos/github/$coverallsOrgName/$coverallsRepoName/badge.svg"
    def linkURL:Option[String] = Some(s"https://coveralls.io/github/$coverallsOrgName/$coverallsRepoName")
    def name:String = "Coveralls"
  }

  class MavenCentralBadgeGenerator(val data:DocsMetaData) extends BadgeGenerator {
    import data._
    def imageURL:String = s"https://img.shields.io/maven-central/v/$mavenGroupID/$mavenArtifactID.svg"
    def linkURL:Option[String] = Some("https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22" + mavenGroupID + "%22%20AND%20a%3A%22" + mavenArtifactID + "%22")
    def name:String = "Maven Central"
  }

  class SnykBadgeGenerator(val data:DocsMetaData) extends BadgeGenerator {
    import data._
    def imageURL:String = s"https://snyk.io/test/$snykRepoProvider/$snykOrgName/$snykRepoName/badge.svg"
    def linkURL:Option[String] = Some(s"https://snyk.io/test/$snykRepoProvider/$snykOrgName/$snykRepoName")
    def name:String = "Snyk"
  }

  class TravisCIBadgeGenerator(val data:DocsMetaData) extends BadgeGenerator {
    import data._
    def imageURL:String = s"https://travis-ci.org/$travisCIOwnerName/$travisCIRepoName.svg"
    def linkURL:Option[String] = Some(s"https://travis-ci.org/$travisCIOwnerName/$travisCIRepoName")
    def name:String = "Travis CI"
  }
}
