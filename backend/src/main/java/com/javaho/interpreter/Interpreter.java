package com.javaho.interpreter;

import com.javaho.ast.*;
import com.javaho.lexer.Token;
import com.javaho.lexer.TokenType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Interpreter implements Statement.StatementVisitor<Void>, Expression.ExpressionVisitor<Object> {
    private Environment environment = new Environment();
    private final List<String> outputLines = new ArrayList<>();
    private int stepCount = 0;
    private static final int MAX_STEPS = 100_000;

    public Interpreter() {
    }

    public List<String> interpret(Program program) {
        outputLines.clear();
        stepCount = 0;
        execute(program);
        return outputLines;
    }

    public List<String> getOutputLines() {
        return outputLines;
    }

    private void execute(Statement stmt) {
        stepCount++;
        if (stepCount > MAX_STEPS) {
            throw new RuntimeError("Execution step limit exceeded (infinite loop detected)", stmt.getLine());
        }
        stmt.accept(this);
    }

    private Object evaluate(Expression expr) {
        return expr.accept(this);
    }

    @Override
    public Void visitProgram(Program stmt) {
        for (Statement s : stmt.getStatements()) {
            execute(s);
        }
        return null;
    }

    @Override
    public Void visitVarDecl(VarDeclStmt stmt) {
        Object value = null;
        if (stmt.getInitializer() != null) {
            value = evaluate(stmt.getInitializer());
        }
        environment.define(stmt.getName(), value);
        return null;
    }

    @Override
    public Void visitAssignment(AssignmentStmt stmt) {
        Object value = evaluate(stmt.getValue());
        environment.assign(stmt.getName(), value, stmt.getLine());
        return null;
    }

    @Override
    public Void visitPrint(PrintStmt stmt) {
        Object value = evaluate(stmt.getExpression());
        outputLines.add(stringify(value));
        return null;
    }

    @Override
    public Void visitIf(IfStmt stmt) {
        if (isTruthy(evaluate(stmt.getCondition()))) {
            for (Statement s : stmt.getThenBranch()) {
                execute(s);
            }
        } else {
            for (Statement s : stmt.getElseBranch()) {
                execute(s);
            }
        }
        return null;
    }

    @Override
    public Void visitWhile(WhileStmt stmt) {
        while (isTruthy(evaluate(stmt.getCondition()))) {
            for (Statement s : stmt.getBody()) {
                execute(s);
            }
        }
        return null;
    }

    @Override
    public Void visitExpression(ExpressionStmt stmt) {
        evaluate(stmt.getExpression());
        return null;
    }

    @Override
    public Object visitLiteral(LiteralExpr expr) {
        return expr.getValue();
    }

    @Override
    public Object visitVariable(VariableExpr expr) {
        return environment.get(expr.getName(), expr.getLine());
    }

    @Override
    public Object visitBinary(BinaryExpr expr) {
        Object left = evaluate(expr.getLeft());
        Object right = evaluate(expr.getRight());
        Token op = expr.getOperator();

        switch (op.getType()) {
            case PLUS:
                if (left instanceof Integer && right instanceof Integer) {
                    return (Integer) left + (Integer) right;
                }
                if (left instanceof String || right instanceof String) {
                    return stringify(left) + stringify(right);
                }
                throw new RuntimeError("Operands for '+' must be two integers or at least one string", expr.getLine());

            case MINUS:
                checkNumberOperands(op, left, right);
                return (Integer) left - (Integer) right;

            case STAR:
                checkNumberOperands(op, left, right);
                return (Integer) left * (Integer) right;

            case SLASH:
                checkNumberOperands(op, left, right);
                if ((Integer) right == 0) {
                    throw new RuntimeError("Division by zero", expr.getLine());
                }
                return (Integer) left / (Integer) right;

            case GREATER:
                checkNumberOperands(op, left, right);
                return (Integer) left > (Integer) right;

            case GREATER_EQUAL:
                checkNumberOperands(op, left, right);
                return (Integer) left >= (Integer) right;

            case LESS:
                checkNumberOperands(op, left, right);
                return (Integer) left < (Integer) right;

            case LESS_EQUAL:
                checkNumberOperands(op, left, right);
                return (Integer) left <= (Integer) right;

            case EQUAL_EQUAL:
                return Objects.equals(left, right);

            case NOT_EQUAL:
                return !Objects.equals(left, right);

            default:
                throw new RuntimeError("Unknown binary operator " + op.getLexeme(), expr.getLine());
        }
    }

    private void checkNumberOperands(Token operator, Object left, Object right) {
        if (left instanceof Integer && right instanceof Integer) return;
        throw new RuntimeError("Operands for '" + operator.getLexeme() + "' must be integers", operator.getLine());
    }

    private boolean isTruthy(Object object) {
        if (object == null) return false;
        if (object instanceof Boolean) return (Boolean) object;
        if (object instanceof Integer) return (Integer) object != 0;
        if (object instanceof String) return !((String) object).isEmpty();
        return true;
    }

    private String stringify(Object object) {
        if (object == null) return "";
        if (object instanceof Boolean) {
            return (Boolean) object ? "sahi" : "galat";
        }
        return object.toString();
    }
}
