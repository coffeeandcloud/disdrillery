package com.martinsteinhauer.disdrillery.git.writer.csv;

import com.martinsteinhauer.disdrillery.git.writer.DbWriter;

import java.io.File;

public class CsvDbWriter extends DbWriter {

    public CsvDbWriter(File dbFile) {
        super(dbFile);
    }

    @Override
    public void write() {

    }

    @Override
    public void close() {

    }
}
