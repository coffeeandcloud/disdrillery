package com.martinsteinhauer.disdrillery.git.model;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;

import java.util.ArrayList;

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

    public static Schema getFileContentVertexSchema() {
        return SchemaBuilder
                .record("file_content_vertex")
                .fields()
                .requiredString("commit_sha")
                .requiredString("object_sha")
                .requiredString("name")
                .optionalString("type")
                .name("path")
                .type().array().items().stringType().arrayDefault(new ArrayList<>())
                .endRecord();
    }
}
