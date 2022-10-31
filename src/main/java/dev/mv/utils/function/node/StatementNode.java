package dev.mv.utils.function.node;

import dev.mv.utils.function.Node;
import dev.mv.utils.function.NodeVisitor;

public class StatementNode implements Node {

    public Runnable statement;

    public StatementNode(Runnable statement) {
        this.statement = statement;
    }

    public Runnable visit(NodeVisitor visitor) {
        return visitor.visitStatement(this);
    }

}
