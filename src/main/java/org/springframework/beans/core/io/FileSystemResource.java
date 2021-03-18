package org.springframework.beans.core.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 处理文件系统路径的资源
 */
public class FileSystemResource implements Resource {

    private final String filePath;

    public FileSystemResource(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public InputStream getInputStream() throws IOException {

        Path path = new File(this.filePath).toPath();
        return Files.newInputStream(path);
    }
}
