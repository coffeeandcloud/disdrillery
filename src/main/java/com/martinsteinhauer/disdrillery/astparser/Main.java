package com.martinsteinhauer.disdrillery.astparser;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        Arrays.stream(args).sequential()
                .forEach(System.out::println);

        ASTParser parser = ASTParser.newParser(AST.JLS3);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setSource("".toCharArray());
        parser.setResolveBindings(true);

        CompilationUnit cu = (CompilationUnit) parser.createAST(null);
        ASTNode root = cu.getRoot();
        System.out.println(root.toString());
    }
}
