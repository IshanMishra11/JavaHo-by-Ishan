package com.javaho.ast;

import java.util.List;

public class WhileStmt implements Statement {
    private final Expression condition;
    private final List<Statement> body;
    private final int line;

    public WhileStmt(Expression condition, List<Statement> body, int line) {
        this.condition = condition;
        this.body = body;
        this.line = line;
    }

    public Expression getCondition() {
        return condition;
    }

    public List<Statement> getBody() {
        return body;
    }

    @Override
    public int getLine() {
        return line;
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visitWhile(this);
    }
}
