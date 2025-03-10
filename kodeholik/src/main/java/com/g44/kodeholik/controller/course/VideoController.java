package com.g44.kodeholik.controller.course;

import com.g44.kodeholik.service.gcs.GoogleCloudStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/videos")
public class VideoController {

    @Autowired
    private GoogleCloudStorageService storageService;

    @GetMapping("/upload-signed-url")
    public ResponseEntity<Map<String, String>> getUploadSignedUrl(@RequestParam String fileName, @RequestParam String contentType) {
        String signedUrl = storageService.generateUploadSignedUrl(fileName, contentType);
        Map<String, String> response = Map.of("signedUrl", signedUrl);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{fileName}/signed-url")
    public ResponseEntity<String> getSignedUrl(@PathVariable String fileName) {
        String signedUrl = storageService.generateSignedUrl("videos/" + fileName);
        return ResponseEntity.ok(signedUrl);
    }
}