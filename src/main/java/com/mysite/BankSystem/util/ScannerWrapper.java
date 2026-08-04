package com.mysite.BankSystem.util;

import java.util.Scanner;
import java.util.function.Function;

public class ScannerWrapper implements AutoCloseable {
    private final Scanner scanner;
    private static final ScannerWrapper INSTANCE;

    public static ScannerWrapper getInstance() {
        return INSTANCE;
    }

    static {

        INSTANCE = new ScannerWrapper();
    }

    private ScannerWrapper() {
        scanner = new Scanner(System.in);
    }


    public <T> T getUserInput(String message, Function<String, T> converter) {
        System.out.println(message);
        return converter.apply(scanner.nextLine());

    }

    @Override
    public void close() {
        scanner.close();

    }
}
