package com.g44.kodeholik.service.user;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import com.g44.kodeholik.model.dto.request.user.AddUserAvatarFileDto;
import com.g44.kodeholik.model.dto.request.user.AddUserRequestDto;
import com.g44.kodeholik.model.dto.request.user.ChangePasswordRequestDto;
import com.g44.kodeholik.model.dto.request.user.EditProfileRequestDto;
import com.g44.kodeholik.model.dto.response.user.ProfileResponseDto;
import com.g44.kodeholik.model.entity.user.Users;

public interface UserService {

    public Optional<Users> isUserExistedbyUsernameOrEmail(String username);

    public Users getUserById(Long userId);

    public Users getUserByUsernameOrEmail(String username);

    public UserDetails getCurrentUserDetails();

    public Users getCurrentUser();

    public Users addUserAfterLoginGoogle(AddUserRequestDto addUserRequestDto);

    public Users addUserByAdmin(AddUserAvatarFileDto addUserAvatarFileDto);

    public void activateUser(Long userId);

    public void deactivateUser(Long userId);

    public void banUser(Long userId);

    public void unbanUser(Long userId);

    public ProfileResponseDto getProfileCurrentUser();

    public ProfileResponseDto editProfile(EditProfileRequestDto editProfileRequestDto);

    public boolean isUserNotAllowed(String username);

}
