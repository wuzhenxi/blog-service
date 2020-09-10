package com.wzx.service.impl;

import com.wzx.constants.ConstantsUtils;
import com.wzx.constants.EnumValues.EnumFileTypeToPath;
import com.wzx.service.StorageService;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
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

    @Value("${server.port}")
    private String serverPort;

    private String images;

    private String videos;

    private String other;

    @Override
    public String upload(HttpServletRequest request, InputStream is, String filename, String contentType)
            throws IOException {
        String requestURL = request.getRequestURL().toString();
        String requestURI = request.getRequestURI();
        String url = requestURL.substring(0, requestURL.length() - requestURI.length());
        // 带上端口返回避免多台实例，文件404
        if (!url.contains(serverPort)) {
            url += ":" + serverPort;
        }
        List<String> strings = Arrays.asList(filename.split("\\."));
        StringBuilder sb = new StringBuilder();
        String path = getCurrentSaveDate(contentType);
        String newFileName = sb.append(strings.get(0)).append("_").append(UUID.randomUUID()).append(".")
                .append(strings.get(1))
                .toString();
        StringBuilder sbUrl = new StringBuilder(url);
        Path targetPath = null;
        if (contentType.contains(ConstantsUtils.FILE_IMAGE_TYPE)) {
            sbUrl.append(ConstantsUtils.FILE_IMAGE_URL);
            targetPath = new File(images + path, newFileName).toPath();
        } else if (contentType.contains(ConstantsUtils.FILE_VIDEO_TYPE)) {
            sbUrl.append(ConstantsUtils.FILE_VIDEO_URL);
            targetPath = new File(videos + path, newFileName).toPath();
        } else {
            sbUrl.append(ConstantsUtils.FILE_OTHER_URL);
            targetPath = new File(other + path, newFileName).toPath();
        }
        Files.copy(is, targetPath);

        return sbUrl.append(path).append(File.separator).append(newFileName).toString();
    }

    @Override
    public boolean deleteFile(String fileUrl) {
        boolean deleteFlag = false;
        Optional<EnumFileTypeToPath> result = Stream.of(EnumFileTypeToPath.values())
                .filter(item -> Arrays.asList(fileUrl.split(item.getFilePath())).size() == 2).findFirst();
        if (result.isPresent()) {
            String fileAbsolutePath = "";
            EnumFileTypeToPath enumFileTypeToPath = result.get();
            String filePath = enumFileTypeToPath.getFilePath();
            if (ConstantsUtils.FILE_IMAGE_URL.equals(filePath)) {
                fileAbsolutePath = images + Arrays.asList(fileUrl.split(filePath)).get(1);
            } else if (ConstantsUtils.FILE_VIDEO_URL.equals(filePath)) {
                fileAbsolutePath = videos + Arrays.asList(fileUrl.split(filePath)).get(1);
            } else {
                fileAbsolutePath = other + Arrays.asList(fileUrl.split(filePath)).get(1);
            }
            File file = new File(fileAbsolutePath);
            if (file.exists()) {
                try {
                    Files.delete(new File(fileAbsolutePath).toPath());
                    deleteFlag = true;
                } catch (IOException e) {
                    log.error("fileName:{} delete failed.Err:{},Excption:{}", fileUrl, e, e.getMessage());
                }
            }
        }
        return deleteFlag;
    }

    public String getCurrentSaveDate(String contentType) {
        String savePath = "";
        if (contentType.contains(ConstantsUtils.FILE_IMAGE_TYPE)) {
            savePath = images;
        } else if (contentType.contains(ConstantsUtils.FILE_VIDEO_TYPE)) {
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
