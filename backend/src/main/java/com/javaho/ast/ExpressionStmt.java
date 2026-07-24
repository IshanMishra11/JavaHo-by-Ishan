package com.javaho.ast;

public class ExpressionStmt implements Statement {
    private final Expression expression;
    private final int line;

    public ExpressionStmt(Expression expression, int line) {
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
        return visitor.visitExpression(this);
    }
}
