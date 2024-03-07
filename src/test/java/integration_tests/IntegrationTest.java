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
        // Arrange
        GUI guiSpy = spy(new GUI(10, logMock));
        LogicImpl logicMock = mock(LogicImpl.class);

        guiSpy.setLogic(logicMock);

        // Stubbing the behavior of LogicImpl
        when(logicMock.hit(any())).thenReturn(Optional.of(1));
        when(logicMock.isOver()).thenReturn(false);

        // Act
        // Simulate user actions by clicking on a button
        // Get the first button in the cells map of guiSpy
        JButton button = guiSpy.getCells().keySet().iterator().next();

        // Simulate a click on the button
        guiSpy.handleButtonClick(button);
        button.doClick(); // Simulate a click on the first button

        // Assert
        // Verify that the expected interactions between GUI, LogicImpl, and Log occurred
        verify(logMock, atLeastOnce()).info(anyString());
        verify(logMock, never()).error(anyString());
        verify(logicMock, atLeastOnce()).hit(any(Position.class));
        verify(logicMock, never()).moveMarks();
        verify(logicMock, atLeastOnce()).getMark(any(Position.class));

        // Verify that the GUI updated the button text
        assertEquals("1", guiSpy.getCells().keySet().iterator().next().getText());

        // Verify that the GUI did not exit the application (isOver is false)
        assertFalse(guiSpy.isGameFinished());

        // Verify Log messages
        ArgumentCaptor<String> logCaptor = ArgumentCaptor.forClass(String.class);
        verify(logMock, atLeastOnce()).info(logCaptor.capture());

    }
}


