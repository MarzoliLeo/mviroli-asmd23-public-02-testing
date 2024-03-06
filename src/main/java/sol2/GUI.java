package sol2;

import javax.swing.*;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.ActionListener;

public class GUI extends JFrame {
    
    private static final long serialVersionUID = -6218820567019985015L;
    private final Map<JButton, Position> cells = new HashMap<>();
    private Logic logic;
    private Log log;
    
    public GUI(int size, Log log) {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(70*size, 70*size);
        this.log = log;
        this.logic = new LogicImpl(size, this.log);

        
        JPanel panel = new JPanel(new GridLayout(size,size));
        this.getContentPane().add(panel);
        
        ActionListener al = e -> {
            var jb = (JButton)e.getSource();
            this.logic.hit(this.cells.get(jb));
            this.log.info("GUI finished hit...");
            for (var entry: this.cells.entrySet()){
                entry.getKey().setText(
                    this.logic
                        .getMark(entry.getValue())
                        .map(String::valueOf)
                        .orElse(" "));
            }
            if (this.logic.isOver()){
                this.log.info("Game over. Exiting...");
                System.exit(0);
            }
        };
                
        for (int i=0; i<size; i++){
            for (int j=0; j<size; j++){
            	final JButton jb = new JButton();
                this.cells.put(jb, new Position(j,i));
                jb.addActionListener(al);
                panel.add(jb);
            }
        }
        this.setVisible(true);
    }

    /*Aux method for integration testing.*/

    // Setter methods for integration testing
    public void setLogic(Logic logic) {
        this.logic = logic;
    }

    // Getter methods for integration testing
    public Map<JButton, Position> getCells() {
        return this.cells;
    }

    // Method to check if the game is finished for integration testing
    public boolean isGameFinished() {
        return this.logic.isOver();
    }

    // New method to handle button clicks
    public void handleButtonClick(JButton button) {
        Position position = this.cells.get(button);
        this.logic.hit(position);

        for (var entry : this.cells.entrySet()) {
            entry.getKey().setText(
                    this.logic
                            .getMark(entry.getValue())
                            .map(String::valueOf)
                            .orElse(" "));
        }

        if (this.logic.isOver()) {
            this.log.info("Game over. Exiting...");
            System.exit(0);
        }
    }
    
}
