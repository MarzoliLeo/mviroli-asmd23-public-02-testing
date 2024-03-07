package devices;

import java.util.stream.IntStream;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FakesShowcaseTest extends TestTemplateImpl {

    @Override
    @BeforeEach
    public void initMock() {
        super.initMock();
        when(failingPolicy.attemptOn()).thenReturn(true, true, false);
        when(failingPolicy.policyName()).thenReturn("mock");
    }

    @Test
    @DisplayName("Device switch on and off until failing")
    void testSwitchesOnAndOff() {
        IntStream.range(0, 2).forEach(i -> {
            device.on();
            assertTrue(device.isOn());
            device.off();
            assertFalse(device.isOn());
        });
        assertThrows(IllegalStateException.class, () -> device.on());
    }

}
