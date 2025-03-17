package com.g44.kodeholik.service.user.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import com.g44.kodeholik.exception.BadRequestException;
import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.dto.request.user.AddUserAvatarFileDto;
import com.g44.kodeholik.model.dto.request.user.AddUserRequestDto;
import com.g44.kodeholik.model.dto.request.user.EditProfileRequestDto;
import com.g44.kodeholik.model.dto.response.user.ProfileResponseDto;
import com.g44.kodeholik.model.entity.user.UserPrincipal;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.s3.FileNameType;
import com.g44.kodeholik.model.enums.user.NotificationType;
import com.g44.kodeholik.model.enums.user.UserStatus;
import com.g44.kodeholik.repository.user.UserRepository;
import com.g44.kodeholik.service.aws.s3.S3Service;
import com.g44.kodeholik.service.email.EmailService;
import com.g44.kodeholik.service.user.UserService;
import com.g44.kodeholik.util.mapper.request.user.AddUserAvatarFileMapper;
import com.g44.kodeholik.util.mapper.request.user.AddUserRequestMapper;
import com.g44.kodeholik.util.mapper.response.user.ProfileResponseMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AddUserRequestMapper addUserRequestMapper;

    @Mock
    private AddUserAvatarFileMapper addUserAvatarFileMapper;

    @Mock
    private EmailService emailService;

    @Mock
    private S3Service s3Service;

    @Mock
    private ProfileResponseMapper profileResponseMapper;

    @Mock
    private NotificationServiceImpl notificationService;

    @InjectMocks
    private UserServiceImpl userService;

    @Spy
    private UserService spyUserService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        Authentication authentication = Mockito.mock(Authentication.class);
        // Mockito.whens() for your authorization object
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Users mockUser = new Users();
        mockUser.setUsername("oldUser");
        mockUser.setFullname("Old Fullname");
        mockUser.setAvatar("old-avatar.jpg");

        // 🔹 Mock phương thức getCurrentUser()
        UserDetails mockUserDetails = new UserPrincipal(mockUser);

        when(authentication.getPrincipal()).thenReturn(mockUserDetails);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserByIdUserExists() {
        Users user = new Users();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Users result = userService.getUserById(1L);

        assertEquals(user, result);
    }

    @Test
    void testGetUserByIdUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void testGetCurrentUserDetails_serNotAuthenticated() {
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);

        UserDetails result = userService.getCurrentUserDetails();

        assertNull(result);
    }

    @Test
    void testAddUserAfterLoginGoogle() {
        AddUserRequestDto addUserRequestDto = new AddUserRequestDto();
        Users user = new Users();
        user.setEmail("test");
        user.setPassword("test");
        when(addUserRequestMapper.mapTo(addUserRequestDto)).thenReturn(user);
        when(userRepository.save(any(Users.class))).thenReturn(user);

        Users result = userService.addUserAfterLoginGoogle(addUserRequestDto);

        assertEquals(user, result);
        verify(emailService).sendEmailLoginGoogle(any(), any(), any(),
                any());
        verify(notificationService).saveNotification(any(Users.class), anyString(), any(), any());
    }

    @Test
    void testAddUser() {
        AddUserAvatarFileDto addUserRequestDto = new AddUserAvatarFileDto();
        Users user = new Users();
        user.setEmail("test");
        user.setPassword("test");
        List<String> uploadedFileKeys = List.of("new-avatar.jpg");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        when(addUserAvatarFileMapper.mapTo(addUserRequestDto)).thenReturn(user);
        when(s3Service.uploadFileNameTypeFile(anyList(), eq(FileNameType.AVATAR))).thenReturn(uploadedFileKeys);
        when(userRepository.save(any(Users.class))).thenReturn(user);

        Users result = userService.addUserByAdmin(addUserRequestDto);

        assertEquals(user, result);
        verify(emailService).sendEmailAddUser(any(), any(), any(),
                any());
        verify(notificationService).saveNotification(any(Users.class), anyString(), any(), any());
        verify(s3Service, times(1)).uploadFileNameTypeFile(anyList(), eq(FileNameType.AVATAR));
    }

    @Test
    void testActivateUser() {
        Users user = new Users();
        user.setStatus(UserStatus.NOT_ACTIVATED);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.activateUser(1L);

        assertEquals(UserStatus.ACTIVATED, user.getStatus());
        verify(userRepository).save(user);
    }

    @Test
    void testActivateUserAlreadyActive() {
        Users user = new Users();
        user.setStatus(UserStatus.ACTIVATED);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> userService.activateUser(1L));
    }

    @Test
    void testDeactivateUser() {
        Users user = new Users();
        user.setStatus(UserStatus.ACTIVATED);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deactivateUser(1L);

        assertEquals(UserStatus.NOT_ACTIVATED, user.getStatus());
        verify(userRepository).save(user);
    }

    @Test
    void testDeactivateUserAlreadyInactive() {
        Users user = new Users();
        user.setStatus(UserStatus.NOT_ACTIVATED);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> userService.deactivateUser(1L));
    }

    @Test
    void testBanUser() {
        Users user = new Users();
        user.setStatus(UserStatus.ACTIVATED);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.banUser(1L);

        assertEquals(UserStatus.BANNED, user.getStatus());
        verify(userRepository).save(user);
    }

    @Test
    void testBanUserAlreadyBanned() {
        Users user = new Users();
        user.setStatus(UserStatus.BANNED);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> userService.banUser(1L));
    }

    @Test
    void testUnbanUser() {
        Users user = new Users();
        user.setStatus(UserStatus.BANNED);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.unbanUser(1L);

        assertEquals(UserStatus.ACTIVATED, user.getStatus());
        verify(userRepository).save(user);
    }

    @Test
    void testUnbanUserNotBanned() {
        Users user = new Users();
        user.setStatus(UserStatus.ACTIVATED);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> userService.unbanUser(1L));
    }

    @Test
    void testEditProfile() {
        EditProfileRequestDto requestDto = new EditProfileRequestDto();
        requestDto.setUsername("oldUser");
        requestDto.setFullname("New Fullname");

        MultipartFile mockFile = mock(MultipartFile.class);
        requestDto.setAvatar(mockFile);
        Users mockUser = new Users();
        mockUser.setUsername("oldUser");
        mockUser.setFullname("Old Fullname");
        mockUser.setAvatar("old-avatar.jpg");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(mockUser));
        when(profileResponseMapper.mapFrom(any(Users.class))).thenReturn(new ProfileResponseDto());

        List<String> uploadedFileKeys = List.of("new-avatar.jpg");
        when(s3Service.uploadFileNameTypeFile(anyList(), eq(FileNameType.AVATAR))).thenReturn(uploadedFileKeys);

        ProfileResponseDto expectedProfile = new ProfileResponseDto();
        expectedProfile.setUsername("newUser");
        expectedProfile.setFullname("New Fullname");
        expectedProfile.setAvatar("new-avatar.jpg");

        when(profileResponseMapper.mapFrom(any(Users.class))).thenReturn(expectedProfile);
        when(userService.getProfileCurrentUser()).thenReturn(expectedProfile);
        HttpServletRequest request = mock(HttpServletRequest.class);

        ProfileResponseDto result = userService.editProfile(requestDto, request);

        assertNotNull(result);
        assertEquals("newUser", result.getUsername());
        assertEquals("New Fullname", result.getFullname());
        assertEquals("new-avatar.jpg", result.getAvatar());

        verify(s3Service, times(1)).uploadFileNameTypeFile(anyList(), eq(FileNameType.AVATAR));
    }

    @Test
    void testEditProfileNewUsername() {
        EditProfileRequestDto requestDto = new EditProfileRequestDto();
        requestDto.setUsername("oldUser");
        requestDto.setFullname("New Fullname");

        MultipartFile mockFile = mock(MultipartFile.class);
        requestDto.setAvatar(mockFile);
        Users mockUser = new Users();
        mockUser.setUsername("newUser");
        mockUser.setFullname("Old Fullname");
        mockUser.setAvatar("old-avatar.jpg");

        when(userRepository.findByUsername(anyString()))
                .thenReturn(Optional.of(mockUser))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(mockUser));
        when(profileResponseMapper.mapFrom(any(Users.class))).thenReturn(new ProfileResponseDto());

        List<String> uploadedFileKeys = List.of("new-avatar.jpg");
        when(s3Service.uploadFileNameTypeFile(anyList(), eq(FileNameType.AVATAR))).thenReturn(uploadedFileKeys);

        ProfileResponseDto expectedProfile = new ProfileResponseDto();
        expectedProfile.setUsername("newUser");
        expectedProfile.setFullname("New Fullname");
        expectedProfile.setAvatar("new-avatar.jpg");

        when(profileResponseMapper.mapFrom(any(Users.class))).thenReturn(expectedProfile);
        HttpServletRequest request = mock(HttpServletRequest.class);

        ProfileResponseDto result = userService.editProfile(requestDto, request);

        assertNotNull(result);
        assertEquals("newUser", result.getUsername());
        assertEquals("New Fullname", result.getFullname());
        assertEquals("new-avatar.jpg", result.getAvatar());

        verify(s3Service, times(1)).uploadFileNameTypeFile(anyList(), eq(FileNameType.AVATAR));
    }

    @Test
    void testEditProfileNewUsernameExisted() {
        EditProfileRequestDto requestDto = new EditProfileRequestDto();
        requestDto.setUsername("oldUser");
        requestDto.setFullname("New Fullname");

        MultipartFile mockFile = mock(MultipartFile.class);
        requestDto.setAvatar(mockFile);
        Users mockUser = new Users();
        mockUser.setUsername("newUser");
        mockUser.setFullname("Old Fullname");
        mockUser.setAvatar("old-avatar.jpg");

        when(userRepository.findByUsername(anyString()))
                .thenReturn(Optional.of(mockUser));
        when(profileResponseMapper.mapFrom(any(Users.class))).thenReturn(new ProfileResponseDto());

        List<String> uploadedFileKeys = List.of("new-avatar.jpg");
        when(s3Service.uploadFileNameTypeFile(anyList(), eq(FileNameType.AVATAR))).thenReturn(uploadedFileKeys);

        ProfileResponseDto expectedProfile = new ProfileResponseDto();
        expectedProfile.setUsername("newUser");
        expectedProfile.setFullname("New Fullname");
        expectedProfile.setAvatar("new-avatar.jpg");

        when(profileResponseMapper.mapFrom(any(Users.class))).thenReturn(expectedProfile);
        HttpServletRequest request = mock(HttpServletRequest.class);

        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> userService.editProfile(requestDto, request));
        assertEquals("Username already exists", badRequestException.getMessage());
        assertEquals("Username already exists", badRequestException.getDetails());
    }

    @Test
    void testGetProfileCurrentUser() {
        Users user = new Users();
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(profileResponseMapper.mapFrom(any(Users.class))).thenReturn(new ProfileResponseDto());

        ProfileResponseDto result = userService.getProfileCurrentUser();

        assertNotNull(result);
    }
}