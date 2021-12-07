package com.burningcastle.adventofcode.year2021.days;

import com.burningcastle.adventofcode.year2021.AbstractDay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day6 extends AbstractDay {

    public Day6() {
        super("src/resources/Day6.txt");
    }

    @Override
    public void run() {
        List<String> input = readLinesFromFile();
        List<Integer> fishPopulation = Arrays.stream(input.get(0).split(",")).map(Integer::parseInt).collect(Collectors.toList());

        // Part 1
        System.out.println("Part 1: " + simulatePopulationNaive(fishPopulation, 80).size()); // 387413

        // Part 2
        System.out.println("Part 2: " + simulatePopulation(fishPopulation, 256)); // 1738377086345
    }

    private long simulatePopulation(List<Integer> fishStartPopulation, int days) {
        long[] population = new long[9];
        fishStartPopulation.forEach(age -> population[age]++);

        for (int day = 1; day <= days; day++) {
            long fishWithTimer0 = population[0];
            for (int i = 0; i < population.length - 1; i++) {
                population[i] = population[i + 1];
            }
            // timer reset
            population[6] += fishWithTimer0;
            // new fish are born
            population[8] = fishWithTimer0;
        }

        return Arrays.stream(population).sum();
    }

    private List<Integer> simulatePopulationNaive(List<Integer> fishStartPopulation, int days) {
        List<Integer> fishs = new ArrayList<>(fishStartPopulation);
        for (int day = 1; day <= days; day++) {
            long newFish = fishs.stream().filter(age -> age == 0).count();
            fishs = fishs.stream().map(age -> age == 0 ? 6 : age - 1).collect(Collectors.toList());
            for (int fish = 1; fish <= newFish; fish++) {
                fishs.add(8);
            }
        }
        return fishs;
    }
}
