package com.martinsteinhauer.disdrillery.git.tree;

import com.martinsteinhauer.disdrillery.git.model.FileContentVertex;
import com.martinsteinhauer.disdrillery.git.repository.FileContentRepository;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class FastContentReader {

    private final ExecutorService executor;
    private final Repository repository;

    public FastContentReader(Repository repository, int numThreads) {
        this.executor = Executors.newFixedThreadPool(numThreads);
        this.repository = repository;
    }

    public void copyTo(FileContentRepository contentRepository, List<ImmutablePair<ObjectId, FileContentVertex>> objectIds) throws InterruptedException {
        List<CopyOperation> tasks = objectIds.stream().map(id -> new CopyOperation(id, contentRepository, repository)).collect(Collectors.toList());
        List<Future<ObjectId>> futures = executor.invokeAll(tasks);
        for(Future<ObjectId> f: futures) {
            try {
                f.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

    }

    private static class CopyOperation implements Callable<ObjectId>{

        private final FileContentRepository fileContentRepository;
        private final ImmutablePair<ObjectId, FileContentVertex> objectId;
        private final ObjectReader objectReader;

        public CopyOperation(ImmutablePair<ObjectId, FileContentVertex> objectId, FileContentRepository fileContentRepository, Repository repository) {
            this.objectId = objectId;
            this.fileContentRepository = fileContentRepository;
            this.objectReader = repository.newObjectReader();
        }

        @Override
        public ObjectId call() throws Exception {
            try {
                fileContentRepository.saveCommitFileContent(objectId.getRight(), objectReader.open(objectId.getLeft()).openStream());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                objectReader.close();
            }
            return objectId.getLeft();
        }
    }

    public void shutdown() {
        executor.shutdown();
    }
}
