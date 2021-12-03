package com.burningcastle.adventofcode.year2021.days;

import com.burningcastle.adventofcode.year2021.AbstractDay;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

// TODO refactor a bit
public class Day3 extends AbstractDay {

    public Day3() {
        super("src/resources/Day3.txt");
    }

    @Override
    public void run() {
        List<String> input = readLinesFromFile();

        // Part 1
        System.out.println("Part 1: " + part1(input)); // 775304

        // Part 2
        System.out.println("Part 2: " + part2(input)); // 1370737
    }

    private int part1(List<String> input) {
        char[] mostCommonBitsPerColumn = getResultingBitsPerColumnBasedOnCriteria(input, this::mostOccurrences);

        // gamma rate
        String gammaRateBinary = new String(mostCommonBitsPerColumn);
        int gammaRateDecimal = Integer.parseInt(gammaRateBinary, 2);

        // epsilon rate (= inverted gamma rate)
        String invertedGammaRate = gammaRateBinary.replace('0', '2').replace('1', '0').replace('2', '1');
        int epsilonRateDecimal = Integer.parseInt(invertedGammaRate, 2);

        return gammaRateDecimal * epsilonRateDecimal;
    }

    private int part2(List<String> input) {
        String oxygenGeneratorRating = calculateGasRating(input, this::mostOccurrences);
        String co2ScrubberRating = calculateGasRating(input, this::leastOccurrences);

        int oxygenDecimal = Integer.parseInt(oxygenGeneratorRating, 2);
        int co2Decimal = Integer.parseInt(co2ScrubberRating, 2);

        return oxygenDecimal * co2Decimal;
    }

    // gamma rate and oxygen generator rating: determine the most common value in the column (0 or 1) and keep it
    private char mostOccurrences(int numberOfOneBits, int threshold) {
        if (numberOfOneBits >= threshold) {
            return '1';
        } else {
            return '0';
        }
    }

    // CO2 scrubber rating: determine the least common value (0 or 1) and keep it
    private char leastOccurrences(int numberOfOneBits, int threshold) {
        if (numberOfOneBits >= threshold) {
            return '0';
        } else {
            return '1';
        }
    }

    // needs refactoring!
    private String calculateGasRating(List<String> input, BiFunction<Integer, Integer, Character> bitCriteria) {
        List<String> filteredInput = input;
        int column = 0;
        while (filteredInput.size() > 1) {
            char[] mostCommonChars = getResultingBitsPerColumnBasedOnCriteria(filteredInput, bitCriteria);
            int currentColumn = column;
            filteredInput = filteredInput.stream().filter(line -> line.charAt(currentColumn) == mostCommonChars[currentColumn]).collect(Collectors.toList());
            column++;
        }
        return filteredInput.get(0);
    }

    private char[] getResultingBitsPerColumnBasedOnCriteria(List<String> input, BiFunction<Integer, Integer, Character> bitCriteria) {
        int[] countOfOnes = countOnesPerColumn(input);
        char[] result = new char[countOfOnes.length];
        int threshold = (input.size() + 1) / 2;
        for (int col = 0; col < countOfOnes.length; col++) {
            int numberOfOneBits = countOfOnes[col];
            result[col] = bitCriteria.apply(numberOfOneBits, threshold);
        }
        return result;
    }

    private int[] countOnesPerColumn(List<String> input) {
        int[] result = new int[input.get(0).length()];
        for (String line : input) {
            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) == '1')
                    result[i]++;
            }
        }
        return result;
    }

}
