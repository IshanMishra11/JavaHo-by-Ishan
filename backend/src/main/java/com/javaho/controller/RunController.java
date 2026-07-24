package com.javaho.controller;

import com.javaho.ast.AstPrinter;
import com.javaho.ast.Program;
import com.javaho.interpreter.Interpreter;
import com.javaho.interpreter.RuntimeError;
import com.javaho.lexer.Lexer;
import com.javaho.lexer.LexerException;
import com.javaho.lexer.Token;
import com.javaho.model.ExecutionRequest;
import com.javaho.model.ExecutionResponse;
import com.javaho.parser.ParseException;
import com.javaho.parser.Parser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
public class RunController {

    @PostMapping({"/run", "/api/run"})
    public ResponseEntity<ExecutionResponse> runCode(@RequestBody ExecutionRequest request) {
        if (request == null || request.getCode() == null) {
            return ResponseEntity.ok(new ExecutionResponse(false, "", List.of("No Javaho code provided."), Collections.emptyList(), ""));
        }

        try {
            // Step 1: Tokenize
            Lexer lexer = new Lexer(request.getCode());
            List<Token> tokens = lexer.scanTokens();
            List<String> tokenStrings = tokens.stream()
                    .map(t -> t.getType() + " ('" + t.getLexeme() + "') Line " + t.getLine())
                    .collect(Collectors.toList());

            // Step 2: Parse
            Parser parser = new Parser(tokens);
            Program program = parser.parse();
            AstPrinter astPrinter = new AstPrinter();
            String astTree = astPrinter.print(program);

            // Step 3: Interpret
            Interpreter interpreter = new Interpreter();
            List<String> outputLines = interpreter.interpret(program);

            String resultOutput = String.join("\n", outputLines);
            return ResponseEntity.ok(new ExecutionResponse(true, resultOutput, Collections.emptyList(), tokenStrings, astTree));

        } catch (LexerException | ParseException | RuntimeError e) {
            return ResponseEntity.ok(new ExecutionResponse(false, "", List.of(e.getMessage()), Collections.emptyList(), ""));
        } catch (Exception e) {
            return ResponseEntity.ok(new ExecutionResponse(false, "", List.of("Internal Error: " + e.getMessage()), Collections.emptyList(), ""));
        }
    }
}
