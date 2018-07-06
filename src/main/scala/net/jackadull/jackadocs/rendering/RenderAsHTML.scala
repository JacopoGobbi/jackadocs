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
          val (chapterNumber, cn2) = cn count chapter
          val (renderedSubChapters, cn3) = recurse(chapter subChapters, cn2 subChapters, depth+1, NodeSeq.Empty)
          val chapterLocal =
<section class="chapter" id={s"chapter_${chapter id root}"}>
<a name={chapter id root} />
{<h1>{if(chapterNumber nonEmpty) chapterNumber+" " else ""}{chapter title}</h1>.copy(label = s"h$depth")}
{chapter contents root}
{renderedSubChapters}
</section>
          recurse(moreChapters, cn3 parent, depth, soFar ++ chapterLocal)
      }
    recurse(Seq(root), chapterNumbering, 1, NodeSeq.Empty) _1
  }
}
