package com.burningcastle.adventofcode.year2021;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public interface Day {

    void run();

    default List<String> readFile(String fileName) {
        List<String> result = null;
        try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {
            result = in.lines().collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("I/O error for " + fileName);
        }
        return result;
    }

}
