package com.javaho.lexer;

public class LexerException extends RuntimeException {
    private final int line;
    private final int column;

    public LexerException(String message, int line, int column) {
        super("Lexical Error at line " + line + ", column " + column + ": " + message);
        this.line = line;
        this.column = column;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }
}
