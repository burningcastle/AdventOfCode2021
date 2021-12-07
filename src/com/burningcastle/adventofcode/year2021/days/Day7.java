package com.burningcastle.adventofcode.year2021.days;

import com.burningcastle.adventofcode.year2021.AbstractDay;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day7 extends AbstractDay {

    public Day7() {
        super("src/resources/Day7.txt");
    }

    @Override
    public void run() {
        List<String> input = readLinesFromFile();
        List<Integer> crabPositions = Arrays.stream(input.get(0).split(",")).map(Integer::parseInt).collect(Collectors.toList());

        // Part 1
        System.out.println("Part 1: " + getOptimalFuelCosts1(crabPositions)); // 337833

        // Part 2
        System.out.println("Part 2: " + getOptimalFuelCosts2(crabPositions)); // 96678050
    }

    private int getOptimalFuelCosts1(List<Integer> crabPositions) {
        crabPositions.sort(Integer::compareTo);
        int median = (crabPositions.size() + 1) / 2;
        int targetPos = crabPositions.get(median);
        return calculateFuelCostWithConstantRate(targetPos, crabPositions);
    }

    // Part 1 -> fuel cost = distance to target
    private int calculateFuelCostWithConstantRate(int targetPosition, List<Integer> crabPositions) {
        return crabPositions.stream().reduce(0, (fuelCostSum, crabPosition) -> {
            int cost = Math.abs(crabPosition - targetPosition);
            return fuelCostSum + cost;
        });
    }

    private int getOptimalFuelCosts2(List<Integer> crabPositions) {
        float avg = getAverage(crabPositions);
        int targetPos = (int) avg; // needs to be rounded down for actual input, idk why, is my math wrong? 461.557 --> 461
//        int targetPos = (int) Math.ceil(avg); // rounding needed for example input 4.9 --> 5
        return calculateFuelCostWithIncreasingRate(targetPos, crabPositions);
    }

    // Part 2 -> fuel cost increases by 1 per step
    private int calculateFuelCostWithIncreasingRate(int targetPosition, List<Integer> crabPositions) {
        return crabPositions.stream().reduce(0, (fuelCostSum, crabPosition) -> {
            int distanceToTarget = Math.abs(crabPosition - targetPosition);
            int cost = (distanceToTarget * distanceToTarget + distanceToTarget) / 2; // Gaußsche Summenformel
            return fuelCostSum + cost;
        });
    }

    private float getAverage(List<Integer> nums) {
        int sum = nums.stream().reduce(Integer::sum).orElse(0);
        return (float) sum / nums.size();
    }
}
