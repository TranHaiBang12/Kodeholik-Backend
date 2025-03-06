package com.g44.kodeholik.service.gcs.impl;
import com.google.cloud.storage.*;



import com.g44.kodeholik.service.gcs.GoogleCloudStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class GoogleCloudStorageServiceImpl implements GoogleCloudStorageService {
    private final Storage storage;

    @Value("${gcs.bucket.name}")
    private String bucketName;

    @Override
    public String uploadVideo(MultipartFile file) throws IOException {
        String fileName = "videos/" + System.currentTimeMillis() + "_" + file.getOriginalFilename();

        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();

        storage.create(blobInfo, file.getBytes());
        return fileName;
    }

    @Override
    public String generateSignedUrl(String fileName) {
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, fileName).build();

        URL signedUrl = storage.signUrl(blobInfo, 30, TimeUnit.MINUTES, Storage.SignUrlOption.withV4Signature());

        return signedUrl.toString();
    }
}
