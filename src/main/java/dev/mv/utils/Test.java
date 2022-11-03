package dev.mv.utils;

import dev.mv.utils.args.ParsedArgs;
import lombok.SneakyThrows;

import static dev.mv.utils.Utils.*;

public class Test extends ParsedArgs {

    public Test(String[] args) {
        ArgParser parser = new ArgParser(args);

        parser.parse();
    }

    public static void main(String[] args) {
        new Test(args);
    }

    @Override
    @SneakyThrows
    protected void setVariable(String name, Object value) {
        try {
            parsedArgs.getClass().getDeclaredField(name).set(parsedArgs, value);
        } catch (IllegalArgumentException e) {
            parsedArgs.getClass().getDeclaredField(name).set(parsedArgs, 0);
        }
    }
}
