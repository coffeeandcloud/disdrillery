package com.martinsteinhauer.disdrillery.git.writer.parquet;


import com.martinsteinhauer.disdrillery.git.writer.DbWriter;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.hadoop.ParquetFileWriter;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;

import java.io.File;
import java.io.IOException;

public abstract class ParquetDbWriter<T> extends DbWriter<T> implements AutoCloseable{
    private Schema schema;

    ParquetWriter<GenericData.Record> writer = null;

    public ParquetDbWriter(File dbFile, Schema schema) {
        super(dbFile);
        this.schema = schema;
        init();
    }

    private void init() {
        Path hadoopPath = new Path(getDbFile().toPath().toUri());
        try {
            writer = AvroParquetWriter.<GenericData.Record>builder(hadoopPath)
                    .withSchema(schema)
                    .withRowGroupSize(ParquetWriter.DEFAULT_BLOCK_SIZE)
                    .withPageSize(ParquetWriter.DEFAULT_PAGE_SIZE)
                    .withConf(new Configuration())
                    .withWriteMode(ParquetFileWriter.Mode.OVERWRITE)
                    .withCompressionCodec(CompressionCodecName.SNAPPY)
                    .withValidation(false)
                    .withDictionaryEncoding(false)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write() {
        for(T t: getDatabase()) {
            try {
                writer.write(transform(t));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected abstract GenericData.Record transform(T dataRow);

    protected Schema getSchema() {
        return this.schema;
    }

    @Override
    public void close() {
        if(writer != null) {
            try {
                writer.close();
                getDatabase().clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
