package com.g44.kodeholik.controller.course;

import com.g44.kodeholik.service.gcs.GoogleCloudStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/videos")
public class VideoController {

    @Autowired
    private GoogleCloudStorageService storageService;

//    @GetMapping("/upload-video-url")
//    public

    @GetMapping("/{fileName}/signed-url")
    public ResponseEntity<String> getSignedUrl(@PathVariable String fileName) {
        String signedUrl = storageService.generateSignedUrl("videos/" + fileName);
        return ResponseEntity.ok(signedUrl);
    }
}