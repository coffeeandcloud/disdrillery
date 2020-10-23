package com.martinsteinhauer.disdrillery.git;

import com.martinsteinhauer.disdrillery.git.tree.CommitTreeTraverser;
import com.martinsteinhauer.disdrillery.git.tree.TreeTraverser;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        System.out.println("Running Git Traversal... [repo = '" +args[0]+"']");

        try(Repository repository = Git.open(new File(args[0])).getRepository()) {
            TreeTraverser commitTraverser = new CommitTreeTraverser(repository);
            commitTraverser.process();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
