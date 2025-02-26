package com.g44.kodeholik.service.auth.impl;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

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
import com.g44.kodeholik.model.enums.user.UserRole;
import com.g44.kodeholik.model.enums.user.UserStatus;
import com.g44.kodeholik.repository.user.UserRepository;
import com.g44.kodeholik.service.auth.AuthService;
import com.g44.kodeholik.service.email.EmailService;
import com.g44.kodeholik.service.redis.RedisService;
import com.g44.kodeholik.service.token.TokenService;
import com.g44.kodeholik.service.user.UserService;
import com.g44.kodeholik.util.email.EmailUtils;
import com.g44.kodeholik.util.password.PasswordUtils;
import com.g44.kodeholik.util.validation.Validation;

import co.elastic.clients.elasticsearch.security.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    private final EmailService emailService;

    private final UserRepository userRepository;

    private final UserDetailsService userDetailsService;

    private final RedisService redisService;

    private final UserService userService;

    private final MessageProperties messageProperties;

    @Value("${spring.jwt.forgot-token.expiry-time}")
    private int forgotTokenExpiryTime;

    @Value("${spring.jwt.forgot-token.fe-link}")
    private String feLink;

    @Override
    public boolean verify(LoginRequestDto loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword()));
        return authentication.isAuthenticated();
    }

    @Override
    public void loginNormal(LoginRequestDto loginRequest, HttpServletResponse response) {
        verify(loginRequest);
        String username = loginRequest.getUsername();

        Users user = userRepository.existsByUsernameOrEmail(username)
                .orElseThrow(() -> new NotFoundException("User not found", "User not found"));
        if (userRepository.isUserNotAllowed(username)) {
            throw new ForbiddenException("This account is not allowed to do this action",
                    "This account is not allowed to do this action");
        }

        String accessToken = tokenService.generateAccessToken(user.getUsername());
        tokenService.addTokenToCookie(accessToken, response, TokenType.ACCESS);
        String refreshToken = tokenService.generateRefreshToken(user.getUsername(), new Date());
        tokenService.addTokenToCookie(refreshToken, response, TokenType.REFRESH);
    }

    @Override
    public Users checkUsernameExists(String username) {
        return userRepository.existsByUsernameOrEmail(username)
                .orElseThrow(() -> new NotFoundException("User not found", "User not found"));
    }

    @Override
    public void resetPasswordInit(String username) {
        Optional<Users> userOptional = userRepository.findByEmail(username);
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            if (userRepository.isUserNotAllowed(username)) {
                throw new ForbiddenException("This account is not allowed to do this action",
                        "This account is not allowed to do this action");
            }
            String token = tokenService.generateForgotPasswordToken(username);
            redisService.saveToken(user.getUsername(), token, forgotTokenExpiryTime, TokenType.FORGOT);
            // log.info(token);
            emailService.sendEmailResetPassword(user.getEmail(), "[KODEHOLIK] Reset Password", user.getUsername(),
                    feLink + token);
        } else {
            throw new BadRequestException("Email not found", "Email not found");
        }
    }

    private void setPrincipal(String username, HttpServletRequest request) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (userRepository.isUserNotAllowed(username)) {
            throw new ForbiddenException("This account is not allowed to do this action",
                    "This account is not allowed to do this action");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    @Override
    public boolean checkValidForgotPasswordToken(String token) {
        String username = tokenService.extractUsername(token);
        if (userRepository.isUserNotAllowed(username)) {
            throw new ForbiddenException("This account is not allowed to do this action",
                    "This account is not allowed to do this action");
        }
        if (userRepository.existsByUsernameOrEmail(username).isPresent()) {
            String savedToken = redisService.getToken(username, TokenType.FORGOT);
            if (savedToken != null) {
                if (tokenService.validateToken(token) &&
                        token.equals(savedToken.trim())) {
                    return true;
                }
            }
        }
        throw new UnauthorizedException(messageProperties.getMessage("MSG07"), "Invalid or expired token");
    }

    @Override
    public void resetPasswordFinish(String token, String password) {
        if (checkValidForgotPasswordToken(token)) {
            String username = tokenService.extractUsername(token);
            Users user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new NotFoundException("Email not found", "Email not found"));
            password = password.trim().replaceAll("\"", "");
            if (Validation.isValidPassword(password)) {
                user.setPassword(PasswordUtils.encodePassword(password));
            } else {
                throw new BadRequestException(
                        messageProperties.getMessage("MSG06"),
                        messageProperties.getMessage("MSG06"));
            }
            userRepository.save(user);
            redisService.deleteToken(username, TokenType.FORGOT);
        } else {
            throw new UnauthorizedException("Invalid or expired token", "Invalid or expired token");
        }
    }

    @Override
    public void logout(HttpServletResponse response) {
        Users user = userService.getCurrentUser();
        if (user != null) {
            redisService.deleteToken(user.getUsername(), TokenType.REFRESH);
            tokenService.deleteCookieFromResponse(response, TokenType.ACCESS);
            tokenService.deleteCookieFromResponse(response, TokenType.REFRESH);
        } else {
            throw new UnauthorizedException("User not logged in", "User not logged in");
        }
    }

    @Override
    public void loginWithGoogle(OAuth2AuthenticationToken oAuth2User, HttpServletResponse response,
            HttpServletRequest request) {
        if (oAuth2User == null) {
            throw new UnauthorizedException("Wrong credentials", "Wrong credentials");
        }

        String email = oAuth2User.getPrincipal().getAttribute("email");
        // if (!EmailUtils.isFptEduEmail(email)) {
        // throw new ForbiddenException("This account is not allowed to do this action",
        // "This account is not allowed to do this action");
        // }
        String username = "";
        Optional<Users> optionalUser = userRepository.existsByUsernameOrEmail(email);
        if (!optionalUser.isPresent()) {
            // get data
            String name = oAuth2User.getPrincipal().getAttribute("name");
            String picture = oAuth2User.getPrincipal().getAttribute("picture");
            AddUserRequestDto addUserRequestDto = new AddUserRequestDto();
            addUserRequestDto.setUsername(name);
            addUserRequestDto.setFullname(name);
            addUserRequestDto.setEmail(email);
            addUserRequestDto.setAvatar(picture);
            addUserRequestDto.setRole(UserRole.STUDENT);

            userService.addUserAfterLoginGoogle(addUserRequestDto);
            username = name;

        } else {
            if (userRepository.isUserNotAllowed(email)) {
                throw new ForbiddenException("This account is not allowed to do this action",
                        "This account is not allowed to do this action");
            }
            username = optionalUser.get().getUsername();
        }
        // set security principal
        setPrincipal(username, request);

        // generate token
        String accessToken = tokenService.generateAccessToken(username);
        tokenService.addTokenToCookie(accessToken, response, TokenType.ACCESS);
        String refreshToken = tokenService.generateRefreshToken(username, new Date());
        tokenService.addTokenToCookie(refreshToken, response, TokenType.REFRESH);

    }

    public void changePassword(ChangePasswordRequestDto changePasswordRequestDto) {
        if (!changePasswordRequestDto.getNewPassword().equals(changePasswordRequestDto.getConfirmPassword())) {
            throw new BadRequestException("Confirm password must be the same to new password",
                    "Confirm password must be the same to new password");
        }
        Users user = userService.getCurrentUser();
        String newPassword = changePasswordRequestDto.getNewPassword();

        if (PasswordUtils.verifyPassword(changePasswordRequestDto.getOldPassword(), user.getPassword())) {
            if (Validation.isValidPassword(newPassword)) {
                user.setPassword(PasswordUtils.encodePassword(newPassword));
                userRepository.save(user);
            } else {
                throw new BadRequestException(messageProperties.getMessage("MSG10"),
                        messageProperties.getMessage("MSG10"));

            }
        } else {
            throw new BadRequestException("Wrong password", "Wrong password");
        }
    }

}
