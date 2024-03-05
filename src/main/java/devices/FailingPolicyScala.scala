package devices

import scala.util.Random

trait FailingPolicyScala {

  def attemptOn(): Boolean

  def reset(): Unit

  def policyName(): String

}

object FailingPolicyScala {
  private class RandomFailingImpl extends FailingPolicyScala {

    private val random = new Random
    private var failed = false

    override def attemptOn(): Boolean = {
      failed = failed || random.nextBoolean()
      !failed
    }

    override def reset(): Unit =
      failed = false

    override def policyName(): String =
      "random"

  }

  def apply(): FailingPolicyScala = new RandomFailingImpl

}
