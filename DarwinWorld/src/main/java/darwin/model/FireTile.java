package darwin.model;

public class FireTile implements WorldElement {

    private final Vector2d position;

    public FireTile(Vector2d position) {
        this.position = position;
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "🔥";
    }
}
