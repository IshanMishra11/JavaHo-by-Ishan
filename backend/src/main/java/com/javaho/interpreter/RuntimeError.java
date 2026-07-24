package com.javaho.interpreter;

public class RuntimeError extends RuntimeException {
    private final int line;

    public RuntimeError(String message, int line) {
        super("Runtime Error at line " + line + ": " + message);
        this.line = line;
    }

    public int getLine() {
        return line;
    }
}
