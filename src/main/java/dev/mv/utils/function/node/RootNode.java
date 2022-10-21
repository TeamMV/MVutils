package dev.mv.utils.function.node;

import dev.mv.utils.function.AnonymousFunctionBuilder;
import dev.mv.utils.function.Node;
import dev.mv.utils.function.NodeVisitor;
import dev.mv.utils.function.anonymous.AnonymousFunction;
import dev.mv.utils.function.anonymous.Runnable;
import dev.mv.utils.function.anonymous.Supplier;

import java.util.ArrayList;
import java.util.List;

public class RootNode implements Node {

    private AnonymousFunctionBuilder parent;
    public List<Node> children;

    public RootNode(AnonymousFunctionBuilder parent) {
        this.parent = parent;
        children = new ArrayList<>();
    }

    public RootNode addStatement(AnonymousFunction function) {
        children.add(new StatementNode(function));
        return this;
    }

    public RootNode addStatement(Runnable function) {
        children.add(new StatementNode(function));
        return this;
    }

    public <R> RootNode addStatement(Supplier<R> function) {
        children.add(new StatementNode(function));
        return this;
    }

    public IfNode<RootNode> addIf(Supplier<Boolean> predicate) {
        IfNode<RootNode> ifNode = new IfNode<>(this, predicate);
        children.add(ifNode);
        return ifNode;
    }

    public AnonymousFunctionBuilder endRoot() {
        return parent;
    }

    public AnonymousFunction visit(NodeVisitor visitor) {
        return visitor.visitRoot(this);
    }

}
