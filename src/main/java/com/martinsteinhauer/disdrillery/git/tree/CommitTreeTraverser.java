package com.martinsteinhauer.disdrillery.git.tree;

import com.martinsteinhauer.disdrillery.git.model.CommitEdge;
import com.martinsteinhauer.disdrillery.git.model.CommitVertex;
import com.martinsteinhauer.disdrillery.git.writer.DbWriter;
import com.martinsteinhauer.disdrillery.git.writer.parquet.CommitEdgeDbWriter;
import com.martinsteinhauer.disdrillery.git.writer.parquet.CommitVertexDbWriter;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommitTreeTraverser extends TreeTraverser {

    private List<DbWriter> dbWriters;

    public CommitTreeTraverser(Repository repository) {
        super(repository);
        this.dbWriters = new ArrayList<>();
    }

    public void addDbWriter(DbWriter dbWriter) {
        dbWriters.add(dbWriter);
    }

    @Override
    public void process() {
        System.out.println("Processing commits...");
        getRevWalk().forEach(revCommit -> {
            try {

                /*
                try(TreeWalk treeWalk = new TreeWalk(getRevWalk().getObjectReader())) {
                    treeWalk.addTree(revCommit.getTree());
                    treeWalk.setRecursive(true);
                    while(treeWalk.next()) {
                        String path = treeWalk.getPathString();
                        System.out.println(path);
                    }
                }
                 */
                for(DbWriter dbWriter: dbWriters) {
                    handleDbWriteCall(dbWriter, revCommit);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        writeAll();
        closeAll();
    }

    private void handleDbWriteCall(DbWriter dbWriter, RevCommit revCommit) {
        if(dbWriter instanceof CommitVertexDbWriter) {
            CommitVertexDbWriter writer = (CommitVertexDbWriter)dbWriter;
            CommitVertex commitVertex = new CommitVertex();
            commitVertex.setCommitSha(revCommit.getId().getName());
            commitVertex.setCommitMessage(revCommit.getShortMessage());
            commitVertex.setCreatedAt((long)revCommit.getCommitTime());
            if(revCommit.getAuthorIdent() != null) {
                commitVertex.setAuthorName(revCommit.getAuthorIdent().getName());
                commitVertex.setAuthorMail(revCommit.getAuthorIdent().getEmailAddress());
            }
            if(revCommit.getCommitterIdent() != null) {
                commitVertex.setCommitterName(revCommit.getCommitterIdent().getName());
                commitVertex.setCommitterMail(revCommit.getCommitterIdent().getEmailAddress());
            }
            writer.addEntry(commitVertex);
        } else if(dbWriter instanceof CommitEdgeDbWriter) {
            CommitEdgeDbWriter writer = (CommitEdgeDbWriter)dbWriter;
            List<CommitEdge> collect = Arrays.stream(revCommit.getParents())
                    .map(parent -> {
                        CommitEdge commitEdge = new CommitEdge();
                        commitEdge.setCommitSha(revCommit.getId().getName());
                        commitEdge.setParentCommitSha(parent.getId().getName());
                        return commitEdge;
                    }).collect(Collectors.toList());
            writer.addAllEntries(collect);
        }
    }

    private void writeAll() {
        System.out.println("Writing all databases...");
        for(DbWriter dbWriter: dbWriters) {
            dbWriter.write();
        }
    }

    private void closeAll() {
        System.out.println("Closing all databases...");
        for(DbWriter dbWriter: dbWriters) {
            dbWriter.close();
        }
    }
}
