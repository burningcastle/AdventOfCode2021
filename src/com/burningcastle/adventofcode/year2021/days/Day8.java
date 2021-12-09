package com.burningcastle.adventofcode.year2021.days;

import com.burningcastle.adventofcode.year2021.AbstractDay;

import java.util.*;
import java.util.stream.Collectors;

// needs refactoring!
public class Day8 extends AbstractDay {

    private final ArrayList<List<String>> segmentsPerDigit = new ArrayList<>();
    private final List<String> allSegments = List.of("a", "b", "c", "d", "e", "f", "g");

    public Day8() {
        super("src/resources/Day8.txt");

        // original digits have this format

        //  aaaa
        // b    c
        // b    c
        //  dddd
        // e    f
        // e    f
        //  gggg

        // 0 (all segments except for d)
        this.segmentsPerDigit.add(List.of("a", "b", "c", "e", "f", "g"));
        // 1
        this.segmentsPerDigit.add(List.of("c", "f"));
        // 2
        this.segmentsPerDigit.add(List.of("a", "c", "d", "e", "g"));
        // 3
        this.segmentsPerDigit.add(List.of("a", "c", "d", "f", "g"));
        // 4
        this.segmentsPerDigit.add(List.of("b", "c", "d", "f"));
        // 5
        this.segmentsPerDigit.add(List.of("a", "b", "d", "f", "g"));
        // 6
        this.segmentsPerDigit.add(List.of("a", "b", "d", "e", "f", "g"));
        // 7
        this.segmentsPerDigit.add(List.of("a", "c", "f"));
        // 8 (all segments)
        this.segmentsPerDigit.add(allSegments);
        // 9
        this.segmentsPerDigit.add(List.of("a", "b", "c", "d", "f", "g"));
    }

    @Override
    public void run() {
        List<String> input = readLinesFromFile();
        List<String[]> signals = input.stream().map(line -> line.split(" \\| ")).collect(Collectors.toList());

        // Part 1
        System.out.println("Part 1: " + appearancesOf1478(signals)); // 390

        // Part 2
        System.out.println("Part 2: " + sumOfAllDigits(signals)); // 1011785
    }

    private long sumOfAllDigits(List<String[]> signals) {
        return signals.stream().mapToLong(line -> Long.parseLong(getDigitsAsString(line))).sum();
    }

    private String getDigitsAsString(String[] line) {

        List<List<String>> uniqueSignalPatternsFromInput = Arrays.stream(line[0].split(" ")).map(x -> Arrays.asList(x.split(""))).collect(Collectors.toList());
        List<List<String>> digitsFromInput = Arrays.stream(line[1].split(" ")).map(digit -> Arrays.stream(digit.split("")).sorted().collect(Collectors.toList())).collect(Collectors.toList());

        // initialize candidates
        // key: original segment -> values: all candidates
        Map<String, List<String>> candidatesPerSegment = new HashMap<>();
        for (String segment : this.allSegments) {
            candidatesPerSegment.put(segment, new ArrayList<>(this.allSegments));
        }

        // identify which segment is which by reducing the candidates
        for (int length = 2; length < 7; length++) {
            int numberOfSegments = length;

            // find what all signal patterns from input with the same length have in common
            List<List<String>> inputSignalsWithLength = uniqueSignalPatternsFromInput.stream().filter(signal -> signal.size() == numberOfSegments).collect(Collectors.toList());
            List<String> commonSegmentsOfInput = intersection(inputSignalsWithLength);

            // find what all "real signals" with the same length have in common
            List<List<String>> realSignalsWithLength = this.segmentsPerDigit.stream().filter(segmentsOfDigit -> segmentsOfDigit.size() == numberOfSegments).collect(Collectors.toList());
            List<String> commonSegmentsOfRealSignals = intersection(realSignalsWithLength);

            // ignore the already identified segments (i.e. only 1 candidate left)
            List<Map.Entry<String, List<String>>> alreadyIdentifiedSegments = candidatesPerSegment.entrySet().stream().filter(x -> x.getValue().size() == 1).collect(Collectors.toList());
            commonSegmentsOfInput.removeAll(alreadyIdentifiedSegments.stream().map(e -> e.getValue().get(0)).collect(Collectors.toList()));
            commonSegmentsOfRealSignals.removeAll(alreadyIdentifiedSegments.stream().map(Map.Entry::getKey).collect(Collectors.toList()));

            // reduce candidates list
            reduceCandidates(candidatesPerSegment, commonSegmentsOfInput, commonSegmentsOfRealSignals);
        }

        // create translation
        List<List<String>> segmentsPerDigitUsingTheNewWiring = this.segmentsPerDigit.stream()
                .map(realDigit -> realDigit.stream()
                        .map(realSegment -> candidatesPerSegment.get(realSegment).get(0))
                        .sorted()
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());

        // translate each digit and join together as String
        return digitsFromInput.stream().map(segmentsPerDigitUsingTheNewWiring::indexOf).map(it -> Integer.toString(it)).collect(Collectors.joining());
    }

    private void reduceCandidates(Map<String, List<String>> candidatesPerSegment, List<String> segmentsFromInput, List<String> realSegments) {
        // Example:  reduceCandidates(candidatesList, {"d", "b"}, {"c", "f"})
        // we know that d and b in the signals from the input are in reality c and f (--> 1)

        // loop through all real segments from a to g (and update their candidates)
        for (Map.Entry<String, List<String>> segmentWithCandidates : candidatesPerSegment.entrySet()) {
            if (!realSegments.contains(segmentWithCandidates.getKey())) {
                // loop case: it is not c and not f
                // --> delete d and b from the candidates lists
                segmentWithCandidates.getValue().removeAll(segmentsFromInput);
            } else {
                // loop case: it is c or f
                // --> c and f might each be d or b
                // --> delete every other possibility (i.e. a,c,e,f,g) from their candidate list
                List<String> candidates = segmentWithCandidates.getValue();
                List<String> allSegmentsWithOutCandidates = subtract(this.allSegments, segmentsFromInput);
                candidates.removeAll(allSegmentsWithOutCandidates);
            }
        }
    }

    private List<String> intersection(List<List<String>> list) {
        HashSet<String> result = new HashSet<>(list.get(0));
        list.forEach(result::retainAll);
        return new ArrayList<>(result);
    }

    private List<String> subtract(List<String> list, List<String> subtrahend) {
        return list.stream().filter(segment -> !subtrahend.contains(segment)).collect(Collectors.toList());
    }

    private long appearancesOf1478(List<String[]> signals) {
        List<String[]> digitOutputs = signals.stream().map(s -> s[1].split(" ")).collect(Collectors.toList());
        return digitOutputs.stream().flatMap(Arrays::stream).filter(s ->
                s.length() == 2 || s.length() == 3 || s.length() == 4 || s.length() == 7
        ).count();
    }
}
