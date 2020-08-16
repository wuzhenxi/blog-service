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

    @Value("${file.location.path}")
    private String filePath;

    @Override
    public String upload(HttpServletRequest request, InputStream is, String filename) throws IOException {
        // 获取完整url
        String requestURL = request.getRequestURL().toString();
        // 获取请求路径url
        String requestURI = request.getRequestURI();
        // 得到去掉了uri的路径
        String url = requestURL.substring(0, requestURL.length() - requestURI.length() + 1);

        List<String> strings = Arrays.asList(filename.split("\\."));
        StringBuilder sb = new StringBuilder();
        String path = getCurrentSaveDate();
        String newFileName = sb.append(strings.get(0)).append("_").append(UUID.randomUUID()).append(".").append(strings.get(1))
                .toString();
        Path targetPath = new File(filePath + path, newFileName).toPath();
        Files.copy(is, targetPath);

        return new StringBuffer(url).append("images/").append(path).append(File.separator).append(newFileName).toString();
    }

    public String getCurrentSaveDate() {
        String savePath = filePath;
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
