package dev.mv.utils.function.node;

import dev.mv.utils.function.FunctionBuilder;
import dev.mv.utils.function.Node;
import dev.mv.utils.function.NodeVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class RootNode implements Node {

    public List<Node> children;
    private FunctionBuilder parent;

    public RootNode(FunctionBuilder parent) {
        this.parent = parent;
        children = new ArrayList<>();
    }

    public RootNode addStatement(Runnable function) {
        children.add(new StatementNode(function));
        return this;
    }

    public IfNode<RootNode> addIf(Supplier<Boolean> predicate) {
        IfNode<RootNode> ifNode = new IfNode<>(this, predicate);
        children.add(ifNode);
        return ifNode;
    }

    public FunctionBuilder endRoot() {
        return parent;
    }

    public Runnable visit(NodeVisitor visitor) {
        return visitor.visitRoot(this);
    }

}
