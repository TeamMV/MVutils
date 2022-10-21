package dev.mv.utils.function;

import dev.mv.utils.function.anonymous.AnonymousFunction;
import dev.mv.utils.function.node.RootNode;

public interface AnonymousFunctionBuilder {
    RootNode start();
    AnonymousFunction compile();
}
