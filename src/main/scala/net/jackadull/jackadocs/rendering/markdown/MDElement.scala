package net.jackadull.jackadocs.rendering.markdown

import scala.language.postfixOps

sealed trait MDElement {
  def treeStructure(linePrefix:String=""):String
}
sealed trait MDInline extends MDElement
sealed trait MDBlock extends MDElement

final case class MDATXHeading(level:Int, contents:Seq[MDInline]) extends MDBlock {
  require(level>0 && level<=6, s"level must be from 1 to 6, but is $level")
  def treeStructure(linePrefix:String):String = s"${linePrefix}ATXHeading level=$level\n${contents.map(_.treeStructure(s"$linePrefix  ")) mkString}"
}
final case class MDBlockQuote(contents:Seq[MDBlock]) extends MDBlock {
  def treeStructure(linePrefix:String):String = s"${linePrefix}BlockQuote\n${contents.map(_.treeStructure(s"$linePrefix  ")) mkString}"
}
final case class MDCodeFence(infoString:String, lines:Seq[MDInlineText]) extends MDBlock {
  require(!((infoString contains '\n') || (infoString contains '\r') || (infoString contains '`')), s"info string cannot contain line break or backtick, but is: $infoString")
  def treeStructure(linePrefix:String):String = s"${linePrefix}CodeFence infoString='$infoString'\n${lines.map(_.treeStructure(s"$linePrefix  ")) mkString}"
}
final case class MDCodeSpan(contents:String) extends MDInline {
  def treeStructure(linePrefix:String):String = s"${linePrefix}CodeSpan: '${contents.replaceAll("\r", "\\r").replaceAll("\n", "\\n")}'\n"
}
final case class MDEmphasis(contents:Seq[MDInline]) extends MDInline {
  def treeStructure(linePrefix:String):String = s"${linePrefix}Emphasis\n${contents.map(_.treeStructure(s"$linePrefix  ")) mkString}"
}
object MDHardLineBreak extends MDInline {
  def treeStructure(linePrefix:String):String = s"${linePrefix}HardLineBreak\n"
}
final case class MDImage(description:Seq[MDInline], url:String, title:Option[String]) extends MDInline {
  def treeStructure(linePrefix:String):String = s"${linePrefix}Image url='$url' title=$title\n${description.map(_.treeStructure(s"$linePrefix  ")) mkString}"
}
final case class MDInlineText(data:String) extends MDInline {
  require(!((data contains '\n') || (data contains '\r')), "inline text cannot contain a line break")
  def treeStructure(linePrefix:String):String = s"${linePrefix}InlineText: '$data'\n"
}
final case class MDLink(text:Seq[MDInline], destination:String, title:Option[String]) extends MDInline {
  def treeStructure(linePrefix:String):String = s"${linePrefix}Link destination='$destination' title=$title\n${text.map(_.treeStructure(s"$linePrefix  ")) mkString}"
}
final case class MDList(ordered:Boolean, items:Seq[Seq[MDBlock]], tight:Boolean=false) extends MDBlock {
  def treeStructure(linePrefix:String):String = s"${linePrefix}List ordered=$ordered\n${items.map(item ⇒ s"$linePrefix  item\n${item.map(_.treeStructure(s"$linePrefix    ")) mkString}") mkString}"
}
final case class MDParagraph(contents:Seq[MDInline]) extends MDBlock {
  def treeStructure(linePrefix:String):String = s"${linePrefix}Paragraph\n${contents.map(_.treeStructure(s"$linePrefix  ")) mkString}"
}
final case class MDStrikethrough(contents:Seq[MDInline]) extends MDInline {
  def treeStructure(linePrefix:String):String = s"${linePrefix}Strikethrough\n${contents.map(_.treeStructure(s"$linePrefix  ")) mkString}"
}
final case class MDStrongEmphasis(contents:Seq[MDInline]) extends MDInline {
  def treeStructure(linePrefix:String):String = s"${linePrefix}StrongEmphasis\n${contents.map(_.treeStructure(s"$linePrefix  ")) mkString}"
}
object MDThematicBreak extends MDBlock {
  def treeStructure(linePrefix:String):String = s"${linePrefix}ThematicBreak\n"
}
