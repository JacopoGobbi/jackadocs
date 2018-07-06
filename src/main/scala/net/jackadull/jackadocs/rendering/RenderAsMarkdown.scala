package net.jackadull.jackadocs.rendering

import net.jackadull.jackadocs.rendering.markdown.{HTMLToMarkdown, _}
import net.jackadull.jackadocs.structure.Chapter

import scala.language.postfixOps

object RenderAsMarkdown {
  def apply(chapter:Chapter, chapterNumbering:ChapterNumbering):Seq[MDBlock] = {
    def recurse(toRender:Seq[Chapter], cn:ChapterNumbering, depth:Int, soFar:Seq[MDBlock]):(Seq[MDBlock],ChapterNumbering) =
      toRender match {
        case Seq() ⇒ (soFar, cn)
        case Seq(chapter1, moreChapters@_*) ⇒
          val (chapterNumber, cn2) = cn count chapter1
          val chapterName:Seq[MDInline] =
            (if(chapterNumber nonEmpty) Seq(MDInlineText(s"$chapterNumber ")) else Seq()) ++ (HTMLToMarkdown inline  (chapter1 title))
          val (renderedSubChapters, cn3) = recurse(chapter1 subChapters, cn2 subChapters, depth+1, Vector())
          val chapterLocal:Seq[MDBlock] =
            (MDATXHeading(depth min 6, chapterName) +: HTMLToMarkdown(chapter1 contents)) ++
              renderedSubChapters
          recurse(moreChapters, cn3 parent, depth, soFar ++ chapterLocal)
      }
    recurse(Vector(chapter), chapterNumbering, 1, Vector()) _1
  }
}
