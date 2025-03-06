package com.g44.kodeholik.service.auth.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import java.util.Date;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import com.g44.kodeholik.config.MessageProperties;
import com.g44.kodeholik.exception.BadRequestException;
import com.g44.kodeholik.exception.ForbiddenException;
import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.exception.UnauthorizedException;
import com.g44.kodeholik.model.dto.request.user.AddUserRequestDto;
import com.g44.kodeholik.model.dto.request.user.ChangePasswordRequestDto;
import com.g44.kodeholik.model.dto.request.user.LoginRequestDto;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.token.TokenType;
import com.g44.kodeholik.repository.user.UserRepository;
import com.g44.kodeholik.service.email.EmailService;
import com.g44.kodeholik.service.redis.RedisService;
import com.g44.kodeholik.service.token.TokenService;
import com.g44.kodeholik.service.user.UserService;
import com.g44.kodeholik.util.password.PasswordUtils;
import com.g44.kodeholik.util.validation.Validation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @Mock
    private EmailService emailService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private RedisService redisService;

    @Mock
    private UserService userService;

    @Mock
    private MessageProperties messageProperties;

    @InjectMocks
    private AuthServiceImpl authServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testVerifySuccess() {
        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setUsername("testUser");
        loginRequest.setPassword("testPassword");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);

        boolean result = authServiceImpl.verify(loginRequest);
        assertTrue(result);
    }

    @Test
    public void testVerifyFailed() {
        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setUsername("testUser");
        loginRequest.setPassword("testPassword");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        boolean result = authServiceImpl.verify(loginRequest);
        assertFalse(result);
    }

    @Test
    public void testLoginNormalSuccessfull() {
        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setUsername("testUser");
        loginRequest.setPassword("testPassword");

        HttpServletResponse response = mock(HttpServletResponse.class);

        Users user = new Users();
        user.setUsername("testUser");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);
        when(userRepository.existsByUsernameOrEmail(anyString())).thenReturn(Optional.of(user));
        when(userRepository.isUserNotAllowed(anyString())).thenReturn(false);
        when(tokenService.generateAccessToken(anyString())).thenReturn("accessToken");
        when(tokenService.generateRefreshToken(anyString(), any(Date.class))).thenReturn("refreshToken");
        authServiceImpl.loginNormal(loginRequest, response);

        verify(tokenService).addTokenToCookie(eq("accessToken"), eq(response), eq(TokenType.ACCESS));
        verify(tokenService).addTokenToCookie(eq("refreshToken"), eq(response), eq(TokenType.REFRESH));
    }

    @Test
    public void testLoginNormalThrowForbiddenException() {
        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setUsername("testUser");
        loginRequest.setPassword("testPassword");

        HttpServletResponse response = mock(HttpServletResponse.class);

        Users user = new Users();
        user.setUsername("testUser");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);
        when(userRepository.existsByUsernameOrEmail(anyString())).thenReturn(Optional.of(user));
        when(userRepository.isUserNotAllowed(anyString())).thenReturn(true);

        ForbiddenException forbiddenException = assertThrows(
                ForbiddenException.class,
                () -> authServiceImpl.loginNormal(loginRequest, response));
        assertEquals("This account is not allowed to do this action", forbiddenException.getMessage());
        assertEquals("This account is not allowed to do this action", forbiddenException.getDetails());
    }

    @Test
    public void testLoginNormalThrowNotFoundException() {
        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setUsername("testUser");
        loginRequest.setPassword("testPassword");

        HttpServletResponse response = mock(HttpServletResponse.class);

        Users user = new Users();
        user.setUsername("testUser");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);
        when(userRepository.existsByUsernameOrEmail(anyString()))
                .thenThrow(new NotFoundException("User not found", "User not found"));

        NotFoundException notFoundException = assertThrows(
                NotFoundException.class,
                () -> authServiceImpl.loginNormal(loginRequest, response));
        assertEquals("User not found", notFoundException.getMessage());
        assertEquals("User not found", notFoundException.getDetails());
    }

    @Test
    public void testCheckUsernameExistsSuccessFull() {
        Users user = new Users();
        user.setUsername("testUser");

        when(userRepository.existsByUsernameOrEmail(anyString())).thenReturn(Optional.of(user));

        Users result = authServiceImpl.checkUsernameExists("testUser");
        assertEquals("testUser", result.getUsername());
    }

    @Test
    public void testCheckUsernameExistsNotFound() {
        Users user = new Users();
        user.setUsername("testUser");

        when(userRepository.existsByUsernameOrEmail(anyString())).thenReturn(Optional.empty());
        assertThrows(
                NotFoundException.class,
                () -> authServiceImpl.checkUsernameExists("testUser"));
    }

    @Test
    public void testResetPasswordInit() {
        Users user = new Users();
        user.setUsername("testUser");
        user.setEmail("test@example.com");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(userRepository.isUserNotAllowed(anyString())).thenReturn(false);
        when(tokenService.generateForgotPasswordToken(anyString())).thenReturn("token");

        authServiceImpl.resetPasswordInit("test@example.com");

        verify(redisService).saveToken(eq("testUser"), eq("token"), eq(0L), eq(TokenType.FORGOT));
        verify(emailService).sendEmailResetPassword(eq("test@example.com"), anyString(), eq("testUser"), anyString());
    }

    @Test
    public void testResetPasswordInitThrowForbiddenException() {
        Users user = new Users();
        user.setUsername("testUser");
        user.setEmail("test@example.com");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(userRepository.isUserNotAllowed(anyString())).thenReturn(true);
        when(tokenService.generateForgotPasswordToken(anyString())).thenReturn("token");

        ForbiddenException forbiddenException = assertThrows(
                ForbiddenException.class,
                () -> authServiceImpl.resetPasswordInit("test@example.com"));
        assertEquals("This account is not allowed to do this action", forbiddenException.getMessage());
        assertEquals("This account is not allowed to do this action", forbiddenException.getDetails());
    }

    @Test
    public void testResetPasswordInitThrowBadRequestException() {
        Users user = new Users();
        user.setUsername("testUser");
        user.setEmail("test@example.com");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        BadRequestException badRequestException = assertThrows(
                BadRequestException.class,
                () -> authServiceImpl.resetPasswordInit("test@example.com"));
        assertEquals("Email not found", badRequestException.getMessage());
        assertEquals("Email not found", badRequestException.getDetails());
    }

    @Test
    public void testCheckValidForgotPasswordTokenSuccessful() {
        String token = "token";
        String username = "testUser";
        Users user = new Users();
        user.setUsername(username);

        when(tokenService.extractUsername(anyString())).thenReturn(username);
        when(userRepository.isUserNotAllowed(anyString())).thenReturn(false);
        when(userRepository.existsByUsernameOrEmail(anyString())).thenReturn(Optional.of(user));
        when(redisService.getToken(anyString(), eq(TokenType.FORGOT))).thenReturn(token);
        when(tokenService.validateToken(anyString())).thenReturn(true);

        boolean result = authServiceImpl.checkValidForgotPasswordToken(token);
        assertTrue(result);
    }

    @Test
    public void testCheckValidForgotPasswordTokenUserNotAllowed() {
        String token = "token";
        String username = "testUser";

        when(tokenService.extractUsername(anyString())).thenReturn(username);
        when(userRepository.isUserNotAllowed(anyString())).thenReturn(true);

        ForbiddenException forbiddenException = assertThrows(
                ForbiddenException.class,
                () -> authServiceImpl.checkValidForgotPasswordToken(token));
        assertEquals("This account is not allowed to do this action", forbiddenException.getMessage());
        assertEquals("This account is not allowed to do this action", forbiddenException.getDetails());
    }

    @Test
    public void testCheckValidForgotPasswordTokenEmailNotExisted() {
        String token = "token";
        String username = "testUser";

        String message = "Invalid or expired reset link. Please try again";
        when(messageProperties.getMessage("MSG07")).thenReturn(message);

        when(tokenService.extractUsername(anyString())).thenReturn(username);
        when(userRepository.isUserNotAllowed(anyString())).thenReturn(false);
        when(userRepository.existsByUsernameOrEmail(anyString())).thenReturn(Optional.empty());

        UnauthorizedException unauthorizedException = assertThrows(
                UnauthorizedException.class,
                () -> authServiceImpl.checkValidForgotPasswordToken(token));
        assertEquals(message, unauthorizedException.getMessage());
        assertEquals("Invalid or expired token", unauthorizedException.getDetails());
    }

    @Test
    public void testCheckValidForgotPasswordTokenNotSaveToken() {
        String token = "token";
        String username = "testUser";

        String message = "Invalid or expired reset link. Please try again";
        when(messageProperties.getMessage("MSG07")).thenReturn(message);

        when(tokenService.extractUsername(anyString())).thenReturn(username);
        when(userRepository.isUserNotAllowed(anyString())).thenReturn(false);
        when(userRepository.existsByUsernameOrEmail(anyString())).thenReturn(Optional.of(new Users()));
        when(redisService.getToken(anyString(), eq(TokenType.FORGOT))).thenReturn(null);

        UnauthorizedException unauthorizedException = assertThrows(
                UnauthorizedException.class,
                () -> authServiceImpl.checkValidForgotPasswordToken(token));
        assertEquals(message, unauthorizedException.getMessage());
        assertEquals("Invalid or expired token", unauthorizedException.getDetails());
    }

    @Test
    public void testCheckValidForgotPasswordTokenMalformedToken() {
        String token = "token";
        String username = "testUser";

        String message = "Invalid or expired reset link. Please try again";
        when(messageProperties.getMessage("MSG07")).thenReturn(message);

        when(tokenService.extractUsername(anyString())).thenReturn(username);
        when(userRepository.isUserNotAllowed(anyString())).thenReturn(false);
        when(userRepository.existsByUsernameOrEmail(anyString())).thenReturn(Optional.of(new Users()));
        when(redisService.getToken(anyString(), eq(TokenType.FORGOT))).thenReturn(token);
        when(tokenService.validateToken(anyString())).thenReturn(false);

        UnauthorizedException unauthorizedException = assertThrows(
                UnauthorizedException.class,
                () -> authServiceImpl.checkValidForgotPasswordToken(token));
        assertEquals(message, unauthorizedException.getMessage());
        assertEquals("Invalid or expired token", unauthorizedException.getDetails());
    }

    @Test
    public void testResetPasswordFinishSuccessful() {
        String token = "token";
        String username = "testUser";
        String password = "NewPassword1!";

        Users user = new Users();
        user.setUsername(username);

        when(tokenService.extractUsername(anyString())).thenReturn(username);
        when(userRepository.isUserNotAllowed(anyString())).thenReturn(false);
        when(userRepository.existsByUsernameOrEmail(anyString())).thenReturn(Optional.of(user));
        when(redisService.getToken(anyString(), eq(TokenType.FORGOT))).thenReturn(token);
        when(tokenService.validateToken(anyString())).thenReturn(true);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(tokenService.validateToken(anyString())).thenReturn(true);
        try (MockedStatic<Validation> mockedStaticValidation = mockStatic(Validation.class)) {
            mockedStaticValidation.when(() -> Validation.isValidPassword(anyString())).thenReturn(true);

        }
        authServiceImpl.resetPasswordFinish(token, password);

        verify(userRepository).save(any(Users.class));
        verify(redisService).deleteToken(eq(username), eq(TokenType.FORGOT));

    }

    @Test
    public void testResetPasswordFinishInvalidPassword() {
        String token = "token";
        String username = "testUser";
        String password = "NewPassword1!";

        Users user = new Users();
        user.setUsername(username);

        String message = "Invalid or expired reset link. Please try again";
        when(messageProperties.getMessage("MSG06")).thenReturn(message);

        when(tokenService.extractUsername(anyString())).thenReturn(username);
        when(userRepository.isUserNotAllowed(anyString())).thenReturn(false);
        when(userRepository.existsByUsernameOrEmail(anyString())).thenReturn(Optional.of(user));
        when(redisService.getToken(anyString(), eq(TokenType.FORGOT))).thenReturn(token);
        when(tokenService.validateToken(anyString())).thenReturn(true);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(tokenService.validateToken(anyString())).thenReturn(true);
        try (MockedStatic<Validation> mockedStaticValidation = mockStatic(Validation.class)) {
            mockedStaticValidation.when(() -> Validation.isValidPassword(anyString())).thenReturn(false);
            BadRequestException badRequestException = assertThrows(
                    BadRequestException.class,
                    () -> authServiceImpl.resetPasswordFinish(token, password));
            assertEquals(message, badRequestException.getMessage());
            assertEquals(message, badRequestException.getDetails());
        }

    }

    @Test
    public void testResetPasswordFinishInvalidToken() {
        String token = "token";
        String username = "testUser";
        String password = "NewPassword1!";

        Users user = new Users();
        user.setUsername(username);

        when(tokenService.extractUsername(anyString())).thenReturn(username);
        when(userRepository.isUserNotAllowed(anyString())).thenReturn(false);
        when(userRepository.existsByUsernameOrEmail(anyString())).thenReturn(Optional.of(new Users()));
        when(redisService.getToken(anyString(), eq(TokenType.FORGOT))).thenReturn(token);
        when(tokenService.validateToken(anyString())).thenReturn(false);

        assertThrows(
                UnauthorizedException.class,
                () -> authServiceImpl.resetPasswordFinish(token, password));
    }

    @Test
    public void testLogoutSuccessful() {
        HttpServletResponse response = mock(HttpServletResponse.class);
        Users user = new Users();
        user.setUsername("testUser");

        when(userService.getCurrentUser()).thenReturn(user);

        authServiceImpl.logout(response);

        verify(redisService).deleteToken(eq("testUser"), eq(TokenType.REFRESH));
        verify(tokenService).deleteCookieFromResponse(eq(response), eq(TokenType.ACCESS));
        verify(tokenService).deleteCookieFromResponse(eq(response), eq(TokenType.REFRESH));
    }

    @Test
    public void testChangePasswordSuccessfull() {
        ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto();
        changePasswordRequestDto.setOldPassword("oldPassword");
        changePasswordRequestDto.setNewPassword("NewPassword1!");
        changePasswordRequestDto.setConfirmPassword("NewPassword1!");

        Users user = new Users();
        user.setPassword(changePasswordRequestDto.getOldPassword());

        when(userService.getCurrentUser()).thenReturn(user);
        try (MockedStatic<PasswordUtils> mockedStatic = mockStatic(PasswordUtils.class);
                MockedStatic<Validation> mockedStaticValidation = mockStatic(Validation.class)) {

            mockedStatic.when(() -> PasswordUtils.verifyPassword(anyString(), anyString())).thenReturn(true);
            mockedStaticValidation.when(() -> Validation.isValidPassword(anyString())).thenReturn(true);
            authServiceImpl.changePassword(changePasswordRequestDto);

            verify(userRepository).save(any(Users.class));
        }

        verify(userRepository).save(any(Users.class));
    }

    @Test
    public void testChangePasswordWrongPassword() {
        ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto();
        changePasswordRequestDto.setOldPassword("oldPassword");
        changePasswordRequestDto.setNewPassword("NewPassword1!");
        changePasswordRequestDto.setConfirmPassword("NewPassword1!");

        Users user = new Users();
        user.setPassword(changePasswordRequestDto.getOldPassword());

        when(userService.getCurrentUser()).thenReturn(user);
        try (MockedStatic<PasswordUtils> mockedStatic = mockStatic(PasswordUtils.class);
                MockedStatic<Validation> mockedStaticValidation = mockStatic(Validation.class)) {

            mockedStatic.when(() -> PasswordUtils.verifyPassword(anyString(), anyString())).thenReturn(false);
            BadRequestException badRequestException = assertThrows(BadRequestException.class,
                    () -> authServiceImpl.changePassword(changePasswordRequestDto));
            assertEquals("Wrong password", badRequestException.getMessage());
            assertEquals("Wrong password", badRequestException.getDetails());
        }
    }

    @Test
    public void testChangePasswordNewPasswordInvalid() {
        ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto();
        changePasswordRequestDto.setOldPassword("oldPassword");
        changePasswordRequestDto.setNewPassword("NewPassword1!");
        changePasswordRequestDto.setConfirmPassword("NewPassword1!");

        Users user = new Users();
        user.setPassword(changePasswordRequestDto.getOldPassword());
        String error = "Invalid password. The password must be 8-20 characters long and include at least one lowercase letter, one uppercase letter, one digit, and one special character";
        when(userService.getCurrentUser()).thenReturn(user);
        when(messageProperties.getMessage("MSG10")).thenReturn(error);
        try (MockedStatic<PasswordUtils> mockedStatic = mockStatic(PasswordUtils.class);
                MockedStatic<Validation> mockedStaticValidation = mockStatic(Validation.class)) {

            mockedStatic.when(() -> PasswordUtils.verifyPassword(anyString(), anyString())).thenReturn(true);
            mockedStatic.when(() -> Validation.isValidPassword(anyString())).thenReturn(false);
            BadRequestException badRequestException = assertThrows(BadRequestException.class,
                    () -> authServiceImpl.changePassword(changePasswordRequestDto));
            assertEquals(error, badRequestException.getMessage());
            assertEquals(error, badRequestException.getDetails());
        }
    }
}