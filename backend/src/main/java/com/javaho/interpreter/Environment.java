package com.javaho.interpreter;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    private final Environment enclosing;
    private final Map<String, Object> values = new HashMap<>();

    public Environment() {
        this.enclosing = null;
    }

    public Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }

    public void define(String name, Object value) {
        values.put(name, value);
    }

    public Object get(String name, int line) {
        if (values.containsKey(name)) {
            return values.get(name);
        }

        if (enclosing != null) {
            return enclosing.get(name, line);
        }

        throw new RuntimeError("Undefined variable '" + name + "'", line);
    }

    public void assign(String name, Object value, int line) {
        if (values.containsKey(name)) {
            values.put(name, value);
            return;
        }

        if (enclosing != null) {
            enclosing.assign(name, value, line);
            return;
        }

        throw new RuntimeError("Cannot assign to undefined variable '" + name + "'", line);
    }
}
