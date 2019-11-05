package net.jackadull.jackadocs.rendering

import net.jackadull.jackadocs.structure.{Chapter, RootChapter}

import scala.xml.NodeSeq

object RenderAsHTML {
  def apply(root:RootChapter):NodeSeq = {

    def recurse(toRender:Seq[Chapter], depth:Int, soFar:NodeSeq):NodeSeq =
      toRender match {
        case Seq() => soFar
        case Seq(chapter, moreChapters@_*) =>
          val chapterLocal =
<section class="chapter" id={s"chapter_${chapter id root}"}>
<a name={chapter id root} />
{chapterContents(chapter, depth)}
{recurse(chapter.subChapters, depth + 1, NodeSeq.Empty)}
</section>
          recurse(moreChapters, depth, soFar ++ chapterLocal)
    }

    def chapterContents(chapter:Chapter, depth:Int):NodeSeq = {
<_>{<h1>{chapter titleWithNumber root}</h1>.copy(label = s"h$depth")}
{chapter contentsBeforeTOC root}
{if(chapter.toc) toc(chapter) else NodeSeq.Empty}
{chapter contents root}</_>.child
    }

    def toc(chapter:Chapter):NodeSeq = chapter.subChapters match {
      case Seq() => NodeSeq.Empty
      case subChapters =>
        def visit(chapter:Chapter):NodeSeq = {
          val link = <li><a href={s"#${chapter id root}"}>{chapter titleWithNumber root}</a></li>
          val otherLinks = subChapters.foldLeft(NodeSeq.Empty) {(acc, subChapter) => acc ++ visit(subChapter)}
          if(otherLinks.isEmpty) link else link ++ <ul>{otherLinks}</ul>
        }
        <ul>{chapter.subChapters.foldLeft(NodeSeq.Empty) {(acc, subChapter) => acc ++ visit(subChapter)}}</ul>
    }

    recurse(Seq(root), 1, NodeSeq.Empty)
  }
}
