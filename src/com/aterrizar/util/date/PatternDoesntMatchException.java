package com.aterrizar.util.date;

public class PatternDoesntMatchException extends RuntimeException {
    public PatternDoesntMatchException() {
        super("The available patterns doesn't match with the provided date.");
    }
}
