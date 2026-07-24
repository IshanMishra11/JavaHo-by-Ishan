package com.javaho.ast;

import com.javaho.lexer.Token;

public class BinaryExpr implements Expression {
    private final Expression left;
    private final Token operator;
    private final Expression right;
    private final int line;

    public BinaryExpr(Expression left, Token operator, Expression right, int line) {
        this.left = left;
        this.operator = operator;
        this.right = right;
        this.line = line;
    }

    public Expression getLeft() {
        return left;
    }

    public Token getOperator() {
        return operator;
    }

    public Expression getRight() {
        return right;
    }

    @Override
    public int getLine() {
        return line;
    }

    @Override
    public <R> R accept(ExpressionVisitor<R> visitor) {
        return visitor.visitBinary(this);
    }
}
