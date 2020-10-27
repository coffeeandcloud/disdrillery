package com.martinsteinhauer.disdrillery.git;

import com.martinsteinhauer.disdrillery.git.model.CommitEdge;
import com.martinsteinhauer.disdrillery.git.model.CommitVertex;
import com.martinsteinhauer.disdrillery.git.tree.CommitTreeTraverser;
import com.martinsteinhauer.disdrillery.git.writer.parquet.CommitEdgeDbWriter;
import com.martinsteinhauer.disdrillery.git.writer.parquet.CommitVertexDbWriter;
import com.martinsteinhauer.disdrillery.git.writer.parquet.ParquetDbWriter;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Running Git Traversal... [repo = '" +args[0]+"']");

        File commitVertexDb = new File("/Users/martinsteinhauer/Desktop/disdrilleryRepo/commitVertexDb.parquet");
        File commitEdgeDb = new File("/Users/martinsteinhauer/Desktop/disdrilleryRepo/commitEdgeDb.parquet");
        if(!commitVertexDb.getParentFile().exists()) {
            commitVertexDb.mkdirs();
        }
        if(commitVertexDb.exists()) {
            commitVertexDb.delete();
            commitEdgeDb.delete();
        }
        commitVertexDb.createNewFile();
        commitEdgeDb.createNewFile();

        ParquetDbWriter<CommitVertex> commitVertexDbWriter = new CommitVertexDbWriter(commitVertexDb);
        ParquetDbWriter<CommitEdge> commitEdgeDbWriter = new CommitEdgeDbWriter(commitEdgeDb);

        try(Repository repository = Git.open(new File(args[0])).getRepository()) {
            CommitTreeTraverser commitTraverser = new CommitTreeTraverser(repository);
            commitTraverser.addDbWriter(commitVertexDbWriter);
            commitTraverser.addDbWriter(commitEdgeDbWriter);
            commitTraverser.process();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
