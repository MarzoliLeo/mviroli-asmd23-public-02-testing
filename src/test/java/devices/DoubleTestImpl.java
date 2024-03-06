package devices;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;


public class DoubleTestImpl implements DoubleTest {

    // Task 1 Test - with Java
    Device device;
    FailingPolicy failingPolicy;

    // Task 2 Test - with Scala
    DeviceScala deviceScala;
    FailingPolicyScala failingPolicyScala;

    @Override
    public void initMock() {
        this.failingPolicy = mock(FailingPolicy.class);
        this.device = new StandardDevice(failingPolicy);
    }

    @Override
    public void initSpy() {
        this.failingPolicy = spy(new RandomFailing());
        this.device = new StandardDevice(failingPolicy);
    }

    @Override
    public void initMockScala() {
        this.failingPolicyScala = mock(FailingPolicyScala.class);
        this.deviceScala = DeviceScala.apply(failingPolicyScala);
    }

    @Override
    public void initSpyScala() {
        this.failingPolicyScala = spy(FailingPolicyScala.apply());
        this.deviceScala =  DeviceScala.apply(failingPolicyScala);
    }

}
