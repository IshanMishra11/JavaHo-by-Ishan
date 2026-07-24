package com.javaho.ast;

import java.util.List;

public class Program implements Statement {
    private final List<Statement> statements;
    private final int line;

    public Program(List<Statement> statements, int line) {
        this.statements = statements;
        this.line = line;
    }

    public List<Statement> getStatements() {
        return statements;
    }

    @Override
    public int getLine() {
        return line;
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visitProgram(this);
    }
}
