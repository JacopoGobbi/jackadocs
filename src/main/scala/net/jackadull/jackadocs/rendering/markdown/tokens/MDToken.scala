package net.jackadull.jackadocs.rendering.markdown.tokens

private[markdown] sealed trait MDToken
private[markdown] sealed trait MDInlineToken extends MDToken
private[markdown] sealed trait MDBlockToken extends MDToken

private[markdown] final case class MDATXHeadingToken(level:Int, contents:Seq[MDInlineToken]) extends MDBlockToken
  {require(level>0 && level<=6, s"level must be from 1 to 6, but is $level")}
private[markdown] final case class MDBlockQuoteToken(contents:Seq[MDBlockToken]) extends MDBlockToken
private[markdown] final case class MDCodeSpanToken(contents:String) extends MDInlineToken
private[markdown] final case class MDCodeFenceToken(infoString:String, lines:Seq[MDInlineTextToken]) extends MDBlockToken
  {require(!((infoString contains '\n') || (infoString contains '\r') || (infoString contains '`')), s"info string cannot contain line break or backtick, but is: $infoString") }
private[markdown] final case class MDEmphasisToken(contents:Seq[MDInlineToken]) extends MDInlineToken
private[markdown] object MDHardLineBreakToken extends MDInlineToken
private[markdown] final case class MDInlineTextToken(data:String) extends MDInlineToken
  {require(!((data contains '\n') || (data contains '\r')), "inline text cannot contain a line break") }
private[markdown] final case class MDLinkToken(text:Seq[MDInlineToken], destination:String, title:Option[String]) extends MDInlineToken
private[markdown] final case class MDListToken(ordered:Boolean, items:Seq[MDBlockToken]) extends MDBlockToken
private[markdown] final case class MDParagraphToken(contents:Seq[MDInlineToken]) extends MDBlockToken
private[markdown] final case class MDStrikethroughToken(contents:Seq[MDInlineToken]) extends MDInlineToken
private[markdown] final case class MDStrongEmphasisToken(contents:Seq[MDInlineToken]) extends MDInlineToken
private[markdown] object MDThematicBreakToken extends MDBlockToken
