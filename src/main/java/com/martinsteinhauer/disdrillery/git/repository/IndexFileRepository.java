package com.martinsteinhauer.disdrillery.git.repository;

import com.google.gson.Gson;
import com.martinsteinhauer.disdrillery.git.model.IndexFile;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.Charset;

public class IndexFileRepository {

    private final String INDEX_FILE_NAME = "index.json";
    private final File absoluteIndexFile;
    private File repositoryRoot;
    private IndexFile indexFile;
    private final Gson gson;

    public IndexFileRepository(File repositoryRoot) {
        this.gson = new Gson().newBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        this.indexFile = new IndexFile();
        this.repositoryRoot = repositoryRoot;
        StringBuilder sb = new StringBuilder();
        sb.append(repositoryRoot.getAbsolutePath()).append(File.separator).append(INDEX_FILE_NAME);
        absoluteIndexFile = new File(sb.toString());
        if(!absoluteIndexFile.exists()) {
            initIndexFile();
            saveIndexFile(indexFile);
        } else {
            this.indexFile = parseIndexFile();
        }
    }

    private IndexFile parseIndexFile() {
        try {
            String content = FileUtils.readFileToString(absoluteIndexFile, Charsets.UTF_8);
            return new Gson().fromJson(content, IndexFile.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void initIndexFile() {
        if(!absoluteIndexFile.getParentFile().exists()) {
            absoluteIndexFile.getParentFile().mkdirs();
        }
        try {
            absoluteIndexFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveIndexFile(IndexFile indexFile) {
        try {
            if(!absoluteIndexFile.exists()) {
                initIndexFile();
            }
            String content = gson.toJson(indexFile);
            FileUtils.writeStringToFile(absoluteIndexFile, content, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public IndexFile getIndexFile() {
        return indexFile;
    }

    public IndexFileRepository setIndexFile(IndexFile indexFile) {
        this.indexFile = indexFile;
        return this;
    }

    public void save() {
        if(indexFile != null) saveIndexFile(indexFile);
    }
}
