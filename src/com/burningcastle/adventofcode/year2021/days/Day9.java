package com.burningcastle.adventofcode.year2021.days;

import com.burningcastle.adventofcode.year2021.AbstractDay;
import com.burningcastle.adventofcode.year2021.util.Point;

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
        int sumOfRiskLevels = lowPoints.stream().mapToInt(p -> p.getValue() + 1).sum();
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
        List<Point> membersOfThisBasin = neighbors.stream().filter(neighbor -> neighbor.getValue() > lowPoint.getValue() && neighbor.getValue() != 9).collect(Collectors.toList());
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
                boolean isLowPoint = neighbors.stream().allMatch(neighbor -> point.getValue() < neighbor.getValue());
                if (isLowPoint) {
                    result.add(point);
                }
            }
        }
        return result;
    }

    private List<Point> getNeighborsInHeightmap(Point point, int[][] heightmap) {
        List<Point> neighbors = new ArrayList<>();
        int x = point.getX();
        int y = point.getY();
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

}
