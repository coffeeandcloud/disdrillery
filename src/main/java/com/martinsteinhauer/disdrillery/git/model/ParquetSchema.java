package com.martinsteinhauer.disdrillery.git.model;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;

public class ParquetSchema {

    public static Schema getCommitVertexSchema() {
        return SchemaBuilder.builder()
                .record("commit_vertex")
                .fields()
                .requiredString("commit_sha")
                .optionalString("author_name")
                .optionalString("author_mail")
                .optionalString("committer_name")
                .optionalString("committer_mail")
                .requiredLong("created_at")
                .requiredString("commit_message")
                .endRecord();
    }

    public static Schema getCommitEdgeSchema() {
        return SchemaBuilder
                .record("commit_edge")
                .fields()
                .requiredString("parent_commit_sha")
                .requiredString("commit_sha")
                .endRecord();
    }
}
