package com.javaho.lexer;

public enum TokenType {
    // Keywords
    RAKH,       // rakh (var declaration)
    DIKHA,      // dikha (print)
    AGAR,       // agar (if)
    NAHIN,      // nahin (else)
    JABTAK,     // jabtak (while)
    KHATAM,     // khatam (end block)
    SAHI,       // sahi (true)
    GALAT,      // galat (false)

    // Identifiers and Literals
    IDENTIFIER,
    STRING,
    NUMBER,

    // Operators
    PLUS,           // +
    MINUS,          // -
    STAR,           // *
    SLASH,          // /
    ASSIGN,         // =
    EQUAL_EQUAL,    // ==
    NOT_EQUAL,      // !=
    LESS,           // <
    LESS_EQUAL,     // <=
    GREATER,        // >
    GREATER_EQUAL,  // >=

    // Delimiters
    LPAREN,         // (
    RPAREN,         // )

    // Special
    EOF
}
