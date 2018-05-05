package net.jackadull.jackadocs.rendering.markdown

import scala.language.postfixOps
import scala.xml._

object HTMLToMarkdown {
  def apply(ns:NodeSeq):Seq[MDBlock] = convertBlocks(ns)

  def inline(ns:NodeSeq):Seq[MDInline] = convertInlines(ns)

  private def convertBlocks(ns:NodeSeq):Seq[MDBlock] = {
    def combineStrayInlines(s:Seq[NodeSeq], soFar:Vector[MDInline]=Vector()):Seq[MDBlock] = s match {
      case Seq(strayInline, rst@_*) if convertSingleInline isDefinedAt strayInline ⇒
        convertSingleInline(strayInline) match {
          case MDInlineText(txt) if txt.trim.isEmpty ⇒ combineStrayInlines(rst, soFar)
          case singleInline ⇒ combineStrayInlines(rst, soFar :+ singleInline)
        }
      case Seq() ⇒ if(soFar isEmpty) Vector() else Vector(MDParagraph(soFar))
      case rst ⇒ if(soFar isEmpty) recurseOver(rst) else MDParagraph(soFar) +: recurseOver(rst)
    }
    def recurseOver(s:Seq[NodeSeq]):Seq[MDBlock] = s match {
      case Seq(single, rst@_*) if convertSingleBlock isDefinedAt single ⇒ convertSingleBlock(single) +: recurseOver(rst)
      case Seq(strayInline, _*) if convertSingleInline isDefinedAt strayInline ⇒ combineStrayInlines(s)
      case Seq(fst, rst@_*) ⇒ convertBlocks(fst) ++ recurseOver(rst)
      case Seq() ⇒ Vector()
    }
    ns match {
      case single if convertSingleBlock isDefinedAt single ⇒ Vector(convertSingleBlock(single))
      case a:Atom[_] if s"${a data}".trim isEmpty ⇒ Vector()
      case strayInline if convertSingleInline isDefinedAt strayInline ⇒ Vector(MDParagraph(Vector(convertSingleInline(strayInline))))
      case doc:Document ⇒ recurseOver(doc children)
      case e:Elem ⇒ recurseOver(e child)
      case Group(child) ⇒ recurseOver(child)
      case _ if (ns.getClass.getName startsWith "scala.xml.NodeSeq") && (ns.theSeq != ns) ⇒ recurseOver(ns theSeq)
      case _ if ns isEmpty ⇒ Vector()
      case _ ⇒ sys error s"unhandled NodeSeq type: $ns"
    }
  }

  private def convertSingleBlock:PartialFunction[NodeSeq,MDBlock] = {
    case e:Elem if e.label == "blockquote" ⇒ MDBlockQuote(convertBlocks(e child))
    case e:Elem if e.label == "code" || e.label == "tt" ⇒
      val infoString = e.attribute("class") match {
        case None ⇒ ""
        case Some(classNodes) ⇒
          val className = classNodes.map(_ text).mkString
          if(className startsWith "language-") className.substring("language-" length) else ""
      }
      val inlineTexts = e.child.text.filterNot(_ == '\r').split('\n').filterNot(_ isEmpty) map MDInlineText
      MDCodeFence(infoString, inlineTexts)
    case e:Elem if e.label == "p" ⇒ MDParagraph(convertInlines(e child))
    case e:Elem if e.label == "ul" || e.label == "ol" ⇒
      extractList(e child, e.label == "ol")
  }

  private def convertInlines(ns:NodeSeq):Seq[MDInline] = ns match {
    case single if convertSingleInline isDefinedAt single ⇒ Vector(convertSingleInline(single))
    case a:Atom[_] if s"${a data}".trim isEmpty ⇒ Vector()
    case doc:Document ⇒ doc.children.toVector flatMap convertInlines
    case e:Elem ⇒ e.child.toVector flatMap convertInlines
    case Group(child) ⇒ child.toVector flatMap convertInlines
    case _ if (ns.getClass.getName startsWith "scala.xml.NodeSeq") && (ns.theSeq != ns) ⇒ ns.theSeq.toVector flatMap convertInlines
    case _ if ns isEmpty ⇒ Vector()
    case _ ⇒ sys error s"unhandled NodeSeq type: $ns"
  }

  private val convertSingleInline:PartialFunction[NodeSeq,MDInline] = {
    case a:Atom[_] if s"${a data}".trim nonEmpty ⇒ inlineText(s"${a data}")
    case e:Elem if e.label == "a" && e.attribute("href").nonEmpty ⇒
      MDLink(
        text = convertInlines(e child),
        destination = e.attribute("href").getOrElse(Seq()) text,
        title = e.attribute("title").map(_ text)
      )
    case e:Elem if e.label == "code" || e.label == "tt" ⇒ MDCodeSpan(e.child text)
    case e:Elem if e.label == "del" ⇒ MDStrikethrough(convertInlines(e child))
    case e:Elem if e.label == "em" || e.label == "i" ⇒ MDEmphasis(convertInlines(e child))
  }

  private def extractList(nodes:Seq[NodeSeq], ordered:Boolean, soFar:Vector[Seq[MDBlock]]=Vector()):MDList = nodes match {
    case Seq(e:Elem, rst@_*) if e.label=="li" ⇒
      extractList(rst, ordered, soFar :+ convertBlocks(e child))
    case Seq(Group(child), rst@_*) ⇒ extractList(child ++ rst, ordered, soFar)
    case Seq(_, rst@_*) ⇒ extractList(rst, ordered, soFar)
    case Seq() ⇒ MDList(ordered, soFar)
  }

  private def inlineText(data:String):MDInlineText = MDInlineText(prepareInlineText(data))

  private def prepareInlineText(data:String):String = data.filterNot(_ == '\r').split('\n').map(_ trim).filterNot(_ isEmpty) mkString " "
}
