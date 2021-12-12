package com.burningcastle.adventofcode.year2021.days;

import com.burningcastle.adventofcode.year2021.AbstractDay;
import com.burningcastle.adventofcode.year2021.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

// needs refactoring!
public class Day10 extends AbstractDay {

    private final Map<String, String> brackets = new HashMap<>();
    private final Map<String, Integer> syntaxErrorPoints = new HashMap<>();
    private final Map<String, Integer> autocompletePoints = new HashMap<>();

    public Day10() {
        super("src/resources/Day10.txt");
        brackets.put("(", ")");
        brackets.put("[", "]");
        brackets.put("{", "}");
        brackets.put("<", ">");

        // Part 1
        syntaxErrorPoints.put(")", 3);
        syntaxErrorPoints.put("]", 57);
        syntaxErrorPoints.put("}", 1197);
        syntaxErrorPoints.put(">", 25137);

        // Part 2
        autocompletePoints.put(")", 1);
        autocompletePoints.put("]", 2);
        autocompletePoints.put("}", 3);
        autocompletePoints.put(">", 4);
    }

    @Override
    public void run() {
        List<String> input = readLinesFromFile();
        List<Pair<String, List<String>>> allSyntaxErrors = input.stream().map(this::getSyntaxErrors).collect(Collectors.toList());

        // Part 1
        int totalSyntaxErrorScore = allSyntaxErrors.stream()
                .map(Pair::getFirst)
                .filter(y -> !y.isEmpty())
                .mapToInt(syntaxErrorPoints::get)
                .sum();
        System.out.println("Part 1: " + totalSyntaxErrorScore); // 392043

        // Part 2
        List<Long> sortedAutoCompleteScores = allSyntaxErrors.stream()
                .filter(result -> result.getFirst().isEmpty())
                .map(Pair::getSecond)
                .map(missingBrackets -> missingBrackets.stream()
                        .mapToLong(autocompletePoints::get)
                        .reduce(0, (acc, val) -> acc * 5 + val))
                .sorted()
                .collect(Collectors.toList());
        Long middleScore = sortedAutoCompleteScores.get(sortedAutoCompleteScores.size() / 2);
        System.out.println("Part 2: " + middleScore); // 1605968119
    }

    private Pair<String, List<String>> getSyntaxErrors(String line) {
        List<String> expectedClosingBrackets = new ArrayList<>();
        for (String current : line.split("")) {
            boolean isOpeningBracket = brackets.containsKey(current);

            // a new chunk -> update next expected closing bracket
            if (isOpeningBracket)
                expectedClosingBrackets.add(0, brackets.get(current));

            // error case for Part 1 --> current value is a closing bracket but not the expected one
            if (!isOpeningBracket && !current.equals(expectedClosingBrackets.get(0)))
                return new Pair<>(current, expectedClosingBrackets);

            // closing a chunk -> current value is the next expected closing bracket
            if (current.equals(expectedClosingBrackets.get(0)))
                expectedClosingBrackets.remove(0);
        }
        return new Pair<>("", expectedClosingBrackets);
    }
}
