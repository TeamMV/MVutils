package dev.mv.utils.function;

import dev.mv.utils.function.anonymous.AnonymousFunction;

public interface Node {

    AnonymousFunction visit(NodeVisitor visitor);

}
