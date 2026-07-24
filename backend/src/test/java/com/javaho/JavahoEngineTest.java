package com.javaho;

import com.javaho.ast.Program;
import com.javaho.interpreter.Interpreter;
import com.javaho.interpreter.RuntimeError;
import com.javaho.lexer.Lexer;
import com.javaho.lexer.Token;
import com.javaho.parser.ParseException;
import com.javaho.parser.Parser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JavahoEngineTest {

    private List<String> runCode(String source) {
        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();
        Parser parser = new Parser(tokens);
        Program program = parser.parse();
        Interpreter interpreter = new Interpreter();
        return interpreter.interpret(program);
    }

    @Test
    public void testSampleProgram() {
        String code = "# Variables\n" +
                "\n" +
                "rakh naam = \"Ishan\"\n" +
                "rakh age = 21\n" +
                "\n" +
                "dikha \"Ram Ram!\"\n" +
                "dikha naam\n" +
                "\n" +
                "agar age >= 18\n" +
                "    dikha \"Adult\"\n" +
                "nahin\n" +
                "    dikha \"Minor\"\n" +
                "khatam\n" +
                "\n" +
                "rakh i = 1\n" +
                "\n" +
                "jabtak i <= 3\n" +
                "    dikha i\n" +
                "    i = i + 1\n" +
                "khatam\n";

        List<String> output = runCode(code);
        assertEquals(6, output.size());
        assertEquals("Ram Ram!", output.get(0));
        assertEquals("Ishan", output.get(1));
        assertEquals("Adult", output.get(2));
        assertEquals("1", output.get(3));
        assertEquals("2", output.get(4));
        assertEquals("3", output.get(5));
    }

    @Test
    public void testArithmeticAndComparison() {
        String code = "rakh a = 10\n" +
                "rakh b = 5\n" +
                "dikha a + b\n" +
                "dikha a - b\n" +
                "dikha a * b\n" +
                "dikha a / b\n" +
                "dikha a > b\n" +
                "dikha a == b\n";

        List<String> output = runCode(code);
        assertEquals(6, output.size());
        assertEquals("15", output.get(0));
        assertEquals("5", output.get(1));
        assertEquals("50", output.get(2));
        assertEquals("2", output.get(3));
        assertEquals("sahi", output.get(4));
        assertEquals("galat", output.get(5));
    }

    @Test
    public void testDivisionByZero() {
        String code = "rakh x = 10 / 0";
        RuntimeError ex = assertThrows(RuntimeError.class, () -> runCode(code));
        assertTrue(ex.getMessage().contains("Division by zero"));
    }

    @Test
    public void testUndefinedVariable() {
        String code = "dikha undefinedVar";
        RuntimeError ex = assertThrows(RuntimeError.class, () -> runCode(code));
        assertTrue(ex.getMessage().contains("Undefined variable"));
    }

    @Test
    public void testMissingKhatam() {
        String code = "agar 1 == 1\n dikha \"Hi\"";
        ParseException ex = assertThrows(ParseException.class, () -> runCode(code));
        assertTrue(ex.getMessage().contains("khatam"));
    }
}
