package com.g44.kodeholik.controller.course;

import com.g44.kodeholik.service.gcs.GoogleCloudStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/videos")
@CrossOrigin(origins = "http://localhost:5173")
public class VideoController {

    @Autowired
    private GoogleCloudStorageService storageService;

    @GetMapping("/upload-signed-url")
    public ResponseEntity<Map<String, String>> getUploadSignedUrl(@RequestParam String fileName,
            @RequestParam String contentType) {
        if (!fileName.matches("^[a-zA-Z0-9._-]+$")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid file name"));
        }

        String signedUrl = storageService.generateUploadSignedUrl(fileName, contentType);
        return ResponseEntity.ok(Map.of("signedUrl", signedUrl));
    }

    @GetMapping("/{fileName}/signed-url")
    public ResponseEntity<String> getSignedUrl(@PathVariable String fileName) {
        String signedUrl = storageService.generateSignedUrl(fileName);
        return ResponseEntity.ok(signedUrl);
    }

}