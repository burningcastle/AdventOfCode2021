package com.burningcastle.adventofcode.year2021;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractDay implements Day {

    private final String fileName;

    protected AbstractDay(String fileName) {
        this.fileName = fileName;
    }

    protected List<String> readLinesFromFile() {
        List<String> result = null;
        try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {
            result = in.lines().collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("I/O error for " + fileName);
        }
        return result;
    }

    @Override
    public void runTimed() {
        long startTime = System.nanoTime();
        run();
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;
        System.out.println("-> " + duration + "ms");
    }
}
