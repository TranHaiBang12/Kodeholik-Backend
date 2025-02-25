package com.g44.kodeholik.controller.tag;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.g44.kodeholik.model.entity.setting.Skill;
import com.g44.kodeholik.service.setting.TagService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tag")
public class TagController {

    private final TagService tagService;

    @GetMapping("/all-skill")
    public ResponseEntity<List<String>> getAllSkill() {
        return ResponseEntity.ok(tagService.getAllSkills());
    }

    @GetMapping("/all-topic")
    public ResponseEntity<List<String>> getAllTopic() {
        return ResponseEntity.ok(tagService.getAllTopics());
    }
}
