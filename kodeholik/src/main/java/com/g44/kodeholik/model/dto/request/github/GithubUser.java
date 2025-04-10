package com.g44.kodeholik.model.dto.request.github;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GithubUser {
    @JsonProperty("login")
    private String name;
    private String email;

    @JsonProperty("avatar_url")
    private String avatarUrl;
}
