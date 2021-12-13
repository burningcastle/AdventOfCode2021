package com.burningcastle.adventofcode.year2021.days;

import com.burningcastle.adventofcode.year2021.AbstractDay;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// very messy and inefficient. needs refactoring!
public class Day12 extends AbstractDay {

    private final Map<String, List<String>> nodesWithEdges = new HashMap<>();

    public Day12() {
        super("src/resources/Day12.txt");
    }

    @Override
    public void run() {
        List<String> input = readLinesFromFile();
        // Map with all nodes and their edges
        input.forEach(line -> {
            String[] connection = line.split("-");
            List<String> x = nodesWithEdges.getOrDefault(connection[0], new ArrayList<>());
            x.add(connection[1]);
            nodesWithEdges.put(connection[0], x);
            List<String> y = nodesWithEdges.getOrDefault(connection[1], new ArrayList<>());
            y.add(connection[0]);
            nodesWithEdges.put(connection[1], y);
        });

        // Part 1
        System.out.println("Part 1: " + calculatePaths(false).size()); // 4338

        // Part 2
        System.out.println("Part 2: " + calculatePaths(true).size()); // 114189
    }

    private Set<List<String>> calculatePaths(boolean allowOneSmallCaveTwice) {
        List<String> firstPath = List.of("start");
        Stream<List<String>> allPathsWithoutFirstPath = calculatePaths(Set.of(firstPath), allowOneSmallCaveTwice).stream().filter(path -> !path.equals(firstPath));
        return allPathsWithoutFirstPath.collect(Collectors.toSet());
    }

    private Set<List<String>> calculatePaths(Set<List<String>> paths, boolean allowOneSmallCaveTwice) {
        Set<List<String>> resultPaths = new HashSet<>();
        for (List<String> path : paths) {
            Set<List<String>> expandedPaths = new HashSet<>();
            // get all potential connections and expand
            List<String> connections = nodesWithEdges.get(path.get(path.size() - 1));
            connections.forEach(cave -> {
                if (!cave.equals("start") && // don't add 'start' again
                        !path.get(path.size() - 1).equals("end") && // don't continue if the last step was 'end'
                        (isBigCave(cave) || !path.contains(cave) || // PART 1 - small caves can only be visited once
                                (allowOneSmallCaveTwice && smallCaveCanBeAdded(path)))  // PART 2 - OR one small cave can be visited twice (others once)
                ) {

                    List<String> newPath = new ArrayList<>(path);
                    newPath.add(cave);
                    expandedPaths.add(newPath);
                }
            });
            resultPaths.addAll(calculatePaths(expandedPaths, allowOneSmallCaveTwice));
            resultPaths.removeIf(p -> !p.get(p.size() - 1).equals("end"));
        }
        resultPaths.addAll(paths);
        return resultPaths;
    }

    private boolean smallCaveCanBeAdded(List<String> path) {
        List<String> existingSmallCaves = path.stream().filter(cave -> !isBigCave(cave)).collect(Collectors.toList());
        HashSet<String> uniqueSmallCaves = new HashSet<>(existingSmallCaves);
        return existingSmallCaves.isEmpty() || uniqueSmallCaves.size() == existingSmallCaves.size();
    }

    private boolean isBigCave(String cave) {
        return cave.toUpperCase().equals(cave);
    }
}
