package com.javaho.ast;

public class VariableExpr implements Expression {
    private final String name;
    private final int line;

    public VariableExpr(String name, int line) {
        this.name = name;
        this.line = line;
    }

    public String getName() {
        return name;
    }

    @Override
    public int getLine() {
        return line;
    }

    @Override
    public <R> R accept(ExpressionVisitor<R> visitor) {
        return visitor.visitVariable(this);
    }
}
