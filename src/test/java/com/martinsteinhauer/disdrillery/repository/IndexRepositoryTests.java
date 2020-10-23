package com.martinsteinhauer.disdrillery.repository;

import com.martinsteinhauer.disdrillery.git.repository.IndexFileRepository;
import org.junit.jupiter.api.Test;

import java.io.File;

public class IndexRepositoryTests {

    @Test
    public void testRepositoryInit() {
        String repositoryRoot = "/Users/martinsteinhauer/Desktop/disdrilleryRepo";
        IndexFileRepository repository = new IndexFileRepository(new File(repositoryRoot));
        repository.getIndexFile();
    }
}
