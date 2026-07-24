package com.javaho.ast;

public class PrintStmt implements Statement {
    private final Expression expression;
    private final int line;

    public PrintStmt(Expression expression, int line) {
        this.expression = expression;
        this.line = line;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public int getLine() {
        return line;
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visitPrint(this);
    }
}
