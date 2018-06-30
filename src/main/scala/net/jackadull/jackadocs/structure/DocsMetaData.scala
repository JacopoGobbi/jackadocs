package net.jackadull.jackadocs.structure

/** Provides information about the project to other components. May be implemented by the main class. */
trait DocsMetaData {
  def mavenArtifactID:String
  def mavenGroupID:String
  def organizationName:String
  def repoName:String
  def sourceRepoProvider:String

  def codeFactorOrgName:String = organizationName
  def codeFactorRepoName:String = repoName
  def codeFactorRepoProvider:String = sourceRepoProvider
  def coverallsOrgName:String = organizationName
  def coverallsRepoName:String = repoName
  def snykOrgName:String = organizationName
  def snykRepoName:String = repoName
  def snykRepoProvider:String = sourceRepoProvider
  def travisCIOwnerName:String = organizationName
  def travisCIRepoName:String = repoName
}
