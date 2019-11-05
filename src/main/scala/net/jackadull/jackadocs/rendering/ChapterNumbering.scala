package net.jackadull.jackadocs.rendering

import net.jackadull.jackadocs.structure.Chapter

trait ChapterNumbering {
  def currentNumber:String
  def count(chapter:Chapter):(String,ChapterNumbering)
  def parent:ChapterNumbering
  def subChapters:ChapterNumbering
}
object ChapterNumbering {
  def apply(cns:ChapterNumbering*):ChapterNumbering = {
    def recurse(s:Seq[ChapterNumbering]):ChapterNumbering = s match {
      case Seq() => empty
      case Seq(singleton) => singleton
      case Seq(first, rest@_*) => CompositeChapterNumbering(first, recurse(rest))
    }
    recurse(cns)
  }

  val decimal:ChapterNumbering = DecimalChapterNumbering()
  val empty:ChapterNumbering = EmptyChapterNumbering

  private final case class DecimalChapterNumbering(chapterCounter:Int=1, parentOpt:Option[ChapterNumbering]=None) extends ChapterNumbering {
    override def currentNumber:String = (parentOpt map {_.currentNumber} getOrElse "") + chapterCounter + "."
    override def count(chapter:Chapter):(String, DecimalChapterNumbering) = (currentNumber, copy(chapterCounter=chapterCounter+1))
    override def parent:ChapterNumbering = parentOpt getOrElse this
    override def subChapters:DecimalChapterNumbering = DecimalChapterNumbering(parentOpt = Some(copy(chapterCounter=chapterCounter-1)))
  }

  private object EmptyChapterNumbering extends ChapterNumbering {
    override def currentNumber:String = ""
    override def count(chapter:Chapter):(String,EmptyChapterNumbering.type) = ("", this)
    override def parent:EmptyChapterNumbering.type = this
    override def subChapters:EmptyChapterNumbering.type = this
  }

  private final case class CompositeChapterNumbering(thisLevel:ChapterNumbering, childLevel:ChapterNumbering) extends ChapterNumbering {
    override def currentNumber:String = thisLevel.currentNumber
    override def count(chapter:Chapter):(String,CompositeChapterNumbering) = {
      val (number, next) = thisLevel count chapter
      (number, copy(thisLevel=next))
    }
    override def parent:CompositeChapterNumbering = this
    override def subChapters = ChapterNumberWithParent(childLevel, this)
  }

  private final case class ChapterNumberWithParent(thisLevel:ChapterNumbering, override val parent:ChapterNumbering) extends ChapterNumbering {
    override def currentNumber:String = thisLevel.currentNumber
    override def count(chapter:Chapter):(String,ChapterNumberWithParent) = {
      val (number, next) = thisLevel count chapter
      (number, copy(thisLevel=next))
    }
    override def subChapters:ChapterNumberWithParent = ChapterNumberWithParent(thisLevel.subChapters, this)
  }
}
