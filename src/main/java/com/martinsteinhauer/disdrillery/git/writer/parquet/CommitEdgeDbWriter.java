package com.martinsteinhauer.disdrillery.git.writer.parquet;

import com.martinsteinhauer.disdrillery.git.model.CommitEdge;
import com.martinsteinhauer.disdrillery.git.model.ParquetSchema;
import org.apache.avro.generic.GenericData;

import java.io.File;

public class CommitEdgeDbWriter extends ParquetDbWriter<CommitEdge>{

    public CommitEdgeDbWriter(File dbFile) {
        super(dbFile, ParquetSchema.getCommitEdgeSchema());
    }

    @Override
    protected GenericData.Record transform(CommitEdge dataRow) {
        GenericData.Record record = new GenericData.Record(getSchema());
        record.put("parent_commit_sha", dataRow.getParentCommitSha());
        record.put("commit_sha", dataRow.getCommitSha());
        return record;
    }
}
