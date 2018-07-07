package net.jackadull.jackadocs.rendering

import net.jackadull.jackadocs.rendering.markdown.{HTMLToMarkdown, _}
import net.jackadull.jackadocs.structure.{Chapter, RootChapter}

import scala.language.postfixOps

object RenderAsMarkdown {
  def apply(root:RootChapter, chapterNumbering:ChapterNumbering):Seq[MDBlock] = {

    def recurse(toRender:Seq[Chapter], cn:ChapterNumbering, depth:Int, soFar:Seq[MDBlock]):(Seq[MDBlock],ChapterNumbering) =
      toRender match {
        case Seq() ⇒ (soFar, cn)
        case Seq(chapter1, moreChapters@_*) ⇒
          val (chapterNumber, cn2) = cn count chapter1
          val chapterName:Seq[MDInline] =
            (if(chapterNumber nonEmpty) Seq(MDInlineText(s"$chapterNumber ")) else Seq()) ++ (HTMLToMarkdown inline  (chapter1 title))
          val (renderedSubChapters, cn3) = recurse(chapter1 subChapters, cn2 subChapters, depth+1, Vector())
          val maybeTOC = if(chapter1 toc) toc(chapter1, cn2) else Seq()
          val chapterLocal:Seq[MDBlock] =
            (((MDATXHeading(depth min 6, chapterName) +: HTMLToMarkdown(chapter1 contentsBeforeTOC root)) ++ maybeTOC) ++ HTMLToMarkdown(chapter1 contents root)) ++
              renderedSubChapters
          recurse(moreChapters, cn3 parent, depth, soFar ++ chapterLocal)
      }

    def toc(chapter:Chapter, cn:ChapterNumbering):Seq[MDBlock] = {
      def ofSubChapters(parent:Chapter, cn:ChapterNumbering):(Seq[MDBlock],ChapterNumbering) = parent subChapters match {
        case Seq() ⇒ (Seq(), cn)
        case _ ⇒
          val (bulletPoints:Seq[Seq[MDBlock]], cn2:ChapterNumbering) = (parent subChapters).foldLeft(Seq[Seq[MDBlock]]() → (cn subChapters)) {
            case ((acc, cn3), subChapter) ⇒
              val (chapterNumber, cn4) = cn3 count subChapter
              val chapterName:Seq[MDInline] =
                (if(chapterNumber nonEmpty) Seq(MDInlineText(s"$chapterNumber ")) else Seq()) ++ (HTMLToMarkdown inline (subChapter title))
              val (subTOC, cn5) = ofSubChapters(subChapter, cn4)
              val link = MDLink(chapterName, s"#${subChapter id root}", None)
              (acc ++ Seq(Seq(MDParagraph(Seq(link))) ++ subTOC), cn5)
          }
          (Seq(MDList(ordered = false, bulletPoints, tight = true)), cn2 parent)
      }
      ofSubChapters(chapter, cn) _1
    }

    recurse(Vector(root), chapterNumbering, 1, Vector()) _1
  }
}
