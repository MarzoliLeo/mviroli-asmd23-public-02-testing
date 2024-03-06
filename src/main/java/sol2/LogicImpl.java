package sol2;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LogicImpl implements Logic {

    private final int size;
    private List<Position> marks = new LinkedList<>();
    private boolean moving = false;
    private Log log;

    public LogicImpl(int size, Log log) {
        this.size = size;
        this.log = log;
    }

    @Override
    public Optional<Integer> hit(Position position) {
        if (this.isOver()){
            this.log.info("Game over. Exiting...");
            return Optional.empty();
        }
        if (this.moving || startMoving(position)){
            this.moving = true;
            this.moveMarks();
            return Optional.empty();
        }
        this.marks.add(position);
        this.log.info("Cell marked at position: " + position);
        return Optional.of(this.marks.size());
    }

    private boolean neighbours(Position p1, Position p2){
        this.log.info("Checking for neightbours marks...");
        return Math.abs(p1.x()-p2.x()) <= 1 && Math.abs(p1.y()-p2.y()) <= 1;
    }

    private boolean startMoving(Position position) {
        this.log.info("Starting to move marks, but before...");
        return this.marks.stream().anyMatch(p -> neighbours(p, position));
    }

    //changed visibility for testing.
    public void moveMarks() {
        this.log.info("Moving marks...");
        this.marks = this.marks
                .stream()
                .map(p -> new Position(p.x()+1, p.y()-1))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Optional<Integer> getMark(Position position) {
        return Optional.of(this.marks.indexOf(position)).filter(i -> i>=0).map(i -> i+1);
    }

    @Override
    public boolean isOver() {
        return this.marks.stream().anyMatch(p -> p.x() == this.size || p.y() == -1);
    }

    //Aux methods for testing.

}
