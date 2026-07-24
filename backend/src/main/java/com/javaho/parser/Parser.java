package com.javaho.parser;

import com.javaho.ast.*;
import com.javaho.lexer.Token;
import com.javaho.lexer.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Program parse() {
        List<Statement> statements = new ArrayList<>();
        int startLine = tokens.isEmpty() ? 1 : tokens.get(0).getLine();

        while (!isAtEnd()) {
            statements.add(declaration());
        }

        return new Program(statements, startLine);
    }

    private Statement declaration() {
        if (match(TokenType.RAKH)) {
            return varDeclaration();
        }
        return statement();
    }

    private Statement varDeclaration() {
        Token keywordToken = previous();
        Token nameToken = consume(TokenType.IDENTIFIER, "Expected variable name after 'rakh'.");
        consume(TokenType.ASSIGN, "Expected '=' after variable name in declaration.");
        Expression initializer = expression();

        return new VarDeclStmt(nameToken.getLexeme(), initializer, keywordToken.getLine());
    }

    private Statement statement() {
        if (match(TokenType.DIKHA)) {
            return printStatement();
        }
        if (match(TokenType.AGAR)) {
            return ifStatement();
        }
        if (match(TokenType.JABTAK)) {
            return whileStatement();
        }
        return assignmentOrExpressionStatement();
    }

    private Statement printStatement() {
        Token keywordToken = previous();
        Expression value = expression();
        return new PrintStmt(value, keywordToken.getLine());
    }

    private Statement ifStatement() {
        Token keywordToken = previous();
        Expression condition = expression();

        List<Statement> thenBranch = new ArrayList<>();
        List<Statement> elseBranch = new ArrayList<>();

        while (!check(TokenType.NAHIN) && !check(TokenType.KHATAM) && !isAtEnd()) {
            thenBranch.add(declaration());
        }

        if (match(TokenType.NAHIN)) {
            while (!check(TokenType.KHATAM) && !isAtEnd()) {
                elseBranch.add(declaration());
            }
        }

        consume(TokenType.KHATAM, "Expected 'khatam' to close block starting at line " + keywordToken.getLine() + ".");
        return new IfStmt(condition, thenBranch, elseBranch, keywordToken.getLine());
    }

    private Statement whileStatement() {
        Token keywordToken = previous();
        Expression condition = expression();

        List<Statement> body = new ArrayList<>();

        while (!check(TokenType.KHATAM) && !isAtEnd()) {
            body.add(declaration());
        }

        consume(TokenType.KHATAM, "Expected 'khatam' to close 'jabtak' loop starting at line " + keywordToken.getLine() + ".");
        return new WhileStmt(condition, body, keywordToken.getLine());
    }

    private Statement assignmentOrExpressionStatement() {
        int line = peek().getLine();

        if (check(TokenType.IDENTIFIER) && checkNext(TokenType.ASSIGN)) {
            Token nameToken = advance(); // identifier
            advance(); // '=' token
            Expression value = expression();
            return new AssignmentStmt(nameToken.getLexeme(), value, line);
        }

        Expression expr = expression();
        return new ExpressionStmt(expr, line);
    }

    private Expression expression() {
        return equality();
    }

    private Expression equality() {
        Expression expr = comparison();

        while (match(TokenType.EQUAL_EQUAL, TokenType.NOT_EQUAL)) {
            Token operator = previous();
            Expression right = comparison();
            expr = new BinaryExpr(expr, operator, right, operator.getLine());
        }

        return expr;
    }

    private Expression comparison() {
        Expression expr = term();

        while (match(TokenType.LESS, TokenType.LESS_EQUAL, TokenType.GREATER, TokenType.GREATER_EQUAL)) {
            Token operator = previous();
            Expression right = term();
            expr = new BinaryExpr(expr, operator, right, operator.getLine());
        }

        return expr;
    }

    private Expression term() {
        Expression expr = factor();

        while (match(TokenType.PLUS, TokenType.MINUS)) {
            Token operator = previous();
            Expression right = factor();
            expr = new BinaryExpr(expr, operator, right, operator.getLine());
        }

        return expr;
    }

    private Expression factor() {
        Expression expr = primary();

        while (match(TokenType.STAR, TokenType.SLASH)) {
            Token operator = previous();
            Expression right = primary();
            expr = new BinaryExpr(expr, operator, right, operator.getLine());
        }

        return expr;
    }

    private Expression primary() {
        Token token = peek();

        if (match(TokenType.NUMBER, TokenType.STRING, TokenType.SAHI, TokenType.GALAT)) {
            return new LiteralExpr(previous().getLiteral(), previous().getLine());
        }

        if (match(TokenType.IDENTIFIER)) {
            return new VariableExpr(previous().getLexeme(), previous().getLine());
        }

        if (match(TokenType.LPAREN)) {
            Expression expr = expression();
            consume(TokenType.RPAREN, "Expected ')' after expression.");
            return expr;
        }

        throw new ParseException("Unexpected token '" + token.getLexeme() + "'", token.getLine(), token.getColumn());
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();

        Token token = peek();
        throw new ParseException(message, token.getLine(), token.getColumn());
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().getType() == type;
    }

    private boolean checkNext(TokenType type) {
        if (isAtEnd()) return false;
        if (tokens.get(current).getType() == TokenType.EOF) return false;
        return tokens.get(current + 1).getType() == type;
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().getType() == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }
}
