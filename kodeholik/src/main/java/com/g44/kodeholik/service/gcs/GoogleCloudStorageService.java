package com.g44.kodeholik.service.gcs;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public interface GoogleCloudStorageService {
    public CompletableFuture<String> uploadVideo(byte[] fileBytes, String originalFileName, String contentType);

    public String generateUploadSignedUrl(String fileName, String contentType);

    public String generateSignedUrl(String fileName);

    void deleteFile(String fileName);
}
