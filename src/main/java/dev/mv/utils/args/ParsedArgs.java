package dev.mv.utils.args;

import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.function.Function;

public class ParsedArgs {

    protected ParsedArgs parsedArgs;

    protected ParsedArgs() {
        parsedArgs = this;
    }

    protected class ArgParser {

        private String[] args;
        private HashMap<String, Argument> parser;

        public ArgParser(String[] args) {
            this.args = args;
            parser = new HashMap<>();
        }

        public <T> Argument<T> addArg(String key, String dest) {
            Argument<T> arg = new Argument<T>(this, dest);
            parser.put(key, arg);
            return arg;
        }

        public <T> Argument<T> addArg(String[] keys, String dest) {
            Argument<T> arg = new Argument<T>(this, dest);
            for (String key : keys) {
                parser.put(key, arg);
            }
            return arg;
        }

        public void parse() {
            for (int i = 0; i < args.length; i++) {
                if (parser.containsKey(args[i])) {
                    try {
                        parser.get(args[i]).finish(args[++i]);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("LAUNCHER ERROR: Invalid argument length!");
                    }
                }
            }
            parser.values().forEach(v -> v.checks());
        }

        public class Argument<T> {

            private ArgParser parser;
            private String dest;
            private boolean called = false;
            private T defaultValue = null;
            private Function<String, T> map = null;

            private Argument(ArgParser parser, String dest) {
                this.parser = parser;
                this.dest = dest;
            }

            public ArgParser done() {
                return parser;
            }

            public Argument<T> map(Function<String, T> mapper) {
                map = mapper;
                return this;
            }

            public Argument<T> defaultValue(T value) {
                defaultValue = value;
                return this;
            }

            private void finish(String arg) {
                called = true;
                if (map != null) {
                    parsedArgs.setVariable(dest, map.apply(arg), defaultValue);
                    return;
                }
                parsedArgs.setVariable(dest, arg, defaultValue);
            }

            private void checks() {
                if (called) return;
                if (defaultValue == null) return;
                parsedArgs.setVariable(dest, defaultValue, null);
            }
        }
    }

    @SneakyThrows
    protected void setVariable(String name, Object value, Object defaultValue) {
        try {
            parsedArgs.getClass().getDeclaredField(name).set(parsedArgs, value);
        } catch (IllegalArgumentException e) {
            try {
                parsedArgs.getClass().getDeclaredField(name).set(parsedArgs, defaultValue);
            } catch (IllegalArgumentException ex) {
                parsedArgs.getClass().getDeclaredField(name).set(parsedArgs, 0);
            }
        }
    }
}
