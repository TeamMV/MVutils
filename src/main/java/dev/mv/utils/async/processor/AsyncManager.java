package dev.mv.utils.async.processor;

import com.google.auto.service.AutoService;
import com.sun.source.tree.*;
import com.sun.source.util.*;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.api.*;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.tree.*;
import com.sun.tools.javac.code.*;
import com.sun.tools.javac.util.List;
import dev.mv.utils.async.*;
import lombok.SneakyThrows;

import static com.sun.tools.javac.util.List.nil;

public class AsyncManager implements Plugin {

    private PromiseNull promiseNull = new PromiseNull((res, rej) -> {});
    private Promise promiseT = new Promise<>((res, rej) -> {});
    private ResolverNull resolverNull = () -> {};
    private Resolver resolverT = (o) -> {};
    private Rejector rejector = new Rejector() {
        @SneakyThrows
        @Override
        public void reject(Throwable t) {
            throw new PromiseRejectedException(t);
        }
        @SneakyThrows
        @Override
        public void reject(String s) {
            throw new PromiseRejectedException(s);
        }
        @SneakyThrows
        @Override
        public void reject(String s, Throwable t) {
            throw new PromiseRejectedException(s, t);
        }
    };

    @Override
    public String getName() {
        return "AsyncManager";
    }

    @Override
    public void init(JavacTask task, String... args) {
        Context context = ((BasicJavacTask) task).getContext();

        task.addTaskListener(new TaskListener() {
            @Override
            public void started(TaskEvent e) {

            }

            @Override
            public void finished(TaskEvent e) {
                if (e.getKind() != TaskEvent.Kind.PARSE) return;
                e.getCompilationUnit().accept(new TreeScanner<Void, Void>() {
                    @Override
                    public Void visitMethod(MethodTree node, Void unused) {
                        if (shouldChange(node)) {
                            makeAsync(node, context);
                        }
                        return null;
                    }
                }, null);
            }
        });
    }

    private boolean shouldChange(MethodTree method) {
        return method.getModifiers().getAnnotations().stream().anyMatch(annotation -> annotation.getAnnotationType().toString().equals(Async.class.getSimpleName()));
    }

    @SneakyThrows
    private void makeAsync(MethodTree method, Context context) {
        TreeMaker factory = TreeMaker.instance(context);
        Names names = Names.instance(context);
        Types types = Types.instance(context);
        JCTree.JCMethodDecl function = (JCTree.JCMethodDecl) method;
        factory.at(function.pos);
        if (!function.getReturnType().type.isPrimitive() && function.getReturnType().type.isPrimitiveOrVoid()) {
            Type promiseNullType = function.getReturnType().type.constType(promiseNull);
            function.getReturnType().setType(promiseNullType);
            JCTree.JCReturn body = makePromiseNull(function.getBody().stats, names, factory, types, promiseNullType);
            function.getBody().stats.clear();
            function.getBody().stats.add(body);
        }
        else {
            Type innerType;
            if (function.getReturnType().type.isPrimitive()) {
                throw new AsyncProcessingException("Please don't return primitives from an async function");
            }
            else {
                innerType = function.getReturnType().type;
            }
        }
    }

    public JCTree.JCReturn makePromiseNull(List<JCTree.JCStatement> statements, Names names, TreeMaker factory, Types types, Type promiseNullType) {
        List<JCTree.JCStatement> lambdaBody = com.sun.tools.javac.util.List.<JCTree.JCStatement>nil();

        JCTree.JCLambda promise = factory.Lambda(List.nil(), factory.Block(0, lambdaBody));
        JCTree.JCVariableDecl res = factory.Param(names.fromString("x" + 0), promiseNullType.constType(resolverNull), promise.type.asElement());
        JCTree.JCVariableDecl rej = factory.Param(names.fromString("x" + 1), promiseNullType.constType(rejector), promise.type.asElement());
        promise.params.add(res);
        promise.params.add(rej);
        statements.forEach(statement -> {
            if (statement.getKind() == Tree.Kind.RETURN) {
                JCTree.JCExpression returnVal = ((JCTree.JCReturn) statement).getExpression();
                factory.App(null, List.of(returnVal));
            }
            else if (statement.getKind() == Tree.Kind.THROW) {
                JCTree.JCExpression throwVal = ((JCTree.JCThrow) statement).getExpression();
                factory.App(null, List.of(throwVal));
            }
            else {
                lambdaBody.add(statement);
            }
        });
        return factory.Return(
                factory.NewClass(
                        null,
                        nil(),
                        factory.Ident(names.fromString(PromiseNull.class.getSimpleName())),
                        com.sun.tools.javac.util.List.of(
                                promise
                        ),
                        null));
    }

}
