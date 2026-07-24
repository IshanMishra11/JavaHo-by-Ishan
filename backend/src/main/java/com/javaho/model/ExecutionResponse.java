package com.javaho.model;

import java.util.ArrayList;
import java.util.List;

public class ExecutionResponse {
    private boolean success;
    private String output;
    private List<String> errors;
    private List<String> tokens;
    private String astTree;

    public ExecutionResponse() {
        this.errors = new ArrayList<>();
        this.tokens = new ArrayList<>();
    }

    public ExecutionResponse(boolean success, String output, List<String> errors, List<String> tokens, String astTree) {
        this.success = success;
        this.output = output;
        this.errors = errors != null ? errors : new ArrayList<>();
        this.tokens = tokens != null ? tokens : new ArrayList<>();
        this.astTree = astTree != null ? astTree : "";
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }

    public String getAstTree() {
        return astTree;
    }

    public void setAstTree(String astTree) {
        this.astTree = astTree;
    }
}
