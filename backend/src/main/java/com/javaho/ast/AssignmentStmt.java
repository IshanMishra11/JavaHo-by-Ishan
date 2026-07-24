package com.javaho.ast;

public class AssignmentStmt implements Statement {
    private final String name;
    private final Expression value;
    private final int line;

    public AssignmentStmt(String name, Expression value, int line) {
        this.name = name;
        this.value = value;
        this.line = line;
    }

    public String getName() {
        return name;
    }

    public Expression getValue() {
        return value;
    }

    @Override
    public int getLine() {
        return line;
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visitAssignment(this);
    }
}
