package core;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

// Helper class that holds coordinates (x, y) in the world. Useful for:
// 1. Holds the room's center when connecting to the hallways.
// 2. Tracking visited tiles
public class Position implements Serializable {
    public int x;
    public int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Distance between two positions (used for hallway length or sorting)
    public int distanceTo(Position other) {
        return Math.abs(this.x - other.x) + Math.abs(this.y - other.y);
    }

    // override method to compare positions
    // helpful to compare if two positions are the same (e.g. for hallway intersection)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position other = (Position) o;
        return this.x == other.x && this.y == other.y;
    }

    // Useful when we want to use Position inside a HashSet/Map.
    // To keep track of all tiles already visited.
    // Also to keep track of placed tiles to avoid duplicates.
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    // Useful for testing and debugging
    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public List<Position> getCardinalNeighbors() {
        return List.of(
                new Position(x + 1, y),
                new Position(x - 1, y),
                new Position(x, y + 1),
                new Position(x, y - 1)
        );
    }
}
