package net.jackadull.jackadocs.rendering.markdown

import net.jackadull.jackadocs.rendering.markdown.MDBlock.MDInline
import net.jackadull.jackadocs.rendering.markdown.MDText.isAsciiOperator

import scala.language.postfixOps

final case class MDText(data:String) extends MDInline {
  private lazy val escaped:String = data flatMap {ch ⇒ if(isAsciiOperator(ch)) s"\\$ch" else ch.toString}
  def render(append:String⇒Unit) {append(escaped)}
}
object MDText {
  private val asciiOperators:Set[Char] = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~" toSet
  private[markdown] def isAsciiOperator(ch:Char):Boolean = asciiOperators(ch)
}
