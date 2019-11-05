package net.jackadull.jackadocs.rendering

import net.jackadull.jackadocs.rendering.markdown.{HTMLToMarkdown, _}
import net.jackadull.jackadocs.structure.{Chapter, RootChapter}

object RenderAsMarkdown {
  def apply(root:RootChapter):Seq[MDBlock] = {

    def recurse(toRender:Seq[Chapter], depth:Int, soFar:Seq[MDBlock]):Seq[MDBlock] =
      toRender match {
        case Seq() => soFar
        case Seq(chapter1, moreChapters@_*) =>
          val chapterName:Seq[MDInline] = HTMLToMarkdown inline (chapter1 titleWithNumber root)
          val renderedSubChapters = recurse(chapter1.subChapters, depth+1, Vector())
          val maybeTOC = if(chapter1.toc) toc(chapter1) else Seq()
          val chapterLocal:Seq[MDBlock] =
            (((MDATXHeading(depth min 6, chapterName) +: HTMLToMarkdown(chapter1 contentsBeforeTOC root)) ++ maybeTOC) ++ HTMLToMarkdown(chapter1 contents root)) ++
              renderedSubChapters
          recurse(moreChapters, depth, soFar ++ chapterLocal)
      }

    def toc(chapter:Chapter):Seq[MDBlock] = {
      def ofSubChapters(parent:Chapter):Seq[MDBlock] = parent.subChapters match {
        case Seq() => Seq()
        case _ =>
          val bulletPoints:Seq[Seq[MDBlock]] = parent.subChapters.foldLeft(Seq[Seq[MDBlock]]()) {
            (acc, subChapter) =>
              val chapterName:Seq[MDInline] = HTMLToMarkdown inline (subChapter titleWithNumber root)
              val subTOC = ofSubChapters(subChapter)
              val link = MDLink(chapterName, s"#${subChapter id root}", None)
              acc ++ Seq(Seq(MDParagraph(Seq(link))) ++ subTOC)
          }
          Seq(MDList(ordered = false, bulletPoints, tight = true))
      }
      ofSubChapters(chapter)
    }

    recurse(Vector(root), 1, Vector())
  }
}
