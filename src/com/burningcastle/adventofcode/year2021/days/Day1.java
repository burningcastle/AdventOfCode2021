package com.burningcastle.adventofcode.year2021.days;

import com.burningcastle.adventofcode.year2021.AbstractDay;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day1 extends AbstractDay {

    public Day1() {
        super("src/resources/Day1.txt");
    }

    @Override
    public void run() {
        List<Integer> input = readFileNumbers();

        // Part 1
        int count = countIncreases(input);
        System.out.println("Part 1: " + count); // 1715

        // Part 2
        count = countIncreases(buildWindows(input));
        System.out.println("Part 2: " + count); // 1739
    }

    private int countIncreases(List<Integer> input) {
        int largerCounter = 0;
        for (int i = 0; i < input.size() - 1; i++) {
            int current = input.get(i);
            int next = input.get(i + 1);
//            System.out.println(next + (next > current ? " (increased)" : " (decreased)"));
            if (next > current) largerCounter++;
        }
        return largerCounter;
    }

    private List<Integer> buildWindows(List<Integer> input) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < input.size() - 2; i++) {
            result.add(input.get(i) + input.get(i + 1) + input.get(i + 2));
        }
        return result;
    }

    private List<Integer> readFileNumbers() {
        return readFile().stream().map(Integer::parseInt).collect(Collectors.toList());
    }

}
