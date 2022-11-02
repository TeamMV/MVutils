package dev.mv.utils;

import dev.mv.utils.args.ParsedArgs;

public class Test extends ParsedArgs {

    public String something;

    public Test(String[] args) {
        super();
        ArgParser parser = new ArgParser(args);
        parser.addArg("--arg", "something").done();
        parser.parse();
    }

    public static void main(String[] args) {
        Test test = new Test(args);
    }

}
