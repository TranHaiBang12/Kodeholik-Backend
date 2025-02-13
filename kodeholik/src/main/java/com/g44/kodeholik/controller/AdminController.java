package com.g44.kodeholik.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.g44.kodeholik.model.dto.request.setting.AddTagRequestDto;
import com.g44.kodeholik.model.dto.request.setting.EditTagRequestDto;
import com.g44.kodeholik.model.dto.request.user.AddUserRequestDto;
import com.g44.kodeholik.model.enums.setting.TagType;
import com.g44.kodeholik.service.setting.TagService;
import com.g44.kodeholik.service.user.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final UserService userService;

    private final TagService tagService;

    @PostMapping("/add-user")
    public ResponseEntity<?> addUser(@RequestBody AddUserRequestDto addUserRequestDto) {
        userService.addUserByAdmin(addUserRequestDto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/activate-user/{userId}")
    public ResponseEntity<?> activateUser(@PathVariable Long userId) {
        userService.activateUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/deactivate-user/{userId}")
    public ResponseEntity<?> deactivateUser(@PathVariable Long userId) {
        userService.deactivateUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/add-tag")
    public ResponseEntity<?> addTag(@RequestBody AddTagRequestDto addTagRequestDto) {
        tagService.addTag(addTagRequestDto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/add-tag")
    public ResponseEntity<?> editTag(@RequestBody EditTagRequestDto editTagRequestDto) {
        tagService.editTag(editTagRequestDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete-tag/{id}")
    public ResponseEntity<?> deleteTag(@PathVariable Long id, @RequestParam TagType tagType) {
        tagService.deleteTag(id, tagType);
        return ResponseEntity.noContent().build();
    }
}
