package devices

import java.util.Objects

trait DeviceScala {

  def on(): Unit

  def off(): Unit

  def isOn(): Boolean

  def reset(): Unit

}

object DeviceScala {
  private class StandardDeviceImpl(failingPolicy: FailingPolicyScala) extends DeviceScala {
    private var onFlag: Boolean = false

    override def on(): Unit =
      if (!failingPolicy.attemptOn())
        throw new IllegalStateException()
      else
        onFlag = true


    override def off(): Unit =
      onFlag = false

    override def isOn(): Boolean =
      onFlag

    override def reset(): Unit = {
      off()
      failingPolicy.reset()
    }

    override def toString: String =
      s"DeviceScala{policy=${failingPolicy.policyName()}, on=$onFlag}"

  }

  def apply(failingPolicy: FailingPolicyScala): DeviceScala = new StandardDeviceImpl(Objects.requireNonNull(failingPolicy))

}