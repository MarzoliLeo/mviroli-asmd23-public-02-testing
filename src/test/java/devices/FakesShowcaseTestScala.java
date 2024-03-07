package devices;

import java.util.stream.IntStream;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FakesShowcaseTestScala extends TestTemplateImpl {

    @Override
    @BeforeEach
    public void initMockScala() {
        super.initMockScala();
        when(failingPolicyScala.attemptOn()).thenReturn(true, true, false);
        when(failingPolicyScala.policyName()).thenReturn("mock");
    }

    @Test
    @DisplayName("Device switch on and off until failing")
    void testSwitchesOnAndOff() {
        IntStream.range(0, 2).forEach(i -> {
            deviceScala.on();
            assertTrue(deviceScala.isOn());
            deviceScala.off();
            assertFalse(deviceScala.isOn());
        });
        assertThrows(IllegalStateException.class, () -> deviceScala.on());
    }

}
