package com.example.jwtresttemplate.jwt.config;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private static final List<String> EXCLUDE_URL = Arrays.asList("/jwt-rest-template/auth/refresh-token"
            ,"/jwt-rest-template/auth/get-token");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");
        String jwtToken;
        /**
         * JWT Token is in the form "Bearer token". Remove the word Bearer and get just the token.
         */

        if(null == requestTokenHeader || !requestTokenHeader.startsWith("Bearer")){
            throw new ServletException("401 - UNAUTHORIZED!");
        }else{
            jwtToken = requestTokenHeader.substring(7);
            try{
                jwtTokenUtil.verifyJWT(jwtToken, secret);
            }catch(IllegalArgumentException e){
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied!");
                throw new ServletException("403 - JWT NOT VALID!");
            }catch (ExpiredJwtException e){
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Token Expired!");
                throw new ServletException("403 - JWT EXPIRED!");
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request){
        return EXCLUDE_URL.stream().anyMatch(exclude -> exclude.equalsIgnoreCase(request.getServletPath()));
    }
}
