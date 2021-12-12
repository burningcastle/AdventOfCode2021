package com.burningcastle.adventofcode.year2021.util;

import java.util.Objects;

/**
 * A two-dimensional point with a value
 */
public class Point {
    private final int x;
    private final int y;
    private final int value;

    public Point(int x, int y, int value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        this.value = 0;
    }

    // create point from String e.g. "51,119"
    public Point(String pointAsString) {
        String[] coordinates = pointAsString.split(",");
        this.x = Integer.parseInt(coordinates[0]);
        this.y = Integer.parseInt(coordinates[1]);
        this.value = 0;
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getValue() {
        return value;
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
