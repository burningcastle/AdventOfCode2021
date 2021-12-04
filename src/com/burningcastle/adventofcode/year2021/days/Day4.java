package com.burningcastle.adventofcode.year2021.days;

import com.burningcastle.adventofcode.year2021.AbstractDay;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Day4 extends AbstractDay {

    public Day4() {
        super("src/resources/Day4.txt");
    }

    @Override
    public void run() {

        // process input
        List<String> input = readLinesFromFile();
        String[] numbersToDraw = input.get(0).split(",");
        List<String> allBoards = input.subList(2, input.size());
        List<BingoBoard> boards = splitIntoSublists(allBoards, 5).stream().map(BingoBoard::new).collect(Collectors.toList());

        // Part 1
        System.out.println("Part 1: " + runBingoGame(numbersToDraw, boards, true)); // 63424

        // Part 2
        System.out.println("Part 2: " + runBingoGame(numbersToDraw, boards, false)); // 23541
    }

    private int runBingoGame(String[] numbersToDraw, List<BingoBoard> boards, boolean firstBoardWins) {
        int lastDrawnNumber = 0;
        int winningBoardsScore = 0;
        Set<BingoBoard> boardsThatAlreadyWon = new HashSet<>();

        drawingOfNumbers:
        for (String drawnNumber : numbersToDraw) {

            // mark new number on all bingo boards
            boards.forEach(board -> board.mark(drawnNumber));

            // check if someone has a bingo and if the game is over
            for (BingoBoard board : boards) {
                if (!boardsThatAlreadyWon.contains(board) && board.isBingo()) {
                    winningBoardsScore = board.getScore();
                    lastDrawnNumber = Integer.parseInt(drawnNumber);
                    if (firstBoardWins) {
                        break drawingOfNumbers;
                    } else {
                        boardsThatAlreadyWon.add(board);
                        // end game if the last board has a bingo
                        if (boardsThatAlreadyWon.size() == boards.size()) break drawingOfNumbers;
                    }
                }
            }
        }
        return lastDrawnNumber * winningBoardsScore;
    }

    private List<List<String>> splitIntoSublists(List<String> list, int chunkSize) {
        AtomicInteger counter = new AtomicInteger();
        Collection<List<String>> boards = list.stream()
                .filter(line -> !line.equals("")) // ignore empty lines
                .collect(Collectors.groupingBy(it -> counter.getAndIncrement() / chunkSize))
                .values();
        return new ArrayList<>(boards);
    }

    static class BingoBoard {

        private final List<List<String[]>> rows;

        public BingoBoard(List<String> bingoBoardAsStringList) {
            rows = bingoBoardAsStringList.stream()
                    .map(line -> Arrays.stream(line.split(" "))
                            .filter(number -> !number.isEmpty())
                            .map(number -> new String[]{number, ""}) // Tuple to save the number and an info if it matched ("X" or "")
                            .collect(Collectors.toList()))
                    .collect(Collectors.toList());
        }

        public boolean isBingo() {
            // check row
            for (List<String[]> row : rows) {
                int numberOfMatches = row.stream().reduce(0, (acc, field) -> field[1].equals("X") ? acc + 1 : acc, Integer::sum);
                if (row.size() == numberOfMatches) {
                    return true;
                }
            }

            // check column
            int[] marksPerColumn = new int[rows.size()];
            for (List<String[]> row : rows) {
                for (int col = 0; col < row.size(); col++) {
                    if (row.get(col)[1].equals("X"))
                        marksPerColumn[col]++;
                }
            }
            for (int marks : marksPerColumn) {
                if (marks == rows.size())
                    return true;
            }

            return false;
        }

        public void mark(String drawnNumber) {
            for (List<String[]> row : rows) {
                for (String[] field : row) {
                    if (field[0].equals(drawnNumber))
                        field[1] = "X";
                }
            }
        }

        public int getScore() {
            int sumOfAllUnmarkedNumbers = 0;
            for (List<String[]> row : rows) {
                for (String[] field : row) {
                    if (field[1].equals("")) {
                        sumOfAllUnmarkedNumbers += Integer.parseInt(field[0]);
                    }
                }
            }
            return sumOfAllUnmarkedNumbers;
        }
    }


}
