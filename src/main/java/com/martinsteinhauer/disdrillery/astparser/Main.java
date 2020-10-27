package com.martinsteinhauer.disdrillery.astparser;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) {
        ASTParser parser = ASTParser.newParser(AST.JLS11);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        try {
            char[] content = readFileContent(args[0]);
            parser.setSource(content);
            parser.setResolveBindings(true);
            CompilationUnit cu = (CompilationUnit) parser.createAST(null);
            ASTNode root = cu.getRoot();
            System.out.println(root.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static char[] readFileContent(String file) throws URISyntaxException, IOException {
        return Files.readString(Path.of(file)).toCharArray();
    }
}
