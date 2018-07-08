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
  def apply(cns:ChapterNumbering*):ChapterNumbering = {
    def recurse(s:Seq[ChapterNumbering]):ChapterNumbering = s match {
      case Seq() ⇒ empty
      case Seq(singleton) ⇒ singleton
      case Seq(first, rest@_*) ⇒ CompositeChapterNumbering(first, recurse(rest))
    }
    recurse(cns)
  }

  val decimal:ChapterNumbering = DecimalChapterNumbering()
  val empty:ChapterNumbering = EmptyChapterNumbering

  private final case class DecimalChapterNumbering(chapterCounter:Int=1, parentOpt:Option[ChapterNumbering]=None) extends ChapterNumbering {
    def currentNumber = (parentOpt map {_ currentNumber} getOrElse "") + chapterCounter + "."
    def count(chapter:Chapter) = (currentNumber, copy(chapterCounter=chapterCounter+1))
    def parent = parentOpt getOrElse this
    def subChapters = DecimalChapterNumbering(parentOpt = Some(copy(chapterCounter=chapterCounter-1)))
  }

  private object EmptyChapterNumbering extends ChapterNumbering {
    def currentNumber = ""
    def count(chapter:Chapter) = ("", this)
    def parent = this
    def subChapters = this
  }

  private final case class CompositeChapterNumbering(thisLevel:ChapterNumbering, childLevel:ChapterNumbering) extends ChapterNumbering {
    def currentNumber = thisLevel currentNumber
    def count(chapter:Chapter) = {
      val (number, next) = thisLevel count chapter
      (number, copy(thisLevel=next))
    }
    def parent = this
    def subChapters = ChapterNumberWithParent(childLevel, this)
  }

  private final case class ChapterNumberWithParent(thisLevel:ChapterNumbering, parent:ChapterNumbering) extends ChapterNumbering {
    def currentNumber = thisLevel currentNumber
    def count(chapter:Chapter) = {
      val (number, next) = thisLevel count chapter
      (number, copy(thisLevel=next))
    }
    def subChapters = ChapterNumberWithParent(thisLevel subChapters, this)
  }
}
