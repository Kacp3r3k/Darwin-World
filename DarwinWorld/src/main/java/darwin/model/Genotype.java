package darwin.model;

import java.util.Random;

public class Genotype {
    private final int[] genes;
    private int currentGeneIndex;
    private final Random random = new Random();

    public Genotype(int size) {
        this.genes = new int[size];
        for (int i = 0; i < size; i++) {
            genes[i] = random.nextInt(8);
        }
        this.currentGeneIndex = random.nextInt(size);
    }

    public Genotype(Animal p1, Animal p2, int mutationCountMin, int mutationCountMax) {
        int size = p1.getGenotype().genes.length;
        this.genes = new int[size];

        int totalEnergy = p1.getEnergy() + p2.getEnergy();
        double p1Share = (double) p1.getEnergy() / totalEnergy;
        int splitPoint = (int) (p1Share * size);

        boolean leftSideStrong = random.nextBoolean();

        if (leftSideStrong) {
            System.arraycopy(p1.getGenotype().genes, 0, this.genes, 0, splitPoint);
            System.arraycopy(p2.getGenotype().genes, splitPoint, this.genes, splitPoint, size - splitPoint);
        } else {
            System.arraycopy(p2.getGenotype().genes, 0, this.genes, 0, size - splitPoint);
            System.arraycopy(p1.getGenotype().genes, size - splitPoint, this.genes, size - splitPoint, splitPoint);
        }

        int mutations = mutationCountMin + random.nextInt(mutationCountMax - mutationCountMin + 1);
        for(int i=0; i<mutations; i++){
            int geneIdx = random.nextInt(size);
            genes[geneIdx] = random.nextInt(8);
        }

        this.currentGeneIndex = random.nextInt(size);
    }

    public int activateGene() {
        int gene = genes[currentGeneIndex];
        currentGeneIndex = (currentGeneIndex + 1) % genes.length;
        return gene;
    }

    public int[] getGenes() { return genes; }
}