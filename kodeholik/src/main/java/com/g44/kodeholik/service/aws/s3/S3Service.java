package com.g44.kodeholik.service.aws.s3;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.g44.kodeholik.model.enums.s3.FileNameType;

public interface S3Service {
    public void uploadFileToS3(MultipartFile multipartFile, String key);

    public String getPresignedUrl(String key);

    public boolean isObjectExist(String key);

    public boolean doesObjectExist(String key);

    public List<String> uploadFileNameTypeFile(List<MultipartFile> multipartFiles, FileNameType fileNameType);

    public void deleteFileFromS3(String key);

    public String generateFileName(FileNameType fileNameType);
}
