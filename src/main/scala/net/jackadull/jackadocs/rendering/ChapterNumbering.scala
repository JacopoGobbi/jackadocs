package net.jackadull.jackadocs.rendering

import net.jackadull.jackadocs.structure.Chapter

import scala.language.postfixOps

trait ChapterNumbering {
  def currentNumber:String
  def count(chapter:Chapter):(String,ChapterNumbering)
  def parent:ChapterNumbering
  def subChapters:ChapterNumbering
}
object ChapterNumbering {
  val decimal:ChapterNumbering = DecimalChapterNumbering()
  val empty:ChapterNumbering = EmptyChapterNumbering

  private final case class DecimalChapterNumbering(chapterCounter:Int=1, parentOpt:Option[ChapterNumbering]=None) extends ChapterNumbering {
    def currentNumber = (parentOpt map {_ currentNumber} getOrElse "") + chapterCounter + "."
    def count(chapter:Chapter) = (currentNumber, copy(chapterCounter=chapterCounter+1))
    def parent = parentOpt getOrElse this
    def subChapters = DecimalChapterNumbering(parentOpt = Some(this))
  }

  private object EmptyChapterNumbering extends ChapterNumbering {
    def currentNumber = ""
    def count(chapter:Chapter) = ("", this)
    def parent = this
    def subChapters = this
  }
}
