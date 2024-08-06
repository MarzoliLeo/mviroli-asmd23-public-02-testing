package coverage

trait AdderScala {
  def add(i1: Int, i2: Int): Int
}

object AdderScala {
  private class AdderScalaImpl extends AdderScala {
    override def add(i1: Int, i2: Int): Int =
      (i1, i2) match {
        case (x, y) if x > 0 && y > 0 => x + y
        case _ => -1
      }
  }

  def apply(): AdderScala = new AdderScalaImpl

}
