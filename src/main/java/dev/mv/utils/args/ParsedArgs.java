package dev.mv.utils.args;

import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.function.Function;

public class ParsedArgs {

    private ParsedArgs parsedArgs;

    public ParsedArgs() {
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
                    setVariable(dest, map.apply(arg));
                    return;
                }
                setVariable(dest, arg);
            }

            private void checks() {
                if (called) return;
                if (defaultValue == null) return;
                setVariable(dest, defaultValue);
            }

            @SneakyThrows
            private void setVariable(String name, Object value) {
                try {
                    parsedArgs.getClass().getDeclaredField(name).set(parsedArgs, value);
                } catch (IllegalArgumentException e) {
                    parsedArgs.getClass().getDeclaredField(name).set(parsedArgs, 0);
                }
            }
        }
    }
}
