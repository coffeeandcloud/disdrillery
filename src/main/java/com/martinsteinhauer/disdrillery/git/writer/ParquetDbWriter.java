package com.martinsteinhauer.disdrillery.git.writer;

import java.io.File;

public class ParquetDbWriter extends DbWriter {

    public ParquetDbWriter(File dbFile) {
        super(dbFile);
    }

    @Override
    public void appendLine(String line) {

    }
}
