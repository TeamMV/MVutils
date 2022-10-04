package dev.mv.utils.async.processor;

import com.google.auto.service.AutoService;
import com.sun.source.tree.*;
import com.sun.source.util.*;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.api.*;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.tree.*;
import com.sun.tools.javac.code.*;
import dev.mv.utils.async.Async;

@AutoService(Plugin.class)
public class AsyncManager implements Plugin {
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

    private void makeAsync(MethodTree method, Context context) {
        TreeMaker factory = TreeMaker.instance(context);
        Names names = Names.instance(context);
        Types types = Types.instance(context);
        JCTree.JCMethodDecl function = (JCTree.JCMethodDecl) method;
        if (!function.getReturnType().type.isPrimitive() && function.getReturnType().type.isPrimitiveOrVoid()) {

        }
        else {
            Type innerType;
            if (function.getReturnType().type.isPrimitive()) {
                function.getReturnType();
            }
            else {

            }
        }
    }

}
