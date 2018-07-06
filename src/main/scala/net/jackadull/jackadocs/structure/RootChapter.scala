package net.jackadull.jackadocs.structure

import scala.language.postfixOps

trait RootChapter extends Chapter {
  lazy val (idOfChapter,chapterOfID):(Map[Chapter,String],Map[String,Chapter]) = {
    var forward:Map[Chapter,String] = Map()
    var reverse:Map[String,Chapter] = Map()
    def idForTitle(chapter:Chapter):String = {
      val base = chapter titleAsIDBase
      var id = base
      var suffixNumber = 1
      while(reverse contains id) {id = s"$base-$suffixNumber"; suffixNumber += 1}
      id
    }
    def visit(chapter:Chapter) {
      val id = idForTitle(chapter)
      forward += (chapter → id); reverse += (id → chapter)
      chapter.subChapters foreach visit
    }
    visit(this)
    (forward, reverse)
  }
}
