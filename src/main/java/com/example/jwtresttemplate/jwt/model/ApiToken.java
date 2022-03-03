package com.example.jwtresttemplate.jwt.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class ApiToken implements Serializable {

    private final String jwtToken;
}
