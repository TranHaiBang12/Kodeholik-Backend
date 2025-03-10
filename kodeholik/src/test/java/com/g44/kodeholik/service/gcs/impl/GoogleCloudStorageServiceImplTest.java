package com.g44.kodeholik.service.gcs.impl;

import com.google.cloud.storage.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoogleCloudStorageServiceImplTest {

    @InjectMocks
    private GoogleCloudStorageServiceImpl googleCloudStorageService;

    @Mock
    private Storage storage;

    private final String bucketName = "kodeholik"; // Định nghĩa bucketName đúng

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(googleCloudStorageService, "bucketName", bucketName);
    }


    @Test
    void uploadVideoShouldUploadFileAndReturnFileName() throws IOException {
        
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.mp4", "video/mp4", "dummy data".getBytes());

        String expectedFileName = "videos/" + System.currentTimeMillis() + "_test.mp4";
        BlobId blobId = BlobId.of(bucketName, expectedFileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("video/mp4").build();

        when(storage.create(any(BlobInfo.class), any(byte[].class))).thenReturn(mock(Blob.class));

        
        String actualFileName = googleCloudStorageService.uploadVideo(file);

        
        assertNotNull(actualFileName);
        assertTrue(actualFileName.startsWith("videos/"));
        verify(storage).create(any(BlobInfo.class), any(byte[].class));
    }

    @Test
    void generateUploadSignedUrlShouldReturnSignedUrl() {

        String fileName = "test-video.mp4";
        String expectedSignedUrl = "https://fake-signed-url.com";

        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, "videos/" + fileName)
                .setContentType("video/mp4")
                .build();

        URL url = Mockito.mock(URL.class);
        when(url.toString()).thenReturn(expectedSignedUrl);
        when(storage.signUrl(eq(blobInfo), anyLong(), any(TimeUnit.class),
                any(Storage.SignUrlOption.class), any(Storage.SignUrlOption.class)))
                .thenReturn(url);

        String actualSignedUrl = googleCloudStorageService.generateUploadSignedUrl(fileName, "video/mp4");

        assertEquals(expectedSignedUrl, actualSignedUrl);
        verify(storage).signUrl(eq(blobInfo), eq(30L), eq(TimeUnit.MINUTES),
                any(Storage.SignUrlOption.class), any(Storage.SignUrlOption.class));
    }

    @Test
    void generateSignedUrlShouldReturnSignedUrl() {

        String fileName = "videos/sample.mp4";
        String expectedSignedUrl = "https://fake-signed-url.com";

        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, fileName).build();
        URL url = Mockito.mock(URL.class);
        when(url.toString()).thenReturn(expectedSignedUrl);
        when(storage.signUrl(eq(blobInfo), anyLong(), any(TimeUnit.class), any(Storage.SignUrlOption.class)))
                .thenReturn(url);

        String actualSignedUrl = googleCloudStorageService.generateSignedUrl(fileName);

        assertEquals(expectedSignedUrl, actualSignedUrl);
        verify(storage).signUrl(eq(blobInfo), eq(30L), eq(TimeUnit.MINUTES), any(Storage.SignUrlOption.class));
    }
}
