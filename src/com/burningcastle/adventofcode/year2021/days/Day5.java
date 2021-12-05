package com.burningcastle.adventofcode.year2021.days;

import com.burningcastle.adventofcode.year2021.AbstractDay;

import java.util.*;
import java.util.stream.Collectors;

public class Day5 extends AbstractDay {

    public Day5() {
        super("src/resources/Day5.txt");
    }

    @Override
    public void run() {
        List<String> input = readLinesFromFile();

        // get list of all lines (of hydrothermal vents); represented as an array of start and end point
        List<Point[]> allLines = input.stream().map(line -> Arrays.stream(line.split(" -> ")).map(Point::new).toArray(Point[]::new)).collect(Collectors.toList());

        // Part 1
        List<Point[]> horizontalAndVerticalLines = allLines.stream().filter(line -> line[0].x == line[1].x || line[0].y == line[1].y).collect(Collectors.toList());
        System.out.println("Part 1: " + getNumberOfMostDangerousAreas(horizontalAndVerticalLines)); // 5124

        // Part 2
        System.out.println("Part 2: " + getNumberOfMostDangerousAreas(allLines)); // 19771
    }

    // most dangerous areas = the points where at least two lines overlap
    private long getNumberOfMostDangerousAreas(List<Point[]> startingAndEndPointsOfLines) {
        List<List<Point>> linesWithAllPoints = startingAndEndPointsOfLines.stream().map(this::getAllPointsOnLine).collect(Collectors.toList());

        // add all points and their number of occurrence to a map
        Map<Point, Integer> occurrencesPerPoint = new HashMap<>();
        linesWithAllPoints.stream().flatMap(Collection::stream).forEach(point -> {
            int count = occurrencesPerPoint.getOrDefault(point, 0);
            occurrencesPerPoint.put(point, count + 1);
        });

        long numberOfPointsWhereLinesIntersect = occurrencesPerPoint.values().stream().filter(occurrenceCount -> occurrenceCount >= 2).count();
        return numberOfPointsWhereLinesIntersect;
    }

    // vertical, horizontal and diagonal with 45 degrees
    private List<Point> getAllPointsOnLine(Point[] points) {
        Point start = points[0];
        Point end = points[1];
        List<Point> result = new ArrayList<>();
        result.add(start);

        Point tmp = new Point(start.x, start.y);
        while (!tmp.equals(end)) {
            int newX = tmp.x;
            int newY = tmp.y;

            if (end.x - tmp.x < 0) {
                newX--;
            }
            if (end.x - tmp.x > 0) {
                newX++;
            }
            if (end.y - tmp.y < 0) {
                newY--;
            }
            if (end.y - tmp.y > 0) {
                newY++;
            }

            tmp = new Point(newX, newY);
            result.add(tmp);
        }

        return result;
    }


    static class Point {
        private final int x;
        private final int y;

        // create point from String e.g. "51,119"
        public Point(String pointAsString) {
            int indexOfSeparator = pointAsString.indexOf(",");
            this.x = Integer.parseInt(pointAsString.substring(0, indexOfSeparator));
            this.y = Integer.parseInt(pointAsString.substring(indexOfSeparator + 1));
        }

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
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
