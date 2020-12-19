package com.martinsteinhauer.disdrillery.git.tree;

import com.martinsteinhauer.disdrillery.git.model.CommitEdge;
import com.martinsteinhauer.disdrillery.git.model.CommitVertex;
import com.martinsteinhauer.disdrillery.git.model.FileContentVertex;
import com.martinsteinhauer.disdrillery.git.repository.FileContentRepository;
import com.martinsteinhauer.disdrillery.git.writer.DbWriter;
import com.martinsteinhauer.disdrillery.git.writer.parquet.CommitEdgeDbWriter;
import com.martinsteinhauer.disdrillery.git.writer.parquet.CommitVertexDbWriter;
import com.martinsteinhauer.disdrillery.git.writer.parquet.FileContentVertexDbWriter;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class CommitTreeTraverser extends TreeTraverser {

    private List<DbWriter> dbWriters;
    private FileContentRepository fileContentRepository;


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
        AtomicReference<Long> processedCommits = new AtomicReference<>(0L);
        FastContentReader fastContentReader = new FastContentReader(getRepository(), 8);
        System.out.println("Creating indices...");
        getRevWalk().forEach(revCommit -> {
            for(DbWriter dbWriter: dbWriters) {
                handleDbWriteCall(dbWriter, revCommit);
            }

            try(TreeWalk treeWalk = new TreeWalk(getRevWalk().getObjectReader())) {
                List<ImmutablePair<ObjectId, FileContentVertex>> objectIds = new ArrayList<>();
                treeWalk.addTree(revCommit.getTree());
                treeWalk.setRecursive(true);
                while(treeWalk.next()) {
                    FileContentVertex contentVertex = handleFileContentWriteCall(getFileContentDbWriter(), revCommit, treeWalk);
                    objectIds.add(new ImmutablePair<>(treeWalk.getObjectId(0), contentVertex));
                }
                fastContentReader.copyTo(fileContentRepository, objectIds);
                getFileContentDbWriter().write();
                getFileContentDbWriter().clearDatabase();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            processedCommits.updateAndGet(v -> v + 1);
            System.out.println("Processed commits: " + processedCommits);
        });
        writeAll();
        closeAll();
        fastContentReader.shutdown();
    }

    private FileContentVertex handleFileContentWriteCall(FileContentVertexDbWriter dbWriter, RevCommit revCommit, TreeWalk treeWalk) {
        FileContentVertex contentVertex = new FileContentVertex();
        contentVertex.setCommitSha(revCommit.getId().getName());
        contentVertex.setObjectSha(treeWalk.getObjectId(0).getName());
        // parse file path
        try {
            String[] split = treeWalk.getNameString().split("\\.");
            contentVertex.setName(split[0]);
            if(split.length > 1) {
                contentVertex.setType(split[1]);
            }
            contentVertex.setPath(treeWalk.getPathString().split(File.separator));
        } catch (Exception e) {
            e.printStackTrace();
        }
        dbWriter.addEntry(contentVertex);
        return contentVertex;
    }

    private FileContentVertexDbWriter getFileContentDbWriter() {
        for(DbWriter dbWriter: dbWriters) {
            if(dbWriter instanceof FileContentVertexDbWriter) return (FileContentVertexDbWriter)dbWriter;
        }
        return null;
    }

    public FileContentRepository getFileContentRepository() {
        return fileContentRepository;
    }

    public CommitTreeTraverser setFileContentRepository(FileContentRepository fileContentRepository) {
        this.fileContentRepository = fileContentRepository;
        return this;
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
        for(DbWriter dbWriter: dbWriters) {
            if(!(dbWriter instanceof FileContentVertexDbWriter)) {
                dbWriter.write();
            }
        }
    }

    private void closeAll() {
        for(DbWriter dbWriter: dbWriters) {
            if(!(dbWriter instanceof FileContentVertexDbWriter)) {
                dbWriter.close();
            }
        }
    }
}
