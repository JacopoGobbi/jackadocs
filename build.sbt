import net.jackadull.build.{JackadullBuild, ProjectInfo}
import net.jackadull.build.dependencies.JackadullDependencies._

import scala.language.postfixOps

scalaVersion := JackadullBuild.scalaVersion

lazy val jackadull = JackadullBuild onTravis ProjectInfo(
  name = "jackadocs",
  version = "0.5.1-SNAPSHOT",
  basePackage = "net.jackadull.jackadocs",
  capitalizedIdentifier = "Jackadocs"
)

lazy val jackadocsBuild:Project = (project in file(".")).configure(jackadull project).aggregate(docs)
  .configure(jackadull dependencies (ScalaTest % Test, ScalaXML))

lazy val docs = (project in file("docs")).configure(jackadull docs)
  .settings(scalaVersion := JackadullBuild.scalaVersion)

addCommandAlias("build", jackadull buildCommand)
addCommandAlias("ci", jackadull ciCommand)
