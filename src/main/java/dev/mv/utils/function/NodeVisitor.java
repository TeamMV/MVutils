package dev.mv.utils.function;

import dev.mv.utils.function.anonymous.AnonymousFunction;
import dev.mv.utils.function.node.ElseNode;
import dev.mv.utils.function.node.IfNode;
import dev.mv.utils.function.node.RootNode;
import dev.mv.utils.function.node.StatementNode;

public interface NodeVisitor {

    AnonymousFunction visitRoot(RootNode node);

    AnonymousFunction visitStatement(StatementNode node);

    <T extends Node> AnonymousFunction visitIf(IfNode<T> node);

    <T extends Node> AnonymousFunction visitElse(ElseNode<T> node);

}
