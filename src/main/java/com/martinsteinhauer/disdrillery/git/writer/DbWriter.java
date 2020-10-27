package com.martinsteinhauer.disdrillery.git.writer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class DbWriter<T> {

    private File dbFile;
    private List<T> database;

    public DbWriter(File dbFile) {
        this.dbFile = dbFile;
        this.database = new ArrayList<>();
    }

    public abstract void write();
    public abstract void close();

    protected File getDbFile() {
        return dbFile;
    }

    public void addEntry(T entry) {
        database.add(entry);
    }

    public void clearDatabase() {
        database.clear();
    }

    public void addAllEntries(List<T> entries) {
        database.addAll(entries);
    }

    public List<T> getDatabase() {
        return this.database;
    }
}
