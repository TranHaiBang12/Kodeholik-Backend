package com.g44.kodeholik.service.aws.s3.impl;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.exception.S3Exception;
import com.g44.kodeholik.model.enums.s3.FileNameType;
import com.g44.kodeholik.service.aws.s3.S3Service;
import com.g44.kodeholik.util.file.FileConvert;
import com.g44.kodeholik.util.uuid.UUIDGenerator;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    @Value("${aws.s3.access-key}")
    private String accessKey;

    @Value("${aws.s3.secret-key}")
    private String secretKey;

    @Value("${aws.s3.region}")
    private String region;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.s3.presigned-duration}")
    private int presignedUrlDuration;

    private final S3Client s3Client;

    private final S3Presigner s3Presigner;

    @Async("s3TaskExecutor")
    @Override
    public void uploadFileToS3(MultipartFile multipartFile, String key) {
        try {
            // Tạo file tạm trên server để upload lên S3
            File file = FileConvert.convertMultiPartToFile(multipartFile);

            // Tải file lên S3
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key) // Tên file trong S3
                    .contentType(multipartFile.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, file.toPath());

            // Xóa file tạm sau khi upload
            try {
                java.nio.file.Files.delete(file.toPath());
            } catch (IOException e) {
                throw new S3Exception(e.getMessage(),
                        "Error deleting temporary file");
            }
        } catch (Exception e) {
            throw new S3Exception(e.getMessage(), "Error pushing file");
        }
    }

    @Override
    public boolean isObjectExist(String key) {
        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            s3Client.headObject(headObjectRequest);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean doesObjectExist(String key) {
        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            s3Client.headObject(headObjectRequest);
            return true;
        } catch (NoSuchKeyException e) {
            throw new NotFoundException("Key not found", "Key not found");
        } catch (Exception e) {
            throw new NotFoundException("Key not found", "Key not found");
        }
    }

    @Override
    public String getPresignedUrl(String key) {
        doesObjectExist(key);
        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(presignedUrlDuration))
                .getObjectRequest(r -> r.bucket(bucketName).key(key))
                .build();

        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(getObjectPresignRequest);

        return presignedRequest.url().toString();
    }

    @Override
    public List<String> uploadFileNameTypeFile(List<MultipartFile> multipartFiles, FileNameType fileNameType) {
        List<String> keys = new ArrayList();
        IntStream.range(0, multipartFiles.size()).parallel().forEach(i -> {
            String key = generateFileName(fileNameType);
            keys.add(key);
            uploadFileToS3(multipartFiles.get(i), key);
        });
        return keys;
    }

    @Override
    public void deleteFileFromS3(String key) {
        doesObjectExist(key);
        try {
            // Tạo yêu cầu xóa file
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName) // Tên bucket
                    .key(key) // Key của file cần xóa
                    .build();

            // Gọi phương thức xóa file
            s3Client.deleteObject(deleteObjectRequest);
        } catch (Exception e) {
            // Xử lý lỗi nếu có
            throw new S3Exception("Delete error", "Delete error");
        }
    }

    @Override
    public String generateFileName(FileNameType fileNameType) {
        if (fileNameType == FileNameType.PROBLEM) {
            return "kodeholik-problem-image-" + UUIDGenerator.generateUUID();
        } else if (fileNameType == FileNameType.COURSE) {
            return "kodeholik-course-image-" + UUIDGenerator.generateUUID();
        } else if (fileNameType == FileNameType.AVATAR) {
            return "kodeholik-avatar-image-" + UUIDGenerator.generateUUID();
        }
        return "";
    }

}
