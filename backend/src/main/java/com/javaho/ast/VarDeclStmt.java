package com.javaho.ast;

public class VarDeclStmt implements Statement {
    private final String name;
    private final Expression initializer;
    private final int line;

    public VarDeclStmt(String name, Expression initializer, int line) {
        this.name = name;
        this.initializer = initializer;
        this.line = line;
    }

    public String getName() {
        return name;
    }

    public Expression getInitializer() {
        return initializer;
    }

    @Override
    public int getLine() {
        return line;
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visitVarDecl(this);
    }
}
