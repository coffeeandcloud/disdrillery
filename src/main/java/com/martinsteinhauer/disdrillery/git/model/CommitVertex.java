package com.martinsteinhauer.disdrillery.git.model;

public class CommitVertex {
    private String commitSha;
    private String authorName;
    private String authorMail;
    private String committerName;
    private String committerMail;
    private String commitMessage;
    private Long createdAt;

    public CommitVertex(String commitSha, String authorName, String committerName, Long createdAt, String commitMessage, String authorMail, String committerMail) {
        this.commitSha = commitSha;
        this.authorName = authorName;
        this.committerName = committerName;
        this.createdAt = createdAt;
        this.commitMessage = commitMessage;
        this.authorMail = authorMail;
        this.committerMail = committerMail;
    }

    public CommitVertex() {
    }

    public String getCommitSha() {
        return commitSha;
    }

    public CommitVertex setCommitSha(String commitSha) {
        this.commitSha = commitSha;
        return this;
    }

    public String getAuthorName() {
        return authorName;
    }

    public CommitVertex setAuthorName(String authorName) {
        this.authorName = authorName;
        return this;
    }

    public String getCommitterName() {
        return committerName;
    }

    public CommitVertex setCommitterName(String committerName) {
        this.committerName = committerName;
        return this;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public CommitVertex setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getCommitMessage() {
        return commitMessage;
    }

    public CommitVertex setCommitMessage(String commitMessage) {
        this.commitMessage = commitMessage;
        return this;
    }

    public String getAuthorMail() {
        return authorMail;
    }

    public CommitVertex setAuthorMail(String authorMail) {
        this.authorMail = authorMail;
        return this;
    }

    public String getCommitterMail() {
        return committerMail;
    }

    public CommitVertex setCommitterMail(String committerMail) {
        this.committerMail = committerMail;
        return this;
    }
}
