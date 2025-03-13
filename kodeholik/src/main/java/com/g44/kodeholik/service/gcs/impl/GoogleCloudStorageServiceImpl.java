package com.g44.kodeholik.service.gcs.impl;

import com.google.cloud.storage.*;

import com.g44.kodeholik.service.gcs.GoogleCloudStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class GoogleCloudStorageServiceImpl implements GoogleCloudStorageService {
    private final Storage storage;

    @Value("${gcs.bucket.name}")
    private String bucketName;

    private static final String VIDEO_PREFIX = "videos/";

    @Async
    @Override
    public String uploadVideo(MultipartFile file) throws IOException {
        if (storage.get(bucketName) == null) {
            throw new IllegalStateException("Bucket " + bucketName + " does not exist.");
        }

        String safeFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String filePath = VIDEO_PREFIX + safeFileName;

        BlobId blobId = BlobId.of(bucketName, filePath);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();

        // Dùng InputStream để tránh tải toàn bộ file vào RAM
        try (InputStream inputStream = file.getInputStream()) {
            storage.create(blobInfo, inputStream);
        }

        return filePath;
    }

    @Override
    public String generateUploadSignedUrl(String fileName, String contentType) {
        String filePath = VIDEO_PREFIX + fileName;

        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, filePath)
                .setContentType(contentType)
                .build();

        URL signedUrl = storage.signUrl(blobInfo, 30, TimeUnit.MINUTES,
                Storage.SignUrlOption.httpMethod(HttpMethod.PUT),
                Storage.SignUrlOption.withV4Signature());

        return signedUrl.toString();
    }

    @Override
    public String generateSignedUrl(String fileName) {
        String filePath = VIDEO_PREFIX + fileName;
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, filePath).build();

        URL signedUrl = storage.signUrl(blobInfo, 30, TimeUnit.MINUTES, Storage.SignUrlOption.withV4Signature());

        return signedUrl.toString();
    }

    @Override
    public void deleteFile(String fileName) {
        System.out.println("Deleting file: " +fileName);  // Debug fileName
        BlobId blobId = BlobId.of(bucketName, fileName);
        boolean deleted = storage.delete(blobId);
        if (!deleted) {
            throw new RuntimeException("Failed to delete file: " + fileName);
        }
    }

}
