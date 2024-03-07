package devices;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MocksShowcaseTestScala extends TestTemplateImpl {

    @Override
    @BeforeEach
    public void initMockScala() {
        super.initMockScala();
        when(failingPolicyScala.attemptOn()).thenReturn(true, true, false);
        when(failingPolicyScala.policyName()).thenReturn("mock");
    }

    @Test
    @DisplayName("attemptOn is called as expected")
    void testAttemptOn() {
        verify(failingPolicyScala, times(0)).attemptOn();
        deviceScala.on();
        verify(failingPolicyScala, times(1)).attemptOn();
        assertTrue(deviceScala.isOn());

        deviceScala.off();
        verify(failingPolicyScala, times(1)).attemptOn();
        deviceScala.on();
        verify(failingPolicyScala, times(2)).attemptOn();
        assertTrue(deviceScala.isOn());

        deviceScala.off();
        verify(failingPolicyScala, times(2)).attemptOn();
        assertThrows(IllegalStateException.class, () -> deviceScala.on());
        verify(failingPolicyScala, times(3)).attemptOn();
    }

}