package com.g44.kodeholik.config;

import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class GoogleCloudConfig {

    @Bean
    public Storage storage() throws IOException {
        InputStream serviceAccountStream = new ClassPathResource("flowing-access-452711-a1-15e7d698efdf.json")
                .getInputStream();

        return StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                .build()
                .getService();
    }
}
