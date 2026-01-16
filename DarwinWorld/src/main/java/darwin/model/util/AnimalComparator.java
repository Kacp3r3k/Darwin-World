package darwin.model.util;


import darwin.model.Animal;

import java.util.Comparator;

public class AnimalComparator implements Comparator<Animal> {
    @Override
    public int compare(Animal a1, Animal a2) {
        int energyDiff = Integer.compare(a2.getEnergy(), a1.getEnergy());
        if (energyDiff != 0) return energyDiff;

        int ageDiff = Integer.compare(a2.getAge(), a1.getAge());
        if (ageDiff != 0) return ageDiff;

        int childrenDiff = Integer.compare(a2.getChildrenCount(), a1.getChildrenCount());
        if (childrenDiff != 0) return childrenDiff;

        return 0;
    }
}