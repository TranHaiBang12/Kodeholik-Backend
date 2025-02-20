package com.g44.kodeholik.service.user.impl;

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.g44.kodeholik.config.MessageProperties;
import com.g44.kodeholik.exception.BadRequestException;
import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.dto.request.user.AddUserAvatarFileDto;
import com.g44.kodeholik.model.dto.request.user.AddUserRequestDto;
import com.g44.kodeholik.model.dto.request.user.ChangePasswordRequestDto;
import com.g44.kodeholik.model.dto.request.user.EditProfileRequestDto;
import com.g44.kodeholik.model.dto.response.user.ProfileResponseDto;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.s3.FileNameType;
import com.g44.kodeholik.model.enums.user.UserStatus;
import com.g44.kodeholik.repository.user.UserRepository;
import com.g44.kodeholik.service.aws.s3.S3Service;
import com.g44.kodeholik.service.email.EmailService;
import com.g44.kodeholik.service.user.UserService;
import com.g44.kodeholik.util.mapper.request.user.AddUserAvatarFileMapper;
import com.g44.kodeholik.util.mapper.request.user.AddUserRequestMapper;
import com.g44.kodeholik.util.mapper.request.user.EditProfileRequestMapper;
import com.g44.kodeholik.util.mapper.response.user.ProfileResponseMapper;
import com.g44.kodeholik.util.password.PasswordUtils;
import com.g44.kodeholik.util.validation.Validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final AddUserRequestMapper addUserRequestMapper;

    private final AddUserAvatarFileMapper addUserAvatarFileMapper;

    private final EmailService emailService;

    private final S3Service s3Service;

    private final EditProfileRequestMapper editProfileRequestMapper;

    private final ProfileResponseMapper profileResponseMapper;

    private final MessageProperties messageProperties;

    @Override
    public Users getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found", "User not found"));
    }

    @Override
    public UserDetails getCurrentUserDetails() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal != null && principal instanceof UserDetails userDetails) {
            return userDetails;
        }
        return null;
    }

    private void checkUsernameExisted(String username) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new BadRequestException("Username already exists", "Username already exists");
        }
    }

    private void checkEmailExisted(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new BadRequestException("Email already exists", "Email already exists");
        }
    }

    @Override
    public Users getCurrentUser() {
        UserDetails currenUserDetails = getCurrentUserDetails();
        if (currenUserDetails != null) {
            return userRepository.findByUsername(currenUserDetails.getUsername())
                    .orElseThrow(() -> new NotFoundException("User not found", "User not found"));
        }
        return null;
    }

    @Override
    public Users addUserAfterLoginGoogle(AddUserRequestDto addUserRequestDto) {
        String password = PasswordUtils.generatePassword();
        Users user = addUser(addUserRequestDto, UserStatus.ACTIVATED, password);
        emailService.sendEmailAddUser(user.getEmail(), "[KODEHOLIK] First Time Login", user.getUsername(),
                password);
        return user;
    }

    private Users addUser(AddUserRequestDto addUserRequestDto, UserStatus userStatus, String password) {
        Users user = addUserRequestMapper.mapTo(addUserRequestDto);
        checkUsernameExisted(addUserRequestDto.getUsername());
        checkEmailExisted(addUserRequestDto.getEmail());
        user.setCreatedDate(new java.sql.Date(Date.from(Instant.now()).getTime()));
        user.setPassword(PasswordUtils.encodePassword(password));
        user.setStatus(userStatus);
        return userRepository.save(user);
    }

    private Users addUserAvatar(AddUserAvatarFileDto addUserAvatarFileDto, UserStatus userStatus, String password) {
        Users user = addUserAvatarFileMapper.mapTo(addUserAvatarFileDto);
        checkUsernameExisted(addUserAvatarFileDto.getUsername());
        checkEmailExisted(addUserAvatarFileDto.getEmail());
        user.setCreatedDate(new java.sql.Date(Date.from(Instant.now()).getTime()));
        user.setPassword(PasswordUtils.encodePassword(password));
        user.setStatus(userStatus);
        return userRepository.save(user);
    }

    @Override
    public Users getUserByUsernameOrEmail(String username) {
        return userRepository.existsByUsernameOrEmail(username)
                .orElseThrow(() -> new NotFoundException("User not found", "User not found"));
    }

    @Override
    public Users addUserByAdmin(AddUserAvatarFileDto addUserAvatarFileDto) {
        List<MultipartFile> multipartFiles = new ArrayList();
        multipartFiles.add(addUserAvatarFileDto.getAvatarFile());

        String password = PasswordUtils.generatePassword();
        String avatarKey = s3Service.uploadFileNameTypeFile(multipartFiles, FileNameType.AVATAR).get(0);
        addUserAvatarFileDto.setAvatar(avatarKey);
        Users user = addUserAvatar(addUserAvatarFileDto, UserStatus.ACTIVATED, password);

        emailService.sendEmailAddUser(user.getEmail(), "[KODEHOLIK] Account Created", user.getUsername(),
                password);
        return user;
    }

    @Override
    public void activateUser(Long userId) {
        Users user = getUserById(userId);
        if (user.getStatus() == UserStatus.ACTIVATED) {
            throw new BadRequestException("This user is already active", "This user is already active");
        }
        user.setStatus(UserStatus.ACTIVATED);
        userRepository.save(user);
    }

    @Override
    public void deactivateUser(Long userId) {
        Users user = getUserById(userId);
        if (user.getStatus() == UserStatus.NOT_ACTIVATED) {
            throw new BadRequestException("This user is already inactive", "This user is already inactive");
        }
        user.setStatus(UserStatus.NOT_ACTIVATED);
        userRepository.save(user);
    }

    @Override
    public void banUser(Long userId) {
        Users user = getUserById(userId);
        if (user.getStatus() == UserStatus.BANNED) {
            throw new BadRequestException("This user is already banned", "This user is already banned");
        }
        user.setStatus(UserStatus.BANNED);
        userRepository.save(user);
    }

    @Override
    public void unbanUser(Long userId) {
        Users user = getUserById(userId);
        if (user.getStatus() != UserStatus.BANNED) {
            throw new BadRequestException("This user currently is not banned", "This user currently is not banned");
        }
        user.setStatus(UserStatus.ACTIVATED);
        userRepository.save(user);
    }

    @Override
    public ProfileResponseDto editProfile(EditProfileRequestDto editProfileRequestDto) {
        Users user = getCurrentUser();

        if (!user.getUsername().equals(editProfileRequestDto.getUsername())) {
            checkUsernameExisted(editProfileRequestDto.getUsername());
        }

        List<MultipartFile> multipartFiles = new ArrayList();
        multipartFiles.add(editProfileRequestDto.getAvatar());
        String avatarKey = s3Service.uploadFileNameTypeFile(multipartFiles, FileNameType.AVATAR).get(0);

        user.setAvatar(avatarKey);
        user.setFullname(editProfileRequestDto.getFullname());
        user.setUsername(editProfileRequestDto.getUsername());
        return getProfileCurrentUser();
    }

    @Override
    public ProfileResponseDto getProfileCurrentUser() {
        Users user = getCurrentUser();
        return profileResponseMapper.mapFrom(user);
    }

    @Override
    public Optional<Users> isUserExistedbyUsernameOrEmail(String username) {
        return userRepository.existsByUsernameOrEmail(username);
    }

    @Override
    public boolean isUserNotAllowed(String username) {
        return userRepository.isUserNotAllowed(username);
    }

}
