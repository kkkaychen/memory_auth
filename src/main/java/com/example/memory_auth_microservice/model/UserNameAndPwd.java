package com.example.memory_auth_microservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserNameAndPwd(
        @JsonProperty("userName") String userName,
        @JsonProperty("password") String password
) {
}