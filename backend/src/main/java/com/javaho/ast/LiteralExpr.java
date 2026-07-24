package com.javaho.ast;

public class LiteralExpr implements Expression {
    private final Object value;
    private final int line;

    public LiteralExpr(Object value, int line) {
        this.value = value;
        this.line = line;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public int getLine() {
        return line;
    }

    @Override
    public <R> R accept(ExpressionVisitor<R> visitor) {
        return visitor.visitLiteral(this);
    }
}
