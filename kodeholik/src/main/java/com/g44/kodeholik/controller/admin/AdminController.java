package com.g44.kodeholik.controller.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.g44.kodeholik.model.dto.request.setting.AddTagRequestDto;
import com.g44.kodeholik.model.dto.request.setting.EditTagRequestDto;
import com.g44.kodeholik.model.dto.request.setting.FilterTagRequestDto;
import com.g44.kodeholik.model.dto.request.user.AddUserAvatarFileDto;
import com.g44.kodeholik.model.dto.request.user.AddUserRequestDto;
import com.g44.kodeholik.model.dto.request.user.EditUserAvatarFileDto;
import com.g44.kodeholik.model.dto.request.user.FilterUserRequestDto;
import com.g44.kodeholik.model.dto.response.setting.TagResponseDto;
import com.g44.kodeholik.model.dto.response.user.ProfileResponseDto;
import com.g44.kodeholik.model.enums.setting.TagType;
import com.g44.kodeholik.service.setting.TagService;
import com.g44.kodeholik.service.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final UserService userService;

    private final TagService tagService;

    @PostMapping("/list-user")
    public ResponseEntity<Page<ProfileResponseDto>> getListUsers(
            @RequestBody FilterUserRequestDto filterUserRequestDto) {
        Page<ProfileResponseDto> results = userService.getListOfUsers(filterUserRequestDto);
        if (results.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(results);
    }

    @PostMapping("/add-user")
    public ResponseEntity<?> addUser(
            @ModelAttribute @Valid AddUserAvatarFileDto addUserAvatarFileDto) {
        userService.addUserByAdmin(addUserAvatarFileDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ProfileResponseDto> getUserDetail(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserFromIdByAdmin(id));
    }

    @PutMapping("/edit-user/{id}")
    public ResponseEntity<ProfileResponseDto> editUser(@PathVariable Long id,
            @ModelAttribute EditUserAvatarFileDto editUserAvatarFileDto) {
        return ResponseEntity.ok(userService.editUserByAdmin(id, editUserAvatarFileDto));
    }

    @PutMapping("/activate-user/{userId}")
    public ResponseEntity<?> activateUser(@PathVariable Long userId) {
        userService.activateUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/deactivate-user/{userId}")
    public ResponseEntity<?> deactivateUser(@PathVariable Long userId) {
        userService.deactivateUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/ban-user/{userId}")
    public ResponseEntity<?> banUser(@PathVariable Long userId) {
        userService.banUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/unban-user/{userId}")
    public ResponseEntity<?> unbanUser(@PathVariable Long userId) {
        userService.unbanUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/list-tag")
    public ResponseEntity<Page<TagResponseDto>> getListTag(
            @RequestBody FilterTagRequestDto filterTagRequestDto) {
        Page<TagResponseDto> result = tagService.getListTag(filterTagRequestDto);
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/add-tag")
    public ResponseEntity<?> addTag(@RequestBody AddTagRequestDto addTagRequestDto) {
        tagService.addTag(addTagRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/edit-tag/{id}")
    public ResponseEntity<?> editTag(@PathVariable Long id, @RequestBody EditTagRequestDto editTagRequestDto) {
        tagService.editTag(id, editTagRequestDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete-tag/{id}")
    public ResponseEntity<?> deleteTag(@PathVariable Long id, @RequestParam TagType tagType) {
        tagService.deleteTag(id, tagType);
        return ResponseEntity.noContent().build();
    }
}
