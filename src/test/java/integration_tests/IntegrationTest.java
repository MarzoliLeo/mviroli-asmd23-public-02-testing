package integration_tests;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import sol2.*;

import javax.swing.*;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class IntegrationTest {

    @Test
    void testGameIntegration() {
        Log logMock = mock(Log.class);
        GUI guiSpy = spy(new GUI(10, logMock));
        LogicImpl logicMock = mock(LogicImpl.class);

        guiSpy.setLogic(logicMock);

        stubLogicBehavior(logicMock);

        JButton button = getFirstButton(guiSpy);
        simulateButtonClick(guiSpy, button);

        verifyInteractions(logMock, logicMock, guiSpy);
        verifyButtonText(guiSpy);
        verifyGameNotFinished(guiSpy);
        captureLogMessages(logMock);
    }

    private void stubLogicBehavior(LogicImpl logicMock) {
        when(logicMock.hit(any())).thenReturn(Optional.of(1));
        when(logicMock.isOver()).thenReturn(false);
    }

    private JButton getFirstButton(GUI guiSpy) {
        return guiSpy.getCells().keySet().iterator().next();
    }

    private void simulateButtonClick(GUI guiSpy, JButton button) {
        guiSpy.handleButtonClick(button);
        button.doClick();
    }

    private void verifyInteractions(Log logMock, LogicImpl logicMock, GUI guiSpy) {
        verify(logMock, atLeastOnce()).info(anyString());
        verify(logMock, never()).error(anyString());
        verify(logicMock, atLeastOnce()).hit(any(Position.class));
        verify(logicMock, never()).moveMarks();
        verify(logicMock, atLeastOnce()).getMark(any(Position.class));
    }

    private void verifyButtonText(GUI guiSpy) {
        assertEquals("1", guiSpy.getCells().keySet().iterator().next().getText());
    }

    private void verifyGameNotFinished(GUI guiSpy) {
        assertFalse(guiSpy.isGameFinished());
    }

    private void captureLogMessages(Log logMock) {
        ArgumentCaptor<String> logCaptor = ArgumentCaptor.forClass(String.class);
        verify(logMock, atLeastOnce()).info(logCaptor.capture());
    }
}


