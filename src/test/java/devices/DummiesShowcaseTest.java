package devices;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class DummiesShowcaseTest extends TestTemplateImpl {

    @Override
    @BeforeEach
    public void initMock() { super.initMock(); }

    @Test
    @DisplayName("Device is initially off")
    void testInitiallyOff() {
        assertFalse(device.isOn());
    }

}