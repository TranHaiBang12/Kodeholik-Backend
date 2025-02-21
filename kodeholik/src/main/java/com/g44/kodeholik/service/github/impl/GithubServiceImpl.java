package com.g44.kodeholik.service.github.impl;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.g44.kodeholik.service.github.GithubService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class GithubServiceImpl implements GithubService {

    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    public String getUserEmail(OAuth2AuthenticationToken token) {
        OAuth2AuthorizedClient client = oAuth2AuthorizedClientService.loadAuthorizedClient(
                token.getAuthorizedClientRegistrationId(),
                token.getName());

        if (client == null) {
            return "Không tìm thấy thông tin OAuth2AuthorizedClient!";
        }

        OAuth2AccessToken accessToken = client.getAccessToken();
        // Lấy access token từ OAuth2
        String tokenValue = accessToken.getTokenValue();

        // Tạo URL API lấy email
        String emailApiUrl = "https://api.github.com/user/emails";

        // Tạo HTTP Header với Authorization Bearer Token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + tokenValue);
        headers.set("Accept", "application/vnd.github.v3+json");

        // Tạo request entity
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Gửi request GET
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List> response = restTemplate.exchange(emailApiUrl, HttpMethod.GET, entity, List.class);

        // Lấy danh sách email
        List<Map<String, Object>> emails = response.getBody();

        if (emails != null) {
            for (Map<String, Object> email : emails) {
                if ((boolean) email.get("primary")) {
                    return (String) email.get("email"); // Lấy email chính
                }
            }
        }
        return null;
    }

}
