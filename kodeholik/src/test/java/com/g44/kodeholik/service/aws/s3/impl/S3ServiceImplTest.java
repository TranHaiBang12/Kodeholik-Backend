package com.g44.kodeholik.service.aws.s3.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.exception.S3Exception;
import com.g44.kodeholik.model.enums.s3.FileNameType;
import com.g44.kodeholik.util.file.FileConvert;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@ExtendWith(MockitoExtension.class)
public class S3ServiceImplTest {

    @Mock
    private S3Client s3Client;

    @Mock
    private S3Presigner s3Presigner;

    private MultipartFile multipartFile;

    @InjectMocks
    private S3ServiceImpl s3ServiceImpl;

    @BeforeEach
    public void setUp() {
        s3ServiceImpl = new S3ServiceImpl(s3Client, s3Presigner);
        multipartFile = new MockMultipartFile("file", "test.txt", "text/plain", "Hello, World!".getBytes());

    }

    @Test
    public void testUploadFileToS3() throws Exception {
        // Tạo file tạm
        File tempFile = Files.createTempFile("test", ".txt").toFile();
        assertNotNull(tempFile); // Kiểm tra file đã được tạo

        // Mock static method của FileConvert
        try (MockedStatic<FileConvert> mockedFileConvert = mockStatic(FileConvert.class)) {
            mockedFileConvert.when(() -> FileConvert.convertMultiPartToFile(multipartFile))
                    .thenReturn(tempFile);

            // Mock các hành vi của multipartFile
            // when(multipartFile.getContentType()).thenReturn("text/plain");

            // Mock hành vi của S3 Client
            when(s3Client.putObject(ArgumentMatchers.<PutObjectRequest>any(), ArgumentMatchers.<Path>any()))
                    .thenReturn(mock(PutObjectResponse.class));

            // Gọi hàm cần test
            s3ServiceImpl.uploadFileToS3(multipartFile, "test-key");

            // Kiểm tra S3 đã gọi putObject đúng số lần
            verify(s3Client, times(1))
                    .putObject(ArgumentMatchers.<PutObjectRequest>any(),
                            ArgumentMatchers.<Path>any());

            // Đảm bảo file bị xóa sau khi test
        }
    }

    @Test
    public void testDoesObjectExist() {
        when(s3Client.headObject(any(HeadObjectRequest.class))).thenReturn(null);

        boolean exists = s3ServiceImpl.doesObjectExist("test-key");

        assertTrue(exists);
    }

    @Test
    public void testDoesObjectExist_NotFound() {
        when(s3Client.headObject(any(HeadObjectRequest.class))).thenThrow(NoSuchKeyException.class);

        assertThrows(NotFoundException.class, () -> s3ServiceImpl.doesObjectExist("test-key"));
    }

    @Test
    public void testGetPresignedUrl() throws MalformedURLException {
        when(s3Client.headObject(any(HeadObjectRequest.class))).thenReturn(null);
        PresignedGetObjectRequest presignedRequest = mock(PresignedGetObjectRequest.class);
        when(presignedRequest.url()).thenReturn(new java.net.URL("http://example.com"));
        when(s3Presigner.presignGetObject(any(GetObjectPresignRequest.class))).thenReturn(presignedRequest);

        String url = s3ServiceImpl.getPresignedUrl("test-key");

        assertEquals("http://example.com", url);
    }

    @Test
    public void testUploadFileNameTypeFile() throws Exception {
        File file = Files.createTempFile("test", ".txt").toFile();
        try (MockedStatic<FileConvert> mockedFileConvert = mockStatic(FileConvert.class)) {
            mockedFileConvert.when(() -> FileConvert.convertMultiPartToFile(multipartFile))
                    .thenReturn(file);
            when(s3Client.putObject(any(PutObjectRequest.class), any(Path.class)))
                    .thenReturn(mock(PutObjectResponse.class));

            List<String> keys = s3ServiceImpl.uploadFileNameTypeFile(Arrays.asList(multipartFile),
                    FileNameType.PROBLEM);

            assertEquals(1, keys.size());
            verify(s3Client, times(1)).putObject(any(PutObjectRequest.class), any(Path.class));
        }
    }

    @Test
    public void testDeleteFileFromS3() {
        when(s3Client.headObject(any(HeadObjectRequest.class))).thenReturn(null);

        s3ServiceImpl.deleteFileFromS3("test-key");

        verify(s3Client, times(1)).deleteObject(any(DeleteObjectRequest.class));
    }

    @Test
    public void testGenerateFileName() {
        String fileName = s3ServiceImpl.generateFileName(FileNameType.PROBLEM);
        assertTrue(fileName.startsWith("kodeholik-problem-image-"));

        fileName = s3ServiceImpl.generateFileName(FileNameType.COURSE);
        assertTrue(fileName.startsWith("kodeholik-course-image-"));

        fileName = s3ServiceImpl.generateFileName(FileNameType.AVATAR);
        assertTrue(fileName.startsWith("kodeholik-avatar-image-"));
    }
}