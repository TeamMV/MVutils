package dev.mv.utils;

import dev.mv.utils.async.Async;
import dev.mv.utils.async.Promise;

import static dev.mv.utils.Utils.*;

public class Test {

    public static void main(String[] args) {
        getString().then(str -> {
            System.out.println(str);
        });
        System.out.println("hello");
    }

    public static Promise<String> getString() {
        return new Promise<>((res, rej) -> {
           await(sleep(500));
           res.resolve("world");
        });
    }

}
