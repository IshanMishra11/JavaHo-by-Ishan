package com.javaho.ast;

import java.util.List;

public class IfStmt implements Statement {
    private final Expression condition;
    private final List<Statement> thenBranch;
    private final List<Statement> elseBranch;
    private final int line;

    public IfStmt(Expression condition, List<Statement> thenBranch, List<Statement> elseBranch, int line) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
        this.line = line;
    }

    public Expression getCondition() {
        return condition;
    }

    public List<Statement> getThenBranch() {
        return thenBranch;
    }

    public List<Statement> getElseBranch() {
        return elseBranch;
    }

    @Override
    public int getLine() {
        return line;
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visitIf(this);
    }
}
