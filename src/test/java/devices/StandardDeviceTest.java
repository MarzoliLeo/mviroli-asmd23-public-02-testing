package devices;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


public class StandardDeviceTest {
    @Test
    @DisplayName("Device must specify a strategy")
    void testNonNullStrategy() {
        assertThrows(NullPointerException.class, () -> new StandardDevice(null));
    }

}