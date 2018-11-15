scalaVersion := "2.12.7"

import net.jackadull.build.{JackadullBuild, ProjectInfo}
import net.jackadull.build.dependencies.JackadullDependencies._

import scala.language.postfixOps

lazy val jackadull = JackadullBuild onTravis ProjectInfo(
  name = "jackadocs",
  version = "0.4.1",
  basePackage = "net.jackadull.jackadocs",
  capitalizedIdentifier = "Jackadocs"
)

lazy val jackadocsBuild:Project = (project in file(".")).configure(jackadull project).aggregate(docs)
  .configure(jackadull dependencies (ScalaTest % Test, ScalaXML))

lazy val docs = (project in file("docs")).configure(jackadull docs).dependsOn(LocalRootProject)

addCommandAlias("build", jackadull buildCommand)
addCommandAlias("ci", jackadull ciCommand)
