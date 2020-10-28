package com.martinsteinhauer.disdrillery.git.model;

public class FileContentVertex {
    private String commitSha;
    private String objectSha;
    private String[] path;
    private String name;
    private String type;

    public FileContentVertex() {
    }

    public String getCommitSha() {
        return commitSha;
    }

    public FileContentVertex setCommitSha(String commitSha) {
        this.commitSha = commitSha;
        return this;
    }

    public String getObjectSha() {
        return objectSha;
    }

    public FileContentVertex setObjectSha(String objectSha) {
        this.objectSha = objectSha;
        return this;
    }

    public String[] getPath() {
        return path;
    }

    public FileContentVertex setPath(String[] path) {
        this.path = path;
        return this;
    }

    public String getName() {
        return name;
    }

    public FileContentVertex setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public FileContentVertex setType(String type) {
        this.type = type;
        return this;
    }
}
