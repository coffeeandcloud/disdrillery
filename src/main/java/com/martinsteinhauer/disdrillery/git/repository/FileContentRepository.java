package com.martinsteinhauer.disdrillery.git.repository;

import com.martinsteinhauer.disdrillery.git.model.FileContentVertex;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class FileContentRepository extends Repository {

    private File fileContentRoot;

    public FileContentRepository(File root) {
        super(root);
        init();
    }

    private void init() {
        StringBuilder sb = new StringBuilder();
        sb.append(getRoot().getAbsolutePath()).append(File.separator).append("content");
        fileContentRoot = new File(sb.toString());
        fileContentRoot.mkdirs();
    }

    public void saveCommitFileContent(FileContentVertex contentVertex, InputStream inputStream) {
        try {
            //File commitShaFolder = createCommitShaFolder(contentVertex.getCommitSha());
            File contentFile = new File(FilenameUtils.concat(fileContentRoot.getAbsolutePath(), contentVertex.getObjectSha()));
            if(contentFile.exists()) return;
            else {
                contentFile.createNewFile();
                FileUtils.copyToFile(inputStream, contentFile);
            }

            //File contentFile = createCommitContentFileSha(contentVertex.getObjectSha(), fileContentRoot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File createCommitShaFolder(String commitSha) {
        File commitContentFolder = new File(FilenameUtils.concat(fileContentRoot.getAbsolutePath(), commitSha));
        if(fileContentRoot.exists() && fileContentRoot.isDirectory()) {
            commitContentFolder.mkdirs();
        }
        return commitContentFolder;
    }

    private File createCommitContentFileSha(String objectSha, File commitShaFolder) throws IOException {
        File contentFile = new File(FilenameUtils.concat(commitShaFolder.getAbsolutePath(), objectSha));
        if(!contentFile.exists()) {
            contentFile.createNewFile();
        }
        return contentFile;
    }
}
