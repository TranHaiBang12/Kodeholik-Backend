package com.g44.kodeholik.controller.course;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.g44.kodeholik.model.dto.request.user.ChangePasswordRequestDto;
import com.g44.kodeholik.model.dto.request.user.EditProfileRequestDto;
import com.g44.kodeholik.model.dto.response.user.NotificationResponseDto;
import com.g44.kodeholik.model.dto.response.user.ProfileResponseDto;
import com.g44.kodeholik.model.dto.response.user.UserResponseDto;
import com.g44.kodeholik.service.user.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @PutMapping("/edit-profile")
    public ResponseEntity<ProfileResponseDto> editProfile(
            @ModelAttribute @Valid EditProfileRequestDto editProfileRequestDto,
            HttpServletRequest request) {
        return ResponseEntity.ok(userService.editProfile(editProfileRequestDto, request));
    }

    @GetMapping("/current")
    public ResponseEntity<ProfileResponseDto> getProfileResponse() {
        return ResponseEntity.ok(userService.getProfileCurrentUser());
    }

    @GetMapping("/notifications")
    public ResponseEntity<?> getNotifications(@RequestParam int page, @RequestParam(required = false) Integer size) {
        Page<NotificationResponseDto> notifications = userService.getNotifications(page, size);
        if (notifications.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/other-profile/{id}")
    public ResponseEntity<UserResponseDto> getOtherProfile(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getOtherProfile(id));
    }

}
