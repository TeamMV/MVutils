package dev.mv.utils.function.node;

import dev.mv.utils.function.Node;
import dev.mv.utils.function.NodeVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ElseNode<T extends Node> implements Node {

    public T parent;
    public List<Node> children;

    public ElseNode(T parent) {
        this.parent = parent;
        children = new ArrayList<>();
    }

    public Runnable visit(NodeVisitor visitor) {
        return visitor.visitElse(this);
    }

    public ElseNode<T> addStatement(Runnable function) {
        children.add(new StatementNode(function));
        return this;
    }

    public IfNode<ElseNode<T>> addIf(Supplier<Boolean> predicate) {
        IfNode<ElseNode<T>> ifNode = new IfNode<>(this, predicate);
        children.add(ifNode);
        return ifNode;
    }

    public T endIf() {
        return parent;
    }

}
