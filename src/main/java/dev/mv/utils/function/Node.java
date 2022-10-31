package dev.mv.utils.function;

public interface Node {

    Runnable visit(NodeVisitor visitor);

}
