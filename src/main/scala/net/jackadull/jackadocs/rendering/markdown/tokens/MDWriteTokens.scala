package net.jackadull.jackadocs.rendering.markdown.tokens

import net.jackadull.jackadocs.rendering.markdown.MDText.isAsciiOperator

import scala.annotation.tailrec
import scala.language.postfixOps

object MDWriteTokens {
  def apply(tokens:Traversable[MDBlockToken], out:Appendable) {
    def renderBlock(blockToken:MDBlockToken, writer:LineWriter):LineWriter = blockToken match {
      case MDATXHeadingToken(level, contents) ⇒
        renderInlines(contents, writer pushLinePrefix ("#"*level+" ", " "*(level+1))) popLinePrefix() nextLine()
      case MDBlockQuoteToken(contents) ⇒
        renderBlocks(contents, writer pushLinePrefix "> ") popLinePrefix()
      case MDCodeFenceToken(infoString, lines) ⇒
        lines.foldLeft(writer append "```" append infoString nextLine()) {(w,line) ⇒
          w append (line data) nextLine()
        } append "```" nextLine()
      case MDListToken(ordered, items) ⇒
        items.zipWithIndex.foldLeft(writer) {case (w, (item, index)) ⇒
          val firstLinePrefix = if(ordered) s"${index+1}. " else "* "
          val followingLinesPrefix = " " * (firstLinePrefix length)
          renderBlock(item, w pushLinePrefix (firstLinePrefix, followingLinesPrefix)) popLinePrefix()
        }
      case MDParagraphToken(contents) ⇒
        contents.zipWithIndex.foldLeft(writer nextLine()) {case (w, (t, index)) ⇒
          if(index==0) renderInline(t, w) else renderInline(t, w append ' ')
        } nextLine() nextLine()
      case MDThematicBreakToken ⇒ writer nextLine() append "---" nextLine() nextLine()
    }

    def renderBlocks(blockTokens:Traversable[MDBlockToken], writer:LineWriter):LineWriter =
      blockTokens.foldLeft(writer) {(w,t) ⇒ renderBlock(t, w)}

    def renderInline(inlineToken:MDInlineToken, writer:LineWriter):LineWriter = inlineToken match {
      case MDCodeSpanToken(contents) ⇒ appendEscaped(contents, writer append '`') append '`'
      case MDEmphasisToken(contents) ⇒ renderInlines(contents, writer append '_') append '_'
      case MDHardLineBreakToken ⇒ writer append '\\' nextLine()
      case MDInlineTextToken(data) ⇒ appendEscaped(data, writer)
      case MDLinkToken(text, destination, title) ⇒
        def maybeAddTitle(w:LineWriter):LineWriter = title match {
          case Some(titleString) ⇒ appendEscaped(titleString, writer append " \"") append '\"'
          case None ⇒ w
        }
        val afterText = renderInlines(text, writer append '[') append ']'
        val afterLink = appendEscaped(destination replaceAll (" ", "%20"), afterText append '(')
        val afterTitle = maybeAddTitle(afterLink)
        afterTitle append ')'
      case MDStrikethroughToken(contents) ⇒ renderInlines(contents, writer append '~') append '~'
      case MDStrongEmphasisToken(contents) ⇒ renderInlines(contents, writer append "**") append "**"
    }

    def renderInlines(inlineTokens:Traversable[MDInlineToken], writer:LineWriter):LineWriter =
      inlineTokens.foldLeft(writer) {(w,t) ⇒ renderInline(t, w)}

    @tailrec def appendEscaped(str:String, writer:LineWriter, startIndex:Int=0):LineWriter =
      if(startIndex >= str.length) writer
      else str indexWhere (isAsciiOperator, startIndex) match {
        case -1 ⇒ writer append (str substring startIndex)
        case `startIndex` ⇒ appendEscaped(str, writer append '\\' append str(startIndex), startIndex+1)
        case n ⇒ appendEscaped(str, writer append (str substring (startIndex, n)), n)
      }

    renderBlocks(tokens, LineWriterToAppendable(out))
  }

  private trait LineWriter {
    def append(str:String):LineWriter
    def append(ch:Char):LineWriter
    def nextLine():LineWriter
    def popLinePrefix():LineWriter
    def pushLinePrefix(prefix:String):LineWriter = WithLinePrefix(prefix, this)
    def pushLinePrefix(nextLine:String, linesThereafter:String):LineWriter = WithLinePrefix2(nextLine, linesThereafter, this)
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
}
