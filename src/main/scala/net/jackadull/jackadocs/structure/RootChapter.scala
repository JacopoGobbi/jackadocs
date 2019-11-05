package net.jackadull.jackadocs.structure

import net.jackadull.jackadocs.rendering.ChapterNumbering

import scala.xml.NodeSeq

trait RootChapter extends Chapter {
  def chapterNumbering:ChapterNumbering = ChapterNumbering.empty

  lazy val (idOfChapter,chapterOfID,numberOfChapter):(Map[Chapter,String],Map[String,Chapter],Map[Chapter,String]) = {
    var forward:Map[Chapter,String] = Map()
    var numbers:Map[Chapter,String] = Map()
    var reverse:Map[String,Chapter] = Map()
    def idForTitle(chapter:Chapter, title:NodeSeq):String = {
      val base = title.text.toLowerCase.replaceAll("""[^\w\- ]""", "").replace(' ', '-')
      var id = base
      var suffixNumber = 1
      while(reverse contains id) {id = s"$base-$suffixNumber"; suffixNumber += 1}
      id
    }
    def visit(chapter:Chapter, cn:ChapterNumbering):ChapterNumbering = {
      val (number, cn2) = cn count chapter
      val titleWithNumber = if(number.isEmpty) chapter.title else <_>{number} {chapter.title}</_>.child
      val id = idForTitle(chapter, titleWithNumber)
      numbers += (chapter -> number)
      forward += (chapter -> id); reverse += (id -> chapter)
      chapter.subChapters.foldLeft(cn2.subChapters) {case (cn3, subChapter) => visit(subChapter, cn3)}.parent
    }
    visit(this, chapterNumbering)
    (forward, reverse, numbers)
  }
}
