package devices;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DummiesShowcaseTest extends TestTemplateImpl {

    @Override
    public void initMock() { super.initMock(); }

    @Test
    @DisplayName("Device is initially off")
    void testInitiallyOff() {
        FailingPolicy dummyFailingPolicy = mock(FailingPolicy.class);
        device = new StandardDevice(dummyFailingPolicy);
        assertFalse(device.isOn());
    }


}