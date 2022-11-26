package dev.mv.utils;

import dev.mv.utils.args.ParsedArgs;
import lombok.Getter;
import lombok.SneakyThrows;

public class Test extends ParsedArgs {

    @Getter
    private float version;

    @Getter
    private String name;

    public Test(String[] args) {
        ArgParser parser = new ArgParser(args);

        parser
                .addArg(new String[]{"--version", "-v"}, "version").map(Float::parseFloat).defaultValue(1.0).done()
                .addArg(new String[]{"--name", "-n"}, "name").defaultValue("").done();

        parser.parse();
    }

    public static void main(String[] args) {
        new Test(args);
    }

    @Override
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
