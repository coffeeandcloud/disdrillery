package com.martinsteinhauer.disdrillery.git;

import com.martinsteinhauer.disdrillery.git.model.CommitEdge;
import com.martinsteinhauer.disdrillery.git.model.CommitVertex;
import com.martinsteinhauer.disdrillery.git.model.FileContentVertex;
import com.martinsteinhauer.disdrillery.git.repository.FileContentRepository;
import com.martinsteinhauer.disdrillery.git.repository.GitRepository;
import com.martinsteinhauer.disdrillery.git.tree.CommitTreeTraverser;
import com.martinsteinhauer.disdrillery.git.writer.parquet.CommitEdgeDbWriter;
import com.martinsteinhauer.disdrillery.git.writer.parquet.CommitVertexDbWriter;
import com.martinsteinhauer.disdrillery.git.writer.parquet.FileContentVertexDbWriter;
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
        File fileContentVertexDb = new File("/Users/martinsteinhauer/Desktop/disdrilleryRepo/fileContentVertex.parquet");

        if(!commitVertexDb.getParentFile().exists()) {
            commitVertexDb.mkdirs();
        }
        if(commitVertexDb.exists()) {
            commitVertexDb.delete();
            commitEdgeDb.delete();
            fileContentVertexDb.delete();
        }
        commitVertexDb.createNewFile();
        commitEdgeDb.createNewFile();
        fileContentVertexDb.createNewFile();

        ParquetDbWriter<CommitVertex> commitVertexDbWriter = new CommitVertexDbWriter(commitVertexDb);
        ParquetDbWriter<CommitEdge> commitEdgeDbWriter = new CommitEdgeDbWriter(commitEdgeDb);
        ParquetDbWriter<FileContentVertex> fileContentVertexDbWriter = new FileContentVertexDbWriter(fileContentVertexDb);

        FileContentRepository fileContentRepository = new FileContentRepository(new File("/Users/martinsteinhauer/Desktop/disdrilleryRepo"));

        try(Git git = Git.open(new File(args[0]))) {
            Repository repository = git.getRepository();

            GitRepository gitRepository = new GitRepository(git);
            gitRepository.checkoutAllBranches();

            CommitTreeTraverser commitTraverser = new CommitTreeTraverser(repository);
            commitTraverser.setFileContentRepository(fileContentRepository);
            commitTraverser.addDbWriter(commitVertexDbWriter);
            commitTraverser.addDbWriter(commitEdgeDbWriter);
            commitTraverser.addDbWriter(fileContentVertexDbWriter);
            commitTraverser.process();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
