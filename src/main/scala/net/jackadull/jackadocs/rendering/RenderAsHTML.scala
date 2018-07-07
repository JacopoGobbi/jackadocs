package net.jackadull.jackadocs.rendering

import net.jackadull.jackadocs.structure.{Chapter, RootChapter}

import scala.language.postfixOps
import scala.xml.NodeSeq

object RenderAsHTML {
  def apply(root:RootChapter, chapterNumbering:ChapterNumbering):NodeSeq = {

    def recurse(toRender:Seq[Chapter], cn:ChapterNumbering, depth:Int, soFar:NodeSeq):(NodeSeq,ChapterNumbering) =
      toRender match {
        case Seq() ⇒ (soFar, cn)
        case Seq(chapter, moreChapters@_*) ⇒
          val (contents, cn2) = chapterContents(chapter, cn, depth)
          val (renderedSubChapters, cn3) = recurse(chapter subChapters, cn2 subChapters, depth+1, NodeSeq.Empty)
          val chapterLocal =
<section class="chapter" id={s"chapter_${chapter id root}"}>
<a name={chapter id root} />
{contents}
{renderedSubChapters}
</section>
          recurse(moreChapters, cn3 parent, depth, soFar ++ chapterLocal)
    }

    def chapterContents(chapter:Chapter, cn:ChapterNumbering, depth:Int):(NodeSeq,ChapterNumbering) = {
      val (chapterNumber, cn2) = cn count chapter
(<_>{<h1>{if(chapterNumber nonEmpty) chapterNumber+" " else ""}{chapter title}</h1>.copy(label = s"h$depth")}
{chapter contentsBeforeTOC root}
{if(chapter toc) toc(chapter, cn) else NodeSeq.Empty}
{chapter contents root}</_> child, cn2)
    }

    def toc(chapter:Chapter, cn:ChapterNumbering):NodeSeq = chapter subChapters match {
      case Seq() ⇒ NodeSeq.Empty
      case _ ⇒
        def visit(chapter:Chapter, cn:ChapterNumbering):(NodeSeq,ChapterNumbering) = {
          val (chapterNumber, cn2) = cn count chapter
          val link = <li><a href={s"#${chapter id root}"}>{if(chapterNumber nonEmpty) chapterNumber+" " else ""}{chapter title}</a></li>
          val (otherLinks, cn3) = (chapter subChapters).foldLeft(NodeSeq.Empty → (cn2 subChapters)) {
            case ((acc, cn4), subChapter) ⇒
              val (acc2, cn5) = visit(subChapter, cn4)
              (acc ++ acc2, cn5)
          }
          if(otherLinks isEmpty) (link, cn3 parent) else (link ++ <ul>{otherLinks}</ul>, cn3 parent)
        }
        val tocContents = (chapter subChapters).foldLeft(NodeSeq.Empty → (cn subChapters)) {
          case ((acc, cn2), subChapter) ⇒
            val (acc2, cn3) = visit(subChapter, cn2)
            (acc ++ acc2, cn3)
        }._1
        <ul>{tocContents}</ul>
    }

    recurse(Seq(root), chapterNumbering, 1, NodeSeq.Empty) _1
  }
}
