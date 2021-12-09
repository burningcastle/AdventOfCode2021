package com.burningcastle.adventofcode.year2021.days;

import com.burningcastle.adventofcode.year2021.AbstractDay;

import java.util.*;
import java.util.stream.Collectors;

public class Day9 extends AbstractDay {

    public Day9() {
        super("src/resources/Day9.txt");
    }

    @Override
    public void run() {
        List<String> input = readLinesFromFile();
        int[][] heightmap = input.stream().map(line -> Arrays.stream(line.split("")).mapToInt(Integer::parseInt).toArray()).toArray(int[][]::new);

        // Part 1
        List<Point> lowPoints = getLowPoints(heightmap);
        int sumOfRiskLevels = lowPoints.stream().mapToInt(p -> p.value + 1).sum();
        System.out.println("Part 1: " + sumOfRiskLevels); // 539

        // Part 2
        List<Set<Point>> basins = getBasins(heightmap);
        int productOf3LargestBasinSizes = basins.stream().map(Set::size).sorted(Comparator.reverseOrder()).limit(3).reduce(1, Math::multiplyExact);
        System.out.println("Part 2: " + productOf3LargestBasinSizes); // 736920
    }

    private List<Set<Point>> getBasins(int[][] heightmap) {
        List<Point> lowPoints = getLowPoints(heightmap);
        return lowPoints.stream().map(p -> getBasinForPoint(heightmap, p)).collect(Collectors.toList());
    }

    private Set<Point> getBasinForPoint(int[][] heightmap, Point lowPoint) {
        List<Point> neighbors = getNeighborsInHeightmap(lowPoint, heightmap);
        List<Point> membersOfThisBasin = neighbors.stream().filter(neighbor -> neighbor.value > lowPoint.value && neighbor.value != 9).collect(Collectors.toList());
        Set<Point> basin = new HashSet<>(membersOfThisBasin);
        membersOfThisBasin.forEach(member -> basin.addAll(getBasinForPoint(heightmap, member)));
        basin.add(lowPoint);
        return basin;
    }

    private List<Point> getLowPoints(int[][] heightmap) {
        List<Point> result = new ArrayList<>();
        for (int row = 0; row < heightmap.length; row++) {
            for (int col = 0; col < heightmap[row].length; col++) {
                int value = heightmap[row][col];
                Point point = new Point(row, col, value);
                List<Point> neighbors = getNeighborsInHeightmap(point, heightmap);
                boolean isLowPoint = neighbors.stream().allMatch(neighbor -> point.value < neighbor.value);
                if (isLowPoint) {
                    result.add(point);
                }
            }
        }
        return result;
    }

    private List<Point> getNeighborsInHeightmap(Point point, int[][] heightmap) {
        List<Point> neighbors = new ArrayList<>();
        int x = point.x;
        int y = point.y;
        Point top = x > 0 ? new Point(x - 1, y, heightmap[x - 1][y]) : null;
        Point left = y > 0 ? new Point(x, y - 1, heightmap[x][y - 1]) : null;
        Point right = y < heightmap[x].length - 1 ? new Point(x, y + 1, heightmap[x][y + 1]) : null;
        Point down = x < heightmap.length - 1 ? new Point(x + 1, y, heightmap[x + 1][y]) : null;
        if (top != null) neighbors.add(top);
        if (left != null) neighbors.add(left);
        if (right != null) neighbors.add(right);
        if (down != null) neighbors.add(down);
        return neighbors;
    }

    static class Point {
        private final int x;
        private final int y;
        private final int value;

        public Point(int x, int y, int value) {
            this.x = x;
            this.y = y;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x && y == point.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
