package devices;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class DummiesShowcaseTestScala extends TestTemplateImpl {

    @Override
    @BeforeEach
    public void initMockScala() { super.initMockScala(); }

    @Test
    @DisplayName("Device is initially off")
    void testInitiallyOff() {
        assertFalse(deviceScala.isOn());
    }

}