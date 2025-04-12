package com.g44.kodeholik.service.auth.impl;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

@Service
public class GoogleTokenVerifierService {
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String GOOGLE_CLIENT_ID;

    public GoogleIdToken.Payload verifyToken(String token) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(), JacksonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID)) // Chỉ chấp nhận token của ứng dụng này
                    .build();

            GoogleIdToken idToken = verifier.verify(token);
            if (idToken != null) {
                return idToken.getPayload(); // Trả về thông tin user nếu token hợp lệ
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
