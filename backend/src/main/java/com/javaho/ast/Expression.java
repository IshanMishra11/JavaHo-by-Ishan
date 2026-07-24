package com.javaho.ast;

public interface Expression extends Node {
    <R> R accept(ExpressionVisitor<R> visitor);

    interface ExpressionVisitor<R> {
        R visitLiteral(LiteralExpr expr);
        R visitVariable(VariableExpr expr);
        R visitBinary(BinaryExpr expr);
    }
}
