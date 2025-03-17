package com.g44.kodeholik.handler;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.exception.ForbiddenException;
import com.g44.kodeholik.exception.UnauthorizedException;
import com.g44.kodeholik.model.dto.request.user.AddUserRequestDto;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.token.TokenType;
import com.g44.kodeholik.model.enums.user.UserRole;
import com.g44.kodeholik.repository.user.UserRepository;
import com.g44.kodeholik.service.github.GithubService;
import com.g44.kodeholik.service.token.TokenService;
import com.g44.kodeholik.service.user.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
public class OnOauth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenService tokenService;

    private final UserService userService;

    private final GithubService githubService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        if (oauthUser == null) {
            throw new UnauthorizedException("Wrong credentials", "Wrong credentials");
        }
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        String registrationId = oauthToken.getAuthorizedClientRegistrationId();
        String name = "";
        String picture = "";
        String email = "";
        String apiCallbackUrl = "";
        if ("google".equals(registrationId)) {
            email = oauthUser.getAttribute("email");
            name = oauthUser.getAttribute("name");
            picture = oauthUser.getAttribute("picture");
        } else {
            email = githubService.getUserEmail(oauthToken);
            name = oauthUser.getAttribute("login");
            picture = oauthUser.getAttribute("avatar_url");
        }

        // OAuth2AuthenticationToken authenticationToken = new
        // OAuth2AuthenticationToken(
        // oauthUser, oauthUser.getAuthorities(), registrationId);
        oauthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(oauthToken);

        // if (!EmailUtils.isFptEduEmail(email)) {
        // throw new ForbiddenException("This account is not allowed to do this action",
        // "This account is not allowed to do this action");
        // }

        String username = "";
        Optional<Users> optionalUser = userService.isUserExistedbyUsernameOrEmail(email);
        if (!optionalUser.isPresent()) {
            AddUserRequestDto addUserRequestDto = new AddUserRequestDto();
            addUserRequestDto.setUsername(name);
            addUserRequestDto.setFullname(name);
            addUserRequestDto.setEmail(email);
            addUserRequestDto.setAvatar(picture);
            addUserRequestDto.setRole(UserRole.STUDENT);
            apiCallbackUrl = "/api/v1/auth/login/oauth2/google?port=5174";

            userService.addUserAfterLoginGoogle(addUserRequestDto);
            username = name;

        } else {
            if (userService.isUserNotAllowed(email)) {
                throw new ForbiddenException("This account is not allowed to do this action",
                        "This account is not allowed to do this action");
            }
            username = optionalUser.get().getUsername();
            apiCallbackUrl = "/api/v1/auth/login/oauth2/google?port="
                    + (optionalUser.get().getRole() == UserRole.STUDENT ? 5174 : 5173);

        }
        if (userService.isUserNotAllowed(email)) {
            throw new ForbiddenException("This account is not allowed to do this action",
                    "This account is not allowed to do this action");
        }

        // UsernamePasswordAuthenticationToken authenticationToken = new
        // UsernamePasswordAuthenticationToken(
        // userDetails, null, userDetails.getAuthorities());
        // authenticationToken.setDetails(new
        // WebAuthenticationDetailsSource().buildDetails(request));

        // generate token
        String accessToken = tokenService.generateAccessToken(username);
        tokenService.addTokenToCookie(accessToken, response, TokenType.ACCESS);
        String refreshToken = tokenService.generateRefreshToken(username, new Date());
        tokenService.addTokenToCookie(refreshToken, response, TokenType.REFRESH);

        response.sendRedirect(apiCallbackUrl);

    }

}
