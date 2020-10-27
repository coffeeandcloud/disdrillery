package com.martinsteinhauer.disdrillery.repository;

import com.martinsteinhauer.disdrillery.git.model.CommitVertex;
import com.martinsteinhauer.disdrillery.git.writer.parquet.CommitVertexDbWriter;
import com.martinsteinhauer.disdrillery.git.writer.parquet.ParquetDbWriter;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class CommitVertexWriterTest {

    @Test
    public void testParquetWriter() throws IOException {
        File f = new File("/Users/martinsteinhauer/Desktop/disdrilleryRepo/commitDb.parquet");
        if(!f.getParentFile().exists()) {
            f.mkdirs();
        }
        if(!f.exists()) {
            f.createNewFile();
        }
        ParquetDbWriter<CommitVertex> writer = new CommitVertexDbWriter(f);
        writer.addEntry(new CommitVertex("abc123", "Martin Steinhauer", "Martin Steinhauer", new Date().getTime(), "Blublub", "",""));
        writer.write();
        writer.close();
    }
}
