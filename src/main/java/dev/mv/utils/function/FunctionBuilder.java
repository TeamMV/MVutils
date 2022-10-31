package dev.mv.utils.function;

import dev.mv.utils.function.node.ElseNode;
import dev.mv.utils.function.node.IfNode;
import dev.mv.utils.function.node.RootNode;
import dev.mv.utils.function.node.StatementNode;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public final class FunctionBuilder {

    private RootNode root;

    public FunctionBuilder() {
        this.root = new RootNode(this);
    }

    public RootNode start() {
        return root;
    }

    public FunctionBuilder reset() {
        root = new RootNode(this);
        return this;
    }

    @NotNull
    public Runnable compile() {
        return (Runnable) root.visit(new RunnableNodeVisitor());
    }

    private static final class RunnableNodeVisitor implements NodeVisitor {

        @Override
        public Runnable visitRoot(RootNode node) {
            final Runnable[] methods = node.children.stream().map(child -> child.visit(this)).toArray(Runnable[]::new);
            return () -> {
                for (Runnable method : methods) {
                    method.run();
                }
            };
        }

        @Override
        @SneakyThrows
        public Runnable visitStatement(StatementNode node) {
            if (node.statement instanceof Runnable) {
                return (Runnable) node.statement;
            } else {
                throw new IllegalFunctionTypeException("All functions given to a RunnableBuilder must be runnables");
            }
        }

        @Override
        public <T extends Node> Runnable visitIf(IfNode<T> node) {
            final Runnable[] methods = node.children.stream().map(child -> child.visit(this)).toArray(Runnable[]::new);
            final Supplier<Boolean> predicate = node.predicate;
            if (node.end != null && (node.end instanceof IfNode || node.end instanceof ElseNode)) {
                final Runnable after = (Runnable) node.end.visit(this);
                return () -> {
                    if (predicate.get()) {
                        for (Runnable method : methods) {
                            method.run();
                        }
                    } else {
                        after.run();
                    }
                };
            } else {
                return () -> {
                    if (predicate.get()) {
                        for (Runnable method : methods) {
                            method.run();
                        }
                    }
                };
            }
        }

        @Override
        public <T extends Node> Runnable visitElse(ElseNode<T> node) {
            final Runnable[] methods = node.children.stream().map(child -> child.visit(this)).toArray(Runnable[]::new);
            return () -> {
                for (Runnable method : methods) {
                    method.run();
                }
            };
        }
    }

}
