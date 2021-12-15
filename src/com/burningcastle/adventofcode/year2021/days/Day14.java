package com.burningcastle.adventofcode.year2021.days;

import com.burningcastle.adventofcode.year2021.AbstractDay;

import java.util.*;
import java.util.stream.Collectors;

public class Day14 extends AbstractDay {

    private Map<String, String> insertions;

    public Day14() {
        super("src/resources/Day14.txt");
    }

    @Override
    public void run() {
        List<String> input = readLinesFromFile();
        List<String> start = Arrays.asList(input.get(0).split(""));
        insertions = input.subList(2, input.size()).stream().map(line -> line.split(" -> ")).collect(Collectors.toMap(pair -> pair[0], pair -> pair[1]));

        // Part 1
        System.out.println("Part 1: " + part1Naive(start, 10)); // 2584

        // Part 2
        System.out.println("Part 2: " + part2optimised(start, 40)); // 3816397135460
    }

    private long part2optimised(List<String> start, int steps) {
        Map<String, Long> pairsWithAmounts = new HashMap<>();
        // initialize the map: put all pairs and number of their occurrence in a map
        // Template = NNCB
        // NN -> 1
        // NC -> 1
        // CB -> 1
        for (int i = 0; i < start.size() - 1; i++) {
            String first = start.get(i);
            String second = start.get(i + 1);
            String newKey = first + second;
            pairsWithAmounts.put(newKey, pairsWithAmounts.getOrDefault(newKey, 0L) + 1);
        }

        // perform steps
        for (int step = 1; step <= steps; step++) {
            pairsWithAmounts = performStep(pairsWithAmounts);
            // After Step 1:
            //"BC" -> 1
            //"NB" -> 1
            //"NC" -> 1
            //"CH" -> 1
            //"HB" -> 1
            //"CN" -> 1

            // After Step 2:
            //"BB" -> 2
            //"CC" -> 1
            //"BC" -> 2
            //"NB" -> 2
            //"BH" -> 1
            //"HC" -> 1
            //"CN" -> 1
            //"CB" -> 2

            // etc...
        }

        // Count each element with occurrence
        Map<String, Long> counts = new HashMap<>();
        pairsWithAmounts.forEach((pair, occurrence) -> {
            String elem1 = pair.substring(0,1);
            String elem2 = pair.substring(1);
            counts.put(elem1, counts.getOrDefault(elem1, 0L) + occurrence);
            counts.put(elem2, counts.getOrDefault(elem2, 0L) + occurrence);
        });

        // IMPORTANT: add first and last element (aka character) to make the count work
        // -> the first and last element of the starting string are also a pair that is not yet completely included in the count!
        String first = start.get(0);
        String last = start.get(start.size() - 1);
        counts.put(first, counts.getOrDefault(first, 0L) + 1L);
        counts.put(last, counts.getOrDefault(last, 0L) + 1L);

        List<Long> values = new ArrayList<>(counts.values()).stream()
                .map(count -> count / 2) // since we counted pairs (i.e. each element is counted twice) we have to divide the count by 2
                .collect(Collectors.toList());

        return getDifferenceOfMaxAndMin(values);
    }

    // for each pair in the map a new pair is created based on the 'instructions' input
    private Map<String, Long> performStep(Map<String, Long> pairs) {
        Map<String, Long> pairsWithAmounts = new HashMap<>();
        pairs.forEach((pair, occurrence) -> {
            String newElem = insertions.get(pair);
            String newPair1 = pair.charAt(0) + newElem;
            pairsWithAmounts.put(newPair1, pairsWithAmounts.getOrDefault(newPair1, 0L) + occurrence);
            String newPair2 = newElem + pair.charAt(1);
            pairsWithAmounts.put(newPair2, pairsWithAmounts.getOrDefault(newPair2, 0L) + occurrence);
        });
        return pairsWithAmounts;
    }

    private long part1Naive(List<String> start, int steps) {
        List<String> res = start;
        for (int step = 1; step <= steps; step++) {
            List<String> newRes = new ArrayList<>();
            // step
            for (int i = 0; i < res.size() - 1; i++) {
                String first = res.get(i);
                String second = res.get(i + 1);
                newRes.add(first);
                newRes.add(insertions.get(first + second));
                if (i == res.size() - 2) newRes.add(second);
            }
            res = newRes;
        }

        Map<String, Long> counts = res.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
        return getDifferenceOfMaxAndMin(new ArrayList<>(counts.values()));
    }

    private long getDifferenceOfMaxAndMin(List<Long> values) {
        List<Long> sortedValues = values.stream().sorted().collect(Collectors.toList());
        long max = sortedValues.get(sortedValues.size() - 1);
        long min = sortedValues.get(0);
        return max - min;
    }
}
