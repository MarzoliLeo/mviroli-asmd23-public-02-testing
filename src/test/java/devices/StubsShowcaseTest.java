package devices;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StubsShowcaseTest extends DoubleTestImpl {

    @Override
    @BeforeEach
    public void initMock() { super.initMock(); }

    @Test
    @DisplayName("Device can be switched on")
    void testCanBeSwitchedOn() {
        when(failingPolicy.attemptOn()).thenReturn(true);
        device.on();
        assertTrue(device.isOn());
    }

    @Test
    @DisplayName("Device won't switch on if failing")
    void testWontSwitchOn() {
        when(failingPolicy.attemptOn()).thenReturn(false);
        when(failingPolicy.policyName()).thenReturn("mock");
        assertThrows(IllegalStateException.class, () -> device.on());
        assertEquals("StandardDevice{policy=mock, on=false}", device.toString());
    }

}