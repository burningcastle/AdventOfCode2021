package com.burningcastle.adventofcode.year2021.days;


import com.burningcastle.adventofcode.year2021.AbstractDay;
import com.burningcastle.adventofcode.year2021.util.Point;

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
        List<Point[]> horizontalAndVerticalLines = allLines.stream().filter(line -> line[0].getX() == line[1].getX() || line[0].getY() == line[1].getY()).collect(Collectors.toList());
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

        Point tmp = new Point(start.getX(), start.getY());
        while (!tmp.equals(end)) {
            int newX = tmp.getX();
            int newY = tmp.getY();

            if (end.getX() - tmp.getX() < 0) {
                newX--;
            }
            if (end.getX() - tmp.getX() > 0) {
                newX++;
            }
            if (end.getY() - tmp.getY() < 0) {
                newY--;
            }
            if (end.getY() - tmp.getY() > 0) {
                newY++;
            }

            tmp = new Point(newX, newY);
            result.add(tmp);
        }

        return result;
    }

}
