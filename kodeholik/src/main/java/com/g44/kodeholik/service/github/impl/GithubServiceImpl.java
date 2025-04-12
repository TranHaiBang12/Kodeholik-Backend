package com.g44.kodeholik.service.github.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.g44.kodeholik.model.dto.request.github.GithubUser;
import com.g44.kodeholik.service.github.GithubService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class GithubServiceImpl implements GithubService {

    @Value("${spring.security.oauth2.client.registration.github.client-id}")
    private String GITHUB_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.github.client-secret}")
    private String GITHUB_CLIENT_SECRET;

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

    private String getUserEmail(String token) {

        // Tạo URL API lấy email
        String emailApiUrl = "https://api.github.com/user/emails";

        // Tạo HTTP Header với Authorization Bearer Token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
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

    @Override
    public String getGitHubAccessToken(String code) {
        String tokenUrl = "https://github.com/login/oauth/access_token";

        RestTemplate restTemplate = new RestTemplate();

        // Tạo các tham số để gửi trong yêu cầu
        Map<String, String> body = new HashMap();
        body.put("client_id", GITHUB_CLIENT_ID);
        body.put("client_secret", GITHUB_CLIENT_SECRET);
        body.put("code", code);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        // Gửi yêu cầu POST tới GitHub để nhận access token
        ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, entity, Map.class);

        // Lấy token từ response
        Map<String, String> responseBody = response.getBody();
        return responseBody != null ? responseBody.get("access_token") : null;
    }

    @Override
    public GithubUser getGitHubUserInfo(String accessToken) {
        String userInfoUrl = "https://api.github.com/user";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<GithubUser> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, entity,
                GithubUser.class);
        GithubUser user = response.getBody();
        user.setEmail(getUserEmail(accessToken));
        return response.getBody();
    }
}
