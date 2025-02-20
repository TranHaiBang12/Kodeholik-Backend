package com.g44.kodeholik.controller.course;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.g44.kodeholik.model.dto.request.user.EditProfileRequestDto;
import com.g44.kodeholik.model.dto.response.user.ProfileResponseDto;
import com.g44.kodeholik.service.user.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @PutMapping("/edit-profile")
    public ResponseEntity<ProfileResponseDto> editProfile(@ModelAttribute EditProfileRequestDto editProfileRequestDto) {
        return ResponseEntity.ok(userService.editProfile(editProfileRequestDto));
    }

    @GetMapping("/current")
    public ResponseEntity<ProfileResponseDto> getProfileResponse() {
        return ResponseEntity.ok(userService.getProfileCurrentUser());
    }

}
