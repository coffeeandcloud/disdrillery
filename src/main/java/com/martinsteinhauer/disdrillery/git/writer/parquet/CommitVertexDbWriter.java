package com.martinsteinhauer.disdrillery.git.writer.parquet;

import com.martinsteinhauer.disdrillery.git.model.CommitVertex;
import com.martinsteinhauer.disdrillery.git.model.ParquetSchema;
import org.apache.avro.generic.GenericData;

import java.io.File;

public class CommitVertexDbWriter extends ParquetDbWriter<CommitVertex> {
    public CommitVertexDbWriter(File dbFile) {
        super(dbFile, ParquetSchema.getCommitVertexSchema());
    }

    @Override
    protected GenericData.Record transform(CommitVertex dataRow) {
        GenericData.Record record = new GenericData.Record(getSchema());
        record.put("commit_sha", dataRow.getCommitSha());
        record.put("author_name", dataRow.getAuthorName());
        record.put("author_mail", dataRow.getAuthorMail());
        record.put("committer_name", dataRow.getCommitterName());
        record.put("committer_mail", dataRow.getCommitterMail());
        record.put("created_at", dataRow.getCreatedAt());
        record.put("commit_message", dataRow.getCommitMessage());
        return record;
    }
}
