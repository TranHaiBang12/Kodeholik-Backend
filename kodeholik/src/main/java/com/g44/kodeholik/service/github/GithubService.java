package com.g44.kodeholik.service.github;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

public interface GithubService {
    public String getUserEmail(OAuth2AuthenticationToken token);
}
