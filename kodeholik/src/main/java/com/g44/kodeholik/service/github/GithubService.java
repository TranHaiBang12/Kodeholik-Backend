package com.g44.kodeholik.service.github;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import com.g44.kodeholik.model.dto.request.github.GithubUser;

public interface GithubService {
    public String getUserEmail(OAuth2AuthenticationToken token);

    public String getGitHubAccessToken(String code);

    public GithubUser getGitHubUserInfo(String accessToken);
}
