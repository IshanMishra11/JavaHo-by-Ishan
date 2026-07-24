package com.javaho.ast;

public interface Statement extends Node {
    <R> R accept(StatementVisitor<R> visitor);

    interface StatementVisitor<R> {
        R visitProgram(Program stmt);
        R visitVarDecl(VarDeclStmt stmt);
        R visitAssignment(AssignmentStmt stmt);
        R visitPrint(PrintStmt stmt);
        R visitIf(IfStmt stmt);
        R visitWhile(WhileStmt stmt);
        R visitExpression(ExpressionStmt stmt);
    }
}
