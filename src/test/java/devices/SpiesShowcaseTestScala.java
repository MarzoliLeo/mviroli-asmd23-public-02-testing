package devices;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SpiesShowcaseTestScala extends DoubleTestImpl {

    @Override
    @BeforeEach
    public void initSpyScala() { super.initSpyScala(); }

    @Test
    @DisplayName("AttemptOn is called as expected")
    void testReset() {
        deviceScala.isOn();
        verifyNoInteractions(failingPolicyScala);
        try {
            deviceScala.on();
        } catch (IllegalStateException e) {
        }
        verify(failingPolicyScala).attemptOn();
        deviceScala.reset();
        assertEquals(2, mockingDetails(failingPolicyScala).getInvocations().size());
    }

}
