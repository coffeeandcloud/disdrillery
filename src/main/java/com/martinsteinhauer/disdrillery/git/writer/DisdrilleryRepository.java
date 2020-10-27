package com.martinsteinhauer.disdrillery.git.writer;

import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;

public class DisdrilleryRepository {

    private File outputDir;

    public DisdrilleryRepository(String outputDir) {
        this.outputDir = new File(outputDir);

    }

    void writeCommit(RevCommit revCommit) {

    }
}
