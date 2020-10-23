package com.martinsteinhauer.disdrillery.git.writer;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.internal.utils.FileUtil;

import java.io.File;

public abstract class DbWriter {

    private File dbFile;

    public DbWriter(File dbFile) {
        this.dbFile = dbFile;
    }

    public abstract void appendLine(String line);

    private File getDbFile() {
        return dbFile;
    }

    protected void appendLineInternal(String line) {

    }
}
