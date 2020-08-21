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
import javax.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @author: Jesse
 * @since: 14/08/2020
 */
@Data
@Slf4j
@Service
@ConfigurationProperties(prefix = "file.location.path")
public class FileSystemStorageServiceImpl implements StorageService {

    private String images;

    private String videos;

    private String other;

    @Override
    public String upload(HttpServletRequest request, InputStream is, String filename, String contentType)
            throws IOException {
        String requestURL = request.getRequestURL().toString();
        String requestURI = request.getRequestURI();
        String url = requestURL.substring(0, requestURL.length() - requestURI.length() + 1);

        List<String> strings = Arrays.asList(filename.split("\\."));
        StringBuilder sb = new StringBuilder();
        String path = getCurrentSaveDate(contentType);
        String newFileName = sb.append(strings.get(0)).append("_").append(UUID.randomUUID()).append(".")
                .append(strings.get(1))
                .toString();
        StringBuilder sbUrl = new StringBuilder(url);
        Path targetPath = null;
        if (contentType.contains("image")) {
            sbUrl.append("images/");
            targetPath = new File(images + path, newFileName).toPath();
        } else if (contentType.contains("video")) {
            sbUrl.append("videos/");
            targetPath = new File(videos + path, newFileName).toPath();
        } else {
            sbUrl.append("other/");
            targetPath = new File(other + path, newFileName).toPath();
        }
        Files.copy(is, targetPath);

        return sbUrl.append(path).append(File.separator).append(newFileName).toString();
    }

    public String getCurrentSaveDate(String contentType) {
        String savePath = "";
        if (contentType.contains("image")) {
            savePath = images;
        } else if (contentType.contains("video")) {
            savePath = videos;
        } else {
            savePath = other;
        }
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
        return path;
    }

}
