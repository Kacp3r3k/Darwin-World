package darwin.model;

public class Animal implements WorldElement {
    private MapDirection direction;
    private Vector2d position;
    private int energy;
    private final Genotype genotype;
    private int age = 0;
    private int childrenCount = 0;
    private boolean isDead = false;
    private int plantsEaten = 0;
    private int deathDay = -1;

    public Animal(Vector2d position, int initialEnergy, int genomeSize) {
        this.position = position;
        this.energy = initialEnergy;
        this.genotype = new Genotype(genomeSize);
        this.direction = MapDirection.values()[new java.util.Random().nextInt(8)];
    }

    public Animal(Vector2d position, int initialEnergy, Animal p1, Animal p2, int mutMin, int mutMax) {
        this.position = position;
        this.energy = initialEnergy;
        this.genotype = new Genotype(p1, p2, mutMin, mutMax);
        this.direction = MapDirection.values()[new java.util.Random().nextInt(8)];
    }

    public void move(MoveValidator validator) {
        if(isDead) return;

        int rotation = genotype.activateGene();
        this.direction = this.direction.rotate(rotation);

        Vector2d unitVector = this.direction.toUnitVector();
        Vector2d newPos = this.position.add(unitVector);

        if (validator.canMoveTo(newPos)) {
            this.position = newPos;
        }

        this.age++;
        this.energy--;
    }


    public void addEnergy(int amount) {
        this.energy += amount;
        this.plantsEaten++;
    }
    public void subtractEnergy(int amount) { this.energy -= amount; }
    public void addChild() { this.childrenCount++; }
    public void markAsDead(int day) {
        this.isDead = true;
        this.deathDay = day;
    }

    public boolean isDead() { return isDead; }
    public int getEnergy() { return energy; }
    public int getAge() { return age; }
    public int getChildrenCount() { return childrenCount; }
    public Genotype getGenotype() { return genotype; }
    public int getPlantsEaten() { return plantsEaten; }
    public int getDeathDay() { return deathDay; }

    public void setPosition(Vector2d position) { this.position = position; }

    @Override
    public Vector2d getPosition() { return position; }

    @Override
    public String toString() { return direction.toString(); }
}