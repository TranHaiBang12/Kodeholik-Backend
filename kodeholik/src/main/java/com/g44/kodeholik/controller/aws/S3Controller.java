package com.g44.kodeholik.controller.aws;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.g44.kodeholik.model.enums.s3.FileNameType;
import com.g44.kodeholik.service.aws.s3.S3Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/s3")
public class S3Controller {

    private final S3Service s3Service;

    @PostMapping("/upload")
    public ResponseEntity<List<String>> createProblem(@RequestPart("file") MultipartFile[] multipartFiles) {
        return new ResponseEntity<>(
                s3Service.uploadFileNameTypeFile(Arrays.asList(multipartFiles), FileNameType.PROBLEM),
                HttpStatus.OK);
    }

    @GetMapping("/presigned-url")
    public ResponseEntity<String> getPresignedUrl(@RequestParam String key) {
        return new ResponseEntity<>(s3Service.getPresignedUrl(key), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteFile(@RequestParam String key) {
        s3Service.deleteFileFromS3(key);
        return ResponseEntity.noContent().build();
    }

}
