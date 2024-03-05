package devices;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StubsShowcaseTestScala extends DoubleTestImpl{

    @Override
    @BeforeEach
    public void initMockScala() { super.initMockScala(); }

    @Test
    @DisplayName("Device can be switched on")
    void testCanBeSwitchedOn() {
        when(failingPolicyScala.attemptOn()).thenReturn(true);
        deviceScala.on();
        assertTrue(deviceScala.isOn());
    }

    @Test
    @DisplayName("Device won't switch on if failing")
    void testWontSwitchOn() {
        when(failingPolicyScala.attemptOn()).thenReturn(false);
        when(failingPolicyScala.policyName()).thenReturn("mock");
        assertThrows(IllegalStateException.class, () -> deviceScala.on());
        assertEquals("DeviceScala{policy=mock, on=false}", deviceScala.toString());
    }

}