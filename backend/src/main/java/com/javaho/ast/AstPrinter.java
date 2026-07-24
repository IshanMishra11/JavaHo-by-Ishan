package com.javaho.ast;

import java.util.List;

public class AstPrinter implements Statement.StatementVisitor<String>, Expression.ExpressionVisitor<String> {
    private int indentLevel = 0;

    public String print(Program program) {
        indentLevel = 0;
        return program.accept(this);
    }

    private String indent() {
        return "  ".repeat(indentLevel);
    }

    @Override
    public String visitProgram(Program stmt) {
        StringBuilder sb = new StringBuilder();
        sb.append("Program [Statements: ").append(stmt.getStatements().size()).append("]\n");
        indentLevel++;
        for (Statement s : stmt.getStatements()) {
            sb.append(indent()).append("├── ").append(s.accept(this)).append("\n");
        }
        indentLevel--;
        return sb.toString();
    }

    @Override
    public String visitVarDecl(VarDeclStmt stmt) {
        return "VarDeclStmt: rakh " + stmt.getName() + " = " +
                (stmt.getInitializer() != null ? stmt.getInitializer().accept(this) : "null");
    }

    @Override
    public String visitAssignment(AssignmentStmt stmt) {
        return "AssignmentStmt: " + stmt.getName() + " = " + stmt.getValue().accept(this);
    }

    @Override
    public String visitPrint(PrintStmt stmt) {
        return "PrintStmt: dikha " + stmt.getExpression().accept(this);
    }

    @Override
    public String visitIf(IfStmt stmt) {
        StringBuilder sb = new StringBuilder();
        sb.append("IfStmt (agar ").append(stmt.getCondition().accept(this)).append(")\n");
        indentLevel++;
        sb.append(indent()).append("├── ThenBranch [").append(stmt.getThenBranch().size()).append(" stmts]:\n");
        indentLevel++;
        for (Statement s : stmt.getThenBranch()) {
            sb.append(indent()).append("│   ├── ").append(s.accept(this)).append("\n");
        }
        indentLevel--;

        if (!stmt.getElseBranch().isEmpty()) {
            sb.append(indent()).append("└── ElseBranch [").append(stmt.getElseBranch().size()).append(" stmts]:\n");
            indentLevel++;
            for (Statement s : stmt.getElseBranch()) {
                sb.append(indent()).append("    ├── ").append(s.accept(this)).append("\n");
            }
            indentLevel--;
        }
        indentLevel--;
        return sb.toString().trim();
    }

    @Override
    public String visitWhile(WhileStmt stmt) {
        StringBuilder sb = new StringBuilder();
        sb.append("WhileStmt (jabtak ").append(stmt.getCondition().accept(this)).append(")\n");
        indentLevel++;
        sb.append(indent()).append("└── Body [").append(stmt.getBody().size()).append(" stmts]:\n");
        indentLevel++;
        for (Statement s : stmt.getBody()) {
            sb.append(indent()).append("    ├── ").append(s.accept(this)).append("\n");
        }
        indentLevel -= 2;
        return sb.toString().trim();
    }

    @Override
    public String visitExpression(ExpressionStmt stmt) {
        return "ExpressionStmt: " + stmt.getExpression().accept(this);
    }

    @Override
    public String visitLiteral(LiteralExpr expr) {
        Object val = expr.getValue();
        if (val instanceof String) {
            return "\"" + val + "\"";
        }
        if (val instanceof Boolean) {
            return (Boolean) val ? "sahi" : "galat";
        }
        return String.valueOf(val);
    }

    @Override
    public String visitVariable(VariableExpr expr) {
        return expr.getName();
    }

    @Override
    public String visitBinary(BinaryExpr expr) {
        return "(" + expr.getLeft().accept(this) + " " + expr.getOperator().getLexeme() + " " + expr.getRight().accept(this) + ")";
    }
}
