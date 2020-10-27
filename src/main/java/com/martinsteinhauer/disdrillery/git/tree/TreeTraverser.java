package com.martinsteinhauer.disdrillery.git.tree;

import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import java.io.IOException;

public abstract class TreeTraverser {

    private RevWalk revWalk;
    private ObjectId latestCommit;

    public TreeTraverser(Repository repository) {
        this.revWalk = new RevWalk(repository);
        try {
            revWalk.markStart(revWalk.parseCommit(repository.resolve(Constants.HEAD)));
            latestCommit = repository.resolve(Constants.HEAD);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected RevWalk getRevWalk() {
        return this.revWalk;
    }

    public abstract void process();
}
