package dev.mv.utils.args;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ParsedArgsMap {
    private final Map<String, String> stringValues;
    private final Map<String, Object> objectValues;

    private final String[] args;

    public ParsedArgsMap(String[] args) {
        stringValues = new HashMap<>();
        objectValues = new HashMap<>();
        this.args = args;
        parse();
    }

    private void parse() {
        for (int i = 0; i < args.length; i++) {
            stringValues.put(args[i], args[++i]);
        }
    }

    public String get(String key) {
        return stringValues.get(key);
    }

    public <T> T getMapped(String key) {
        return (T) objectValues.get(key);
    }

    public <T> T map(String key, Function<String, T> mapper) {
        String value = stringValues.get(key);
        T mapped = mapper.apply(value);
        objectValues.put(key, mapped);
        return mapped;
    }

    public boolean has(String key) {
        return stringValues.containsKey(key);
    }
}
