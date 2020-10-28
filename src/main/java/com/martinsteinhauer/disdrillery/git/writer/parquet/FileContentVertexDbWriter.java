package com.martinsteinhauer.disdrillery.git.writer.parquet;

import com.martinsteinhauer.disdrillery.git.model.FileContentVertex;
import com.martinsteinhauer.disdrillery.git.model.ParquetSchema;
import org.apache.avro.generic.GenericData;

import java.io.File;
import java.util.Arrays;

public class FileContentVertexDbWriter extends ParquetDbWriter<FileContentVertex>{

    public FileContentVertexDbWriter(File dbFile) {
        super(dbFile, ParquetSchema.getFileContentVertexSchema());
    }

    @Override
    protected GenericData.Record transform(FileContentVertex dataRow) {
        GenericData.Record record = new GenericData.Record(getSchema());
        record.put("commit_sha", dataRow.getCommitSha());
        record.put("object_sha", dataRow.getObjectSha());
        record.put("name", dataRow.getName());
        record.put("type", dataRow.getType());
        record.put("path", Arrays.asList(dataRow.getPath()));
        return record;
    }
}
