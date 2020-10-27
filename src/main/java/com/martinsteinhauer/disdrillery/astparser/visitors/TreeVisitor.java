package com.martinsteinhauer.disdrillery.astparser.visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class TreeVisitor extends ASTVisitor {
    @Override
    public boolean visit(MethodDeclaration node) {
        return super.visit(node);
    }
}
