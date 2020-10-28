package com.martinsteinhauer.disdrillery.git.tree;

import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevWalk;

import java.io.IOException;

public abstract class TreeTraverser {

    private final Repository repository;

    public TreeTraverser(Repository repository) {
        this.repository = repository;
    }

    protected RevWalk getRevWalk() {
        RevWalk revWalkInstance = new RevWalk(repository);
        try {
            revWalkInstance.markStart(revWalkInstance.parseCommit(repository.resolve(Constants.HEAD)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return revWalkInstance;
    }

    protected Repository getRepository() {
        return repository;
    }

    public abstract void process();
}
