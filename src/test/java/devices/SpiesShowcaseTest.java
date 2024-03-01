package devices;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SpiesShowcaseTest extends DoubleTestImpl {
    @Override
    @BeforeEach
    public void initSpy() {
        super.initSpy();
    }

    @Test
    @DisplayName("AttemptOn is called as expected")
    void testReset() {
        device.isOn();
        verifyNoInteractions(failingPolicy);
        try {
            device.on();
        } catch (IllegalStateException e) {
        }
        verify(failingPolicy).attemptOn();
        device.reset();
        assertEquals(2, mockingDetails(failingPolicy).getInvocations().size());
    }

}
