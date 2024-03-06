package integration_tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import sol2.GUI;
import sol2.Log;
import sol2.LogicImpl;
import sol2.Position;

import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class IntegrationTest {

    private LogicImpl logicMock;
    private Log logMock;
    private GUI gui;

    @BeforeEach
    void setUp() {
        // Initialize the mocks
        logicMock = mock(LogicImpl.class);
        logMock = mock(Log.class);

        // Initialize the object under test and manually inject the mocks
        gui = new GUI(10);
        gui.setLogic(logicMock);
        gui.setLog(logMock);
    }

    @Test
    void testGameIntegration() {
        // Arrange
        JButton button = new JButton();
        gui.getCells().put(button, new Position(0, 0));

        // Stubbing the behavior of LogicImpl
        when(logicMock.hit(any())).thenReturn(Optional.of(1));
        when(logicMock.isOver()).thenReturn(false);

        // Act
        //gui.getActionListener().actionPerformed(new ActionEvent(button, ActionEvent.ACTION_PERFORMED, "Command"));
        //Check if hit any button of the grid.
        //verify(logicMock).hit(any());

        // Assert
        // Verify that the expected interactions between GUI, LogicImpl, and Log occurred
        //verify(logMock, atLeastOnce()).info(anyString());
        //verify(logMock, never()).error(anyString());
        verify(logicMock, atLeastOnce()).hit(any(Position.class));
        verify(logicMock, never()).moveMarks(); // Assuming that moveMarks is not called in this scenario
        verify(logicMock, atLeastOnce()).getMark(any(Position.class));

        // Verify that the GUI updated the button text
        assertEquals("1", button.getText());

        // Verify that the GUI did not exit the application (isOver is false)
        assertFalse(gui.isGameFinished());

        // Verify Log messages
        ArgumentCaptor<String> logCaptor = ArgumentCaptor.forClass(String.class);
        //verify(logMock, atLeastOnce()).info(logCaptor.capture());

        // Assert specific log messages if needed
        // assertEquals("Expected log message", logCaptor.getValue());
    }
}