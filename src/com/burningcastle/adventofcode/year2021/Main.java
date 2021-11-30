package com.burningcastle.adventofcode.year2021;

import java.lang.reflect.InvocationTargetException;

public class Main {

    public static void main(String[] args) {
//        new Day1().run();

        // ... or call all days with black magic
        for (int i = 1; i <= 25; i++) {
            System.out.println("~~Day " + i + "~~");
            try {
                Day day = (Day) Class.forName("com.burningcastle.adventofcode.year2021.days.Day" + i).getDeclaredConstructor().newInstance();
                day.run();
            } catch (ClassNotFoundException e) {
                System.out.println("Day " + i + " not yet implemented.");
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                System.out.println("A black magic error occurred while trying to run Day " + i);
            }
            System.out.println();
        }
    }
}
