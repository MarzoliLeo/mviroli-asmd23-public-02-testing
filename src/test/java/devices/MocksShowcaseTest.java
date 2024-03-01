package devices;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MocksShowcaseTest extends DoubleTestImpl {
    @Override
    @BeforeEach
    public void initMock() {
        super.initMock();
        when(failingPolicy.attemptOn()).thenReturn(true, true, false);
        when(failingPolicy.policyName()).thenReturn("mock");
    }

    @Test
    @DisplayName("attemptOn is called as expected")
    void testAttemptOn() {
        verify(failingPolicy, times(0)).attemptOn();
        device.on();
        verify(failingPolicy, times(1)).attemptOn();
        assertTrue(device.isOn());

        device.off();
        verify(failingPolicy, times(1)).attemptOn();
        device.on();
        verify(failingPolicy, times(2)).attemptOn();
        assertTrue(device.isOn());

        device.off();
        verify(failingPolicy, times(2)).attemptOn();
        assertThrows(IllegalStateException.class, () -> device.on());
        verify(failingPolicy, times(3)).attemptOn();
    }

}