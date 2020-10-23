package com.martinsteinhauer.disdrillery.git.writer;

import java.io.File;

public class CsvDbWriter extends DbWriter {

    public CsvDbWriter(File dbFile) {
        super(dbFile);
    }

    @Override
    public void appendLine(String line) {

    }
}
