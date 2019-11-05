package net.jackadull.jackadocs.structure.badges

import net.jackadull.jackadocs.structure.DocsMetaData
import net.jackadull.jackadocs.structure.badges.BadgeGenerator._

import scala.xml.NodeSeq

trait BadgeGenerators {
  def docsMetaData:DocsMetaData

  def codeFactorBadge:NodeSeq = new CodeFactorBadgeGenerator(docsMetaData).badge
  def coverallsBadge:NodeSeq = new CoverallsBadgeGenerator(docsMetaData).badge
  def mavenCentralBadge:NodeSeq = new MavenCentralBadgeGenerator(docsMetaData).badge
  def scaladocBadge:NodeSeq = new ScaladocBadgeGenerator(docsMetaData).badge
  def snykBadge:NodeSeq = new SnykBadgeGenerator(docsMetaData).badge
  def travisCIBadge:NodeSeq = new TravisCIBadgeGenerator(docsMetaData).badge
}
