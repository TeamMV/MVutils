package dev.mv.utils.function.node;

import dev.mv.utils.function.Node;
import dev.mv.utils.function.NodeVisitor;
import dev.mv.utils.function.anonymous.AnonymousFunction;
import dev.mv.utils.function.anonymous.Runnable;
import dev.mv.utils.function.anonymous.Supplier;

import java.util.ArrayList;
import java.util.List;

public class ElseNode<T extends Node> implements Node {

    public T parent;
    public List<Node> children;

    public ElseNode(T parent) {
        this.parent = parent;
        children = new ArrayList<>();
    }

    public AnonymousFunction visit(NodeVisitor visitor) {
        return visitor.visitElse(this);
    }

    public ElseNode<T> addStatement(AnonymousFunction function) {
        children.add(new StatementNode(function));
        return this;
    }

    public ElseNode<T> addStatement(Runnable function) {
        children.add(new StatementNode(function));
        return this;
    }

    public <R> ElseNode<T> addStatement(Supplier<R> function) {
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
