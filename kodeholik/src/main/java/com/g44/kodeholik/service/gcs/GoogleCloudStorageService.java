package com.g44.kodeholik.service.gcs;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface GoogleCloudStorageService {
    public String uploadVideo(MultipartFile file) throws IOException;
    public String generateSignedUrl(String fileName);
}
