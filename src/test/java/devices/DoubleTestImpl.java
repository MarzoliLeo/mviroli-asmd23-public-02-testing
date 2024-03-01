package devices;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class DoubleTestImpl implements DoubleTest {
    Device device;
    FailingPolicy failingPolicy;

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
}
