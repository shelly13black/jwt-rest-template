package com.example.jwtresttemplate.jwt.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiTokenRequest implements Serializable {

    private String tokenString;
    private long tokenExpiry;
}
