package com.martinsteinhauer.disdrillery.git.model;

public class CommitEdge {
    private String commitSha;
    private String parentCommitSha;

    public CommitEdge() {
    }

    public CommitEdge(String commitSha, String parentCommitSha) {
        this.commitSha = commitSha;
        this.parentCommitSha = parentCommitSha;
    }

    public String getCommitSha() {
        return commitSha;
    }

    public CommitEdge setCommitSha(String commitSha) {
        this.commitSha = commitSha;
        return this;
    }

    public String getParentCommitSha() {
        return parentCommitSha;
    }

    public CommitEdge setParentCommitSha(String parentCommitSha) {
        this.parentCommitSha = parentCommitSha;
        return this;
    }
}
