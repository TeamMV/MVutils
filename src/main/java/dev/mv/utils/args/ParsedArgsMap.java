package dev.mv.utils.args;

import java.util.HashMap;
import java.util.Map;

public class ParsedArgsMap {

    private ParsedArgsMap parsedArgs;

    private Map<String, Object> values;

    private String[] args;

    public ParsedArgsMap(String[] args) {
        parsedArgs = this;
        values = new HashMap<>();
        this.args = args;
        parse();
    }

    private void parse() {
        for (int i = 0; i < args.length; i++) {
            values.put(args[i], args[++i]);
        }
    }

    public <T> T get(String key) {
        return (T) values.get(key);
    }
}
