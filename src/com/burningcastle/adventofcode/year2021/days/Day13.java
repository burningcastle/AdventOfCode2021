package com.burningcastle.adventofcode.year2021.days;

import com.burningcastle.adventofcode.year2021.AbstractDay;
import com.burningcastle.adventofcode.year2021.util.Pair;
import com.burningcastle.adventofcode.year2021.util.Point;

import java.util.*;
import java.util.stream.Collectors;

public class Day13 extends AbstractDay {

    private final Set<Point> points = new HashSet<>();

    public Day13() {
        super("src/resources/Day13.txt");
    }

    @Override
    public void run() {
        List<String> input = readLinesFromFile();
        points.addAll(input.subList(0, input.indexOf("")).stream().map(Point::new).collect(Collectors.toSet()));
        List<String> instructions = input.subList(input.indexOf("")+1,input.size());

        // Part 1
        fold(instructions.subList(0,1));
        System.out.println("Part 1: " + points.size()); // 682

        // Part 2
        fold(instructions.stream().skip(1).collect(Collectors.toList()));
        System.out.println("Part 2: ");
        draw(); // FAGURZHE
    }

    private void fold(List<String> instructions) {
        instructions.forEach(instruction -> {
            int lineNumber = Integer.parseInt(instruction.split("=")[1]);
            if(instruction.contains("x")) {
                foldLeft(lineNumber);
            } else {
                foldUp(lineNumber);
            }
        });
    }

    private void draw() {
        int maxX = points.stream().mapToInt(Point::getX).max().orElse(0);
        int maxY = points.stream().mapToInt(Point::getY).max().orElse(0);
        for (int y = 0; y <= maxY; y++) {
            for (int x = 0; x <= maxX; x++) {
                System.out.print(points.contains(new Point(x,y)) ?  "#" : ".");
            }
            System.out.println();
        }
    }

    private void foldUp(int lineNumber) {
        Set<Point> newPoints = new HashSet<>();
        points.forEach(p -> {
            if(p.getY() > lineNumber) {
                int diff = p.getY() - lineNumber;
                int newY = lineNumber - diff;
                newPoints.add(new Point(p.getX(),newY));
            }
        });
        points.addAll(newPoints);
        points.removeIf(p -> p.getY() > lineNumber);
    }

    private void foldLeft(int lineNumber) {
        Set<Point> newPoints = new HashSet<>();
        points.forEach(p -> {
            if(p.getX() > lineNumber) {
                int diff = p.getX() - lineNumber;
                int newX = lineNumber - diff;
                newPoints.add(new Point(newX,p.getY()));
            }
        });
        points.addAll(newPoints);
        points.removeIf(p -> p.getX() > lineNumber);
    }
}
