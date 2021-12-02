package com.burningcastle.adventofcode.year2021.days;

import com.burningcastle.adventofcode.year2021.AbstractDay;

import java.util.List;
import java.util.stream.Collectors;

public class Day2 extends AbstractDay {

    public Day2() {
        super("src/resources/Day2.txt");
    }

    @Override
    public void run() {
        List<String[]> input = readLinesFromFile().stream().map(x -> x.split(" ")).collect(Collectors.toList());

        // Part 1
        System.out.println("Part 1: " + part1(input)); // 1815044

        // Part 2
        System.out.println("Part 2: " + part2(input)); // 1739283308
    }

    private int part1(List<String[]> input) {
        int horizontalPosition = 0;
        int depth = 0;

        for (String[] command : input) {

            String direction = command[0];
            int value = Integer.parseInt(command[1]);

            if (direction.equals("forward")) {
                horizontalPosition += value;
            }
            if (direction.equals("down")) {
                depth += value;
            }
            if (direction.equals("up")) {
                depth -= value;
            }
        }
        return horizontalPosition * depth;
    }

    private int part2(List<String[]> input) {
        int horizontalPosition = 0;
        int depth = 0;
        int aim = 0;

        for (String[] command : input) {

            String direction = command[0];
            int value = Integer.parseInt(command[1]);

            if (direction.equals("forward")) {
                horizontalPosition += value;
                depth += aim * value;
            }
            if (direction.equals("down")) {
                aim += value;
            }
            if (direction.equals("up")) {
                aim -= value;
            }
        }

        return horizontalPosition * depth;
    }

}
