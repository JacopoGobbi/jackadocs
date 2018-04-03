package net.jackadull.jackadocs.rendering.markdown

trait MDBlock {
  def render(append:String⇒Unit)
}
object MDBlock {
  trait MDInline extends MDBlock
}
