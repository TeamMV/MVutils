package dev.mv.utils.function.node;

import dev.mv.utils.function.Node;
import dev.mv.utils.function.NodeVisitor;
import dev.mv.utils.function.anonymous.AnonymousFunction;

public class StatementNode implements Node {

    public AnonymousFunction statement;

    public StatementNode(AnonymousFunction statement) {
        this.statement = statement;
    }

    public AnonymousFunction visit(NodeVisitor visitor) {
        return visitor.visitStatement(this);
    }

}
