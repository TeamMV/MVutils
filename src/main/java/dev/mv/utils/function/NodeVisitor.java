package dev.mv.utils.function;

import dev.mv.utils.function.node.ElseNode;
import dev.mv.utils.function.node.IfNode;
import dev.mv.utils.function.node.RootNode;
import dev.mv.utils.function.node.StatementNode;

public interface NodeVisitor {

    Runnable visitRoot(RootNode node);

    Runnable visitStatement(StatementNode node);

    <T extends Node> Runnable visitIf(IfNode<T> node);

    <T extends Node> Runnable visitElse(ElseNode<T> node);

}
