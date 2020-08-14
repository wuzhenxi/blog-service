package com.wzx.service.impl;

import com.wzx.service.StorageService;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @author: Jesse
 * @since: 14/08/2020
 */
@Data
@Slf4j
@Service
public class FileSystemStorageServiceImpl implements StorageService {

    @Override
    public String upload(InputStream is, String filename) throws IOException {
        List<String> strings = Arrays.asList(filename.split("\\."));
        StringBuilder sb = new StringBuilder();
        String savePath = getCurrentSaveDate();
        String newFileName = sb.append(strings.get(0)).append("_").append(UUID.randomUUID()).append(".").append(strings.get(1))
                .toString();
        Path targetPath = new File(savePath, newFileName).toPath();
        Files.copy(is, targetPath);
        return targetPath.getFileName().toAbsolutePath().toString();
    }

    public String getCurrentSaveDate() {
        String savePath = System.getProperty("user.dir") + File.separator + "contents"
                + File.separator;
        StringBuilder sb = new StringBuilder();
        LocalDateTime now = LocalDateTime.now();
        String path = sb.append(now.getYear()).append(File.separator).append(now.getMonthValue())
                .append(File.separator).append(now.getDayOfMonth()).toString();
        if (!savePath.endsWith(File.separator)) {
            savePath += File.separator;
        }
        savePath += path;
        File file = new File(savePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return savePath;
    }

}
