package devices;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DummiesShowcaseTestScala extends TestTemplateImpl {

    @Override
    public void initMockScala() { super.initMockScala(); }

    @Test
    @DisplayName("Device is initially off")
    void testInitiallyOff() {
        FailingPolicy dummyFailingPolicy = mock(FailingPolicy.class);
        device = new StandardDevice(dummyFailingPolicy);
        assertFalse(device.isOn());
    }


}