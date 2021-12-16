package com.burningcastle.adventofcode.year2021.days;

import com.burningcastle.adventofcode.year2021.AbstractDay;
import com.burningcastle.adventofcode.year2021.util.Point;

import java.util.*;

// not beautiful but ok
public class Day15 extends AbstractDay {

    public Day15() {
        super("src/resources/Day15.txt");
    }

    @Override
    public void run() {
        List<String> input = readLinesFromFile();
        int[][] cave = input.stream().map(line -> Arrays.stream(line.split("")).mapToInt(Integer::parseInt).toArray()).toArray(int[][]::new);

        // Part 1
        System.out.println("Part 1: " + dijkstra(cave)); // 696

        // Part 2
        int[][] hugeCave = buildHugeCave(cave);
        System.out.println("Part 2: " + dijkstra(hugeCave)); // 2952 (slow processing, takes ~4s)
    }

    private int dijkstra(int[][] cave) {
        int result = 0;
        Map<Point, Integer> nodeScores = new HashMap<>();
        Map<Point, Integer> unvisited = new HashMap<>();
        Set<Point> visited = new HashSet<>();
        int squareSize = cave.length - 1;
        Point target = new Point(squareSize, squareSize, cave[squareSize][squareSize]);

        // define start
        Point start = new Point(0, 0, cave[0][0]);
        nodeScores.put(start, 0);
        unvisited.put(start, 0);

        boolean targetReached = false;
        while (!targetReached) {

            Map.Entry<Point, Integer> unvisitedNodeWithLowestScore = unvisited.entrySet().stream().reduce((acc, elem) -> acc.getValue() < elem.getValue() ? acc : elem).get();
            int minScore = unvisitedNodeWithLowestScore.getValue();
            Point nodeWithLowestScore = unvisitedNodeWithLowestScore.getKey();
            List<Point> neighbors = getNeighbors(nodeWithLowestScore, cave);

            for (Point neighbor : neighbors) {
                int newNeighborScore = minScore + cave[neighbor.getX()][neighbor.getY()];
                int oldNeighborScore = nodeScores.getOrDefault(neighbor, Integer.MAX_VALUE);
                int newRisk = Math.min(newNeighborScore, oldNeighborScore);
                if (neighbor.equals(target)) {
                    result = newRisk;
                    targetReached = true;
                }

                nodeScores.put(neighbor, newRisk);
                if (!visited.contains(neighbor)) {
                    unvisited.put(neighbor, newRisk);
                }

            }
            visited.add(nodeWithLowestScore);
            unvisited.remove(nodeWithLowestScore);
        }
        return result;
    }

    private int[][] buildHugeCave(int[][] cave) {
        int[][] hugeCave = new int[cave.length * 5][cave.length * 5];
        for (int row = 0; row < hugeCave.length; row++) {
            int rowRepition = row / cave.length;
            for (int col = 0; col < hugeCave.length; col++) {
                int colRepition = col / cave.length;
                int newVal = cave[row % cave.length][col % cave.length] + colRepition + rowRepition;
                hugeCave[row][col] = newVal > 9 ? (newVal % 10) + 1 : newVal;
            }
        }
        return hugeCave;
    }

    private List<Point> getNeighbors(Point point, int[][] cave) {
        List<Point> neighbors = new ArrayList<>();
        int x = point.getX();
        int y = point.getY();
        Point top = x > 0 ? new Point(x - 1, y, cave[x - 1][y]) : null;
        Point left = y > 0 ? new Point(x, y - 1, cave[x][y - 1]) : null;
        Point right = y < cave[x].length - 1 ? new Point(x, y + 1, cave[x][y + 1]) : null;
        Point down = x < cave.length - 1 ? new Point(x + 1, y, cave[x + 1][y]) : null;
        if (top != null) neighbors.add(top);
        if (left != null) neighbors.add(left);
        if (right != null) neighbors.add(right);
        if (down != null) neighbors.add(down);
        return neighbors;
    }

}
