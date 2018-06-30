package net.jackadull.jackadocs.structure.badges

import net.jackadull.jackadocs.structure.DocsMetaData
import net.jackadull.jackadocs.structure.badges.BadgeGenerator._

import scala.language.postfixOps
import scala.xml.NodeSeq

trait BadgeGenerators {
  def data:DocsMetaData

  def codeFactorBadge:NodeSeq = new CodeFactorBadgeGenerator(data) badge
  def coverallsBadge:NodeSeq = new CoverallsBadgeGenerator(data) badge
  def mavenCentralBadge:NodeSeq = new MavenCentralBadgeGenerator(data) badge
  def snykBadge:NodeSeq = new SnykBadgeGenerator(data) badge
  def travisCIBadge:NodeSeq = new TravisCIBadgeGenerator(data) badge
}
