package darwin.model;

public record Plant(Vector2d position) implements WorldElement {
    @Override
    public Vector2d getPosition() {
        return position;
    }
    @Override
    public String toString() {
        return "*";
    }
}