package com.martinsteinhauer.disdrillery.git.tree;

import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.util.concurrent.atomic.AtomicReference;

public class CommitTreeTraverser extends TreeTraverser {

    public CommitTreeTraverser(Repository repository) {
        super(repository);
    }

    @Override
    public void process() {
        System.out.println("Processing commits...");
        getRevWalk().forEach(revCommit -> {
            try {
                ObjectId objectId = revCommit.getId().toObjectId();
                String s = revCommit.getName();
                //System.out.println(" -- "+revCommit.getId().toString() + " -- ");
                //System.out.println("\t" + revCommit.getCommitTime());
                //System.out.println("\t" + revCommit.getShortMessage());
                //System.out.println("\t" + revCommit.getAuthorIdent().toString());
                try(TreeWalk treeWalk = new TreeWalk(getRevWalk().getObjectReader())) {
                    treeWalk.addTree(revCommit.getTree());
                    treeWalk.setRecursive(true);
                    while(treeWalk.next()) {
                        String path = treeWalk.getPathString();
                        System.out.println(path);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
