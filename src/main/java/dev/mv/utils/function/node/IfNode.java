package dev.mv.utils.function.node;

import dev.mv.utils.function.Node;
import dev.mv.utils.function.NodeVisitor;
import dev.mv.utils.function.anonymous.AnonymousFunction;
import dev.mv.utils.function.anonymous.Runnable;
import dev.mv.utils.function.anonymous.Supplier;

import java.util.ArrayList;
import java.util.List;

public class IfNode<T extends Node> implements Node {

    private final T parent;

    public Supplier<Boolean> predicate;
    public List<Node> children;
    public Node end;

    public IfNode(T parent, Supplier<Boolean> predicate) {
        this.parent = parent;
        this.predicate = predicate;
        children = new ArrayList<>();
    }

    public IfNode<T> setPredicate(Supplier<Boolean> predicate) {
        this.predicate = predicate;
        return this;
    }

    public IfNode<T> addStatement(AnonymousFunction function) {
        children.add(new StatementNode(function));
        return this;
    }

    public IfNode<T> addStatement(Runnable function) {
        children.add(new StatementNode(function));
        return this;
    }

    public <R> IfNode<T> addStatement(Supplier<R> function) {
        children.add(new StatementNode(function));
        return this;
    }

    public IfNode<T> addElseIf(Supplier<Boolean> elseIf) {
        end = new IfNode<T>(parent, elseIf);
        return (IfNode<T>) end;
    }

    public ElseNode<T> addElse() {
        end = new ElseNode<T>(parent);
        return (ElseNode<T>) end;
    }

    public T endIf() {
        return parent;
    }

    public AnonymousFunction visit(NodeVisitor visitor) {
        return visitor.visitIf(this);
    }

}
