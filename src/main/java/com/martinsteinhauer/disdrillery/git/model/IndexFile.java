package com.martinsteinhauer.disdrillery.git.model;

public class IndexFile {
    private Integer version = 1;
    private String dbFormat = "csv";

    public Integer getVersion() {
        return version;
    }

    public IndexFile setVersion(Integer version) {
        this.version = version;
        return this;
    }

    public String getDbFormat() {
        return dbFormat;
    }

    public IndexFile setDbFormat(String dbFormat) {
        this.dbFormat = dbFormat;
        return this;
    }
}
