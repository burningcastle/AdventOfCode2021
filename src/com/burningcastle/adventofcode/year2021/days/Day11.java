package com.burningcastle.adventofcode.year2021.days;

import com.burningcastle.adventofcode.year2021.AbstractDay;
import com.burningcastle.adventofcode.year2021.util.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// ugly ugly. needs refactoring
public class Day11 extends AbstractDay {

    private int[][] octopuses;

    public Day11() {
        super("src/resources/Day11.txt");
    }

    @Override
    public void run() {
        List<String> input = readLinesFromFile();
        this.octopuses = input.stream().map(line -> Arrays.stream(line.split("")).mapToInt(Integer::parseInt).toArray()).toArray(int[][]::new);

        // Part 1
        System.out.println("Part 1: " + performSteps1(100)); // 1785

        // Part 2
        // reset octopuses
        this.octopuses = input.stream().map(line -> Arrays.stream(line.split("")).mapToInt(Integer::parseInt).toArray()).toArray(int[][]::new);
        System.out.println("Part 2: " + performSteps2(octopuses.length * octopuses[0].length)); // 354
    }

    // returns sum of all flashes
    private int performSteps1(int steps) {
        int sumOfFlashes = 0;
        for (int i = 1; i <= steps; i++) {
            sumOfFlashes += performStep();
        }
        return sumOfFlashes;
    }

    // returns step
    private int performSteps2(int targetFlashCount) {
        int step = 1;
        while (performStep() != targetFlashCount) {
            step++;
        }
        return step;
    }

    // returns number of flashes during this step
    private int performStep() {
        int flashCounter = 0;
        boolean someoneFlashed = false;

        // First, the energy level of each octopus increases by 1.
        for (int row = 0; row < octopuses.length; row++) {
            for (int col = 0; col < octopuses[row].length; col++) {
                octopuses[row][col]++;
                if (octopuses[row][col] == 10) {
                    someoneFlashed = true;
                    flashCounter++;
                }
            }
        }

        while (someoneFlashed) {
            int neighborsFlashes = letNeighborsFlash();
            someoneFlashed = neighborsFlashes > 0;
            if (someoneFlashed) {
                flashCounter += neighborsFlashes;
            }
        }

        return flashCounter;
    }

    private int letNeighborsFlash() {
        int flashCounter = 0;
        for (int row = 0; row < octopuses.length; row++) {
            for (int col = 0; col < octopuses[row].length; col++) {
                if (octopuses[row][col] == 10) {
                    List<Point> neighborsThatCouldFlash = getAllNeighbors(row, col).stream().filter(neighbor -> neighbor.getValue() > 0 && neighbor.getValue() < 10).collect(Collectors.toList());
                    for (Point neighbor : neighborsThatCouldFlash) {
                        octopuses[neighbor.getX()][neighbor.getY()]++;
                        if (octopuses[neighbor.getX()][neighbor.getY()] == 10) {
                            flashCounter++;
                        }
                    }
                    octopuses[row][col] = 0;
                }
            }
        }
        return flashCounter;
    }

    private List<Point> getAllNeighbors(int row, int col) {
        List<Point> neighbors = new ArrayList<>();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (x != 0 || y != 0) { // exclude original point
                    try {
                        neighbors.add(new Point(row + x, col + y, octopuses[row + x][col + y]));
                    } catch (ArrayIndexOutOfBoundsException ignored) {
                    }
                }
            }
        }
        return neighbors;
    }
}
