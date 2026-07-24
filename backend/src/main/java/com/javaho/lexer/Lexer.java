package com.javaho.lexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lexer {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;
    private int column = 1;
    private int startColumn = 1;

    private static final Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("rakh", TokenType.RAKH);
        keywords.put("dikha", TokenType.DIKHA);
        keywords.put("agar", TokenType.AGAR);
        keywords.put("nahin", TokenType.NAHIN);
        keywords.put("jabtak", TokenType.JABTAK);
        keywords.put("khatam", TokenType.KHATAM);
        keywords.put("sahi", TokenType.SAHI);
        keywords.put("galat", TokenType.GALAT);
    }

    public Lexer(String source) {
        this.source = source != null ? source : "";
    }

    public List<Token> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            startColumn = column;
            scanToken();
        }

        tokens.add(new Token(TokenType.EOF, "", null, line, column));
        return tokens;
    }

    private void scanToken() {
        char c = advance();
        switch (c) {
            case ' ':
            case '\r':
            case '\t':
                // Ignore whitespace
                break;
            case '\n':
                line++;
                column = 1;
                break;
            case '#':
                // Comment line: read until end of line
                while (peek() != '\n' && !isAtEnd()) {
                    advance();
                }
                break;
            case '+':
                addToken(TokenType.PLUS);
                break;
            case '-':
                addToken(TokenType.MINUS);
                break;
            case '*':
                addToken(TokenType.STAR);
                break;
            case '/':
                addToken(TokenType.SLASH);
                break;
            case '(':
                addToken(TokenType.LPAREN);
                break;
            case ')':
                addToken(TokenType.RPAREN);
                break;
            case '=':
                addToken(match('=') ? TokenType.EQUAL_EQUAL : TokenType.ASSIGN);
                break;
            case '!':
                if (match('=')) {
                    addToken(TokenType.NOT_EQUAL);
                } else {
                    throw new LexerException("Unexpected character '!' (did you mean '!='?)", line, startColumn);
                }
                break;
            case '<':
                addToken(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS);
                break;
            case '>':
                addToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);
                break;
            case '"':
                string();
                break;
            default:
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    throw new LexerException("Unexpected character '" + c + "'", line, startColumn);
                }
                break;
        }
    }

    private void string() {
        int strStartLine = line;
        int strStartCol = startColumn;
        StringBuilder sb = new StringBuilder();

        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') {
                line++;
                column = 1;
            }
            if (peek() == '\\' && peekNext() == '"') {
                advance(); // skip backslash
                sb.append('"');
                advance();
            } else {
                sb.append(advance());
            }
        }

        if (isAtEnd()) {
            throw new LexerException("Unterminated string literal", strStartLine, strStartCol);
        }

        // The closing quote
        advance();

        String value = sb.toString();
        addToken(TokenType.STRING, value);
    }

    private void number() {
        while (isDigit(peek())) {
            advance();
        }

        String text = source.substring(start, current);
        try {
            int value = Integer.parseInt(text);
            addToken(TokenType.NUMBER, value);
        } catch (NumberFormatException e) {
            throw new LexerException("Number format too large: " + text, line, startColumn);
        }
    }

    private void identifier() {
        while (isAlphaNumeric(peek())) {
            advance();
        }

        String text = source.substring(start, current);
        TokenType type = keywords.get(text);
        if (type == null) {
            type = TokenType.IDENTIFIER;
            addToken(type, text);
        } else if (type == TokenType.SAHI) {
            addToken(type, true);
        } else if (type == TokenType.GALAT) {
            addToken(type, false);
        } else {
            addToken(type);
        }
    }

    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;

        current++;
        column++;
        return true;
    }

    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
               (c >= 'A' && c <= 'Z') ||
                c == '_';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private char advance() {
        current++;
        column++;
        return source.charAt(current - 1);
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line, startColumn));
    }
}
