package net.jackadull.jackadocs.rendering

import net.jackadull.jackadocs.structure.Chapter

import scala.language.postfixOps
import scala.xml.NodeSeq

object RenderAsHTML {
  def apply(chapter:Chapter, chapterNumbering:ChapterNumbering):NodeSeq = {
    def recurse(toRender:Seq[Chapter], cn:ChapterNumbering, depth:Int, soFar:NodeSeq):(NodeSeq,ChapterNumbering) =
      toRender match {
        case Seq() ⇒ (soFar, cn)
        case Seq(chapter1, moreChapters@_*) ⇒
          val (chapterNumber, cn2) = cn count chapter1
          val (renderedSubChapters, cn3) = recurse(chapter1 subChapters, cn2 subChapters, depth+1, NodeSeq.Empty)
          val chapterLocal =
<section class="chapter" id={s"chapter_${chapter1 id}"}>
<a name={s"chapter_${chapter id}"} />
{<h1>{if(chapterNumber nonEmpty) chapterNumber+" " else ""}{chapter title}</h1>.copy(label = s"h$depth")}
{chapter contents}
{renderedSubChapters}
</section>
          recurse(moreChapters, cn3 parent, depth, soFar ++ chapterLocal)
      }
    recurse(Seq(chapter), chapterNumbering, 1, NodeSeq.Empty) _1
  }
}
