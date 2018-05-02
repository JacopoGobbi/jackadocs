package net.jackadull.jackadocs.rendering.markdown

import scala.annotation.tailrec
import scala.language.postfixOps

object MDWrite {
  def apply(blocks:Traversable[MDBlock], out:Appendable) {
    def renderBlock(block:MDBlock, writer:LineWriter):LineWriter = block match {
      case MDATXHeading(level, contents) ⇒
        renderInlines(contents, writer pushLinePrefix ("#"*level+" ", " "*(level+1)) withoutLeadingWhitespace()) popLinePrefix() nextLine()
      case MDBlockQuote(contents) ⇒
        renderBlocks(contents, writer pushLinePrefix "> ") popLinePrefix()
      case MDCodeFence(infoString, lines) ⇒
        lines.foldLeft(writer append "```" append infoString nextLine()) {(w,line) ⇒
          w append (line data) nextLine()
        } append "```" nextLine()
      case MDList(ordered, items) ⇒
        items.zipWithIndex.foldLeft(writer) {case (w, (item, index)) ⇒
          val firstLinePrefix = if(ordered) s"${index+1}. " else "* "
          val followingLinesPrefix = " " * (firstLinePrefix length)
          item.foldLeft(w pushLinePrefix (firstLinePrefix, followingLinesPrefix)) {(w2, i) ⇒ renderBlock(i, w2)} popLinePrefix()
        }
      case MDParagraph(contents) ⇒
        contents.zipWithIndex.foldLeft(writer) {case (w, (t, index)) ⇒
          if(index==0) renderInline(t, w withoutLeadingWhitespace()) else renderInline(t, w append ' ' withoutLeadingWhitespace())
        } nextLine() nextLine()
      case MDThematicBreak ⇒ writer nextLine() append "---" nextLine() nextLine()
    }

    def renderBlocks(blocks:Traversable[MDBlock], writer:LineWriter):LineWriter =
      blocks.foldLeft(writer) {(w,t) ⇒ renderBlock(t, w)}

    def renderInline(inline:MDInline, writer:LineWriter):LineWriter = inline match {
      case MDCodeSpan(contents) ⇒ writer append '`' append contents append '`' // TODO what if contents contains a backtick?
      case MDEmphasis(contents) ⇒ renderInlines(contents, writer append '_') append '_'
      case MDHardLineBreak ⇒ writer append '\\' nextLine()
      case MDInlineText(data) ⇒ appendEscaped(data, writer)
      case MDLink(text, destination, title) ⇒
        def maybeAddTitle(w:LineWriter):LineWriter = title match {
          case Some(titleString) ⇒ appendEscaped(titleString, writer append " \"") append '\"'
          case None ⇒ w
        }
        val afterText = renderInlines(text, writer append '[') append ']'
        val afterLink = appendEscaped(destination replaceAll (" ", "%20"), afterText append '(')
        val afterTitle = maybeAddTitle(afterLink)
        afterTitle append ')'
      case MDStrikethrough(contents) ⇒ renderInlines(contents, writer append '~') append '~'
      case MDStrongEmphasis(contents) ⇒ renderInlines(contents, writer append "**") append "**"
    }

    def renderInlines(inlines:Traversable[MDInline], writer:LineWriter):LineWriter =
      inlines.foldLeft(writer) {(w,t) ⇒ renderInline(t, w)}

    @tailrec def appendEscaped(str:String, writer:LineWriter, startIndex:Int=0):LineWriter =
      if(startIndex >= str.length) writer
      else str indexWhere (isAsciiOperator, startIndex) match {
        case -1 ⇒ writer append (str substring startIndex)
        case `startIndex` ⇒ appendEscaped(str, writer append '\\' append str(startIndex), startIndex+1)
        case n ⇒ appendEscaped(str, writer append (str substring (startIndex, n)), n)
      }

    renderBlocks(blocks, LineWriterToAppendable(out))
  }

  private trait LineWriter {
    def append(str:String):LineWriter
    def append(ch:Char):LineWriter
    def nextLine():LineWriter
    def popLinePrefix():LineWriter
    def pushLinePrefix(prefix:String):LineWriter = WithLinePrefix(prefix, this)
    def pushLinePrefix(nextLine:String, linesThereafter:String):LineWriter = WithLinePrefix2(nextLine, linesThereafter, this)
    def withoutLeadingWhitespace():LineWriter = WithoutLeadingWhitespace(this)
  }
  private final case class LineWriterToAppendable(out:Appendable) extends LineWriter {
    def append(str:String) = {out append str; this}
    def append(ch:Char) = {out append ch; this}
    def nextLine() = {out append '\n'; this}
    def popLinePrefix() = sys error "no line prefix pushed"
  }
  private final case class WithLinePrefix(prefix:String, inner:LineWriter, atBeginningOfLine:Boolean=true) extends LineWriter {
    def append(str:String) =
      if(str isEmpty) this else if(atBeginningOfLine)
        copy(inner = inner append prefix append str, atBeginningOfLine = false)
      else {
        val newInner = inner append str
        if(newInner ne inner) copy(inner = newInner) else this
      }
    def append(ch:Char) =
      if(atBeginningOfLine) copy(inner = inner append prefix append ch, atBeginningOfLine = false)
      else {
        val newInner = inner append ch
        if(newInner ne inner) copy(inner = newInner) else this
      }
    def nextLine() = copy(inner = inner nextLine(), atBeginningOfLine = true)
    def popLinePrefix() = inner
  }
  private final case class WithLinePrefix2(nextLinePrefix:String, linesThereafter:String, inner:LineWriter) extends LineWriter {
    def append(str:String) =
      if(str isEmpty) this
      else WithLinePrefix(linesThereafter, inner append nextLinePrefix append str, atBeginningOfLine=false)
    def append(ch:Char) =
      WithLinePrefix(linesThereafter, inner append nextLinePrefix append ch, atBeginningOfLine=false)
    def nextLine() =
      WithLinePrefix(linesThereafter, inner append nextLinePrefix nextLine())
    def popLinePrefix() = inner
  }
  private final case class WithoutLeadingWhitespace(inner:LineWriter) extends LineWriter {
    def append(str:String):LineWriter = if(str isEmpty) this else removeLeadingWhitespace(str) match {
      case Some(noWs) if noWs isEmpty ⇒ this
      case Some(noWs) ⇒ inner append noWs
      case None ⇒ inner append str
    }
    def append(ch:Char):LineWriter = if(isWhitespace(ch)) this else inner append ch
    def nextLine():LineWriter = inner.nextLine()
    def popLinePrefix():LineWriter = inner.popLinePrefix()
    override def pushLinePrefix(prefix:String):LineWriter = WithoutLeadingWhitespace(inner pushLinePrefix prefix)
    override def pushLinePrefix(nextLine:String, linesThereafter:String):LineWriter = WithoutLeadingWhitespace(inner pushLinePrefix (nextLine, linesThereafter))
    override def withoutLeadingWhitespace():LineWriter = this
  }

  private val asciiOperators:Set[Char] = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~" toSet
  private def isAsciiOperator(ch:Char):Boolean = asciiOperators(ch)
  private def isWhitespace(ch:Char):Boolean = ch match {
    case ' ' | '\t' | '\n' | '\r' | '\u000B' | '\u000C' ⇒ true
    case _ ⇒ false
  }
  private def removeLeadingWhitespace(str:String):Option[String] =
    if(str isEmpty) None else if(isWhitespace(str(0))) removeLeadingWhitespace(str tail) match {
      case Some(noWs) ⇒ Some(noWs)
      case None ⇒ Some(str tail)
    } else None
}
