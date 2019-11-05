package net.jackadull.jackadocs.structure.badges

import net.jackadull.jackadocs.structure.DocsMetaData

import scala.xml.NodeSeq

trait BadgeGenerator {
  def badge:NodeSeq = {
    val image = <img src={imageURL} alt={name} />
    linkURL match {
      case None => image
      case Some(href) => <a href={href}>{image}</a>
    }
  }

  def data:DocsMetaData
  def imageURL:String
  def linkURL:Option[String]
  def name:String
}
object BadgeGenerator {
  class CodeFactorBadgeGenerator(override val data:DocsMetaData) extends BadgeGenerator {
    import data._
    override def imageURL:String = s"https://www.codefactor.io/repository/$codeFactorRepoProvider/$codeFactorOrgName/$codeFactorRepoName/badge"
    override def linkURL:Option[String] = Some(s"https://www.codefactor.io/repository/$codeFactorRepoProvider/$codeFactorOrgName/$codeFactorRepoName")
    override def name:String = "Codefactor"
  }

  class CoverallsBadgeGenerator(override val data:DocsMetaData) extends BadgeGenerator {
    import data._
    override def imageURL:String = s"https://coveralls.io/repos/github/$coverallsOrgName/$coverallsRepoName/badge.svg"
    override def linkURL:Option[String] = Some(s"https://coveralls.io/github/$coverallsOrgName/$coverallsRepoName")
    override def name:String = "Coveralls"
  }

  class MavenCentralBadgeGenerator(override val data:DocsMetaData) extends BadgeGenerator {
    import data._
    override def imageURL:String = s"https://img.shields.io/maven-central/v/$mavenGroupID/$mavenArtifactID.svg"
    override def linkURL:Option[String] = Some("https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22" + mavenGroupID + "%22%20AND%20a%3A%22" + mavenArtifactID + "%22")
    override def name:String = "Maven Central"
  }

  class ScaladocBadgeGenerator(override val data:DocsMetaData) extends BadgeGenerator {
    import data._
    override def imageURL:String = s"http://javadoc-badge.appspot.com/$mavenGroupID/$mavenArtifactID.svg?label=scaladoc"
    override def linkURL:Option[String] = Some(s"http://javadoc-badge.appspot.com/$mavenGroupID/$mavenArtifactID")
    override def name:String = "Scaladoc"
  }

  class SnykBadgeGenerator(override val data:DocsMetaData) extends BadgeGenerator {
    import data._
    override def imageURL:String = s"https://snyk.io/test/$snykRepoProvider/$snykOrgName/$snykRepoName/badge.svg"
    override def linkURL:Option[String] = Some(s"https://snyk.io/test/$snykRepoProvider/$snykOrgName/$snykRepoName")
    override def name:String = "Snyk"
  }

  class TravisCIBadgeGenerator(override val data:DocsMetaData) extends BadgeGenerator {
    import data._
    override def imageURL:String = s"https://travis-ci.org/$travisCIOwnerName/$travisCIRepoName.svg"
    override def linkURL:Option[String] = Some(s"https://travis-ci.org/$travisCIOwnerName/$travisCIRepoName")
    override def name:String = "Travis CI"
  }
}
