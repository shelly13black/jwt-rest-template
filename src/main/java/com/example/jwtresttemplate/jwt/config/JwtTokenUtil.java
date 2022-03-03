package com.example.jwtresttemplate.jwt.config;

import com.example.jwtresttemplate.jwt.helper.JwtHelper;
import com.example.jwtresttemplate.jwt.model.ApiRefreshTokenRequest;
import com.example.jwtresttemplate.jwt.model.ApiTokenRequest;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.security.Key;
import java.util.Date;

@Component
@PropertySource("class:application.properties")
public class JwtTokenUtil implements Serializable{

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class.getName());

    @Value("")
    private long JWT_TOKEN_VALIDITY;
    @Value("")
    private String secret;
    private static volatile Key key;

    private static Key getKey(String inputKey){
        Key localRefKey = JwtTokenUtil.key;
        if(null == localRefKey){
            synchronized (JwtHelper.class){
                localRefKey = JwtTokenUtil.key;
                if(null == localRefKey){
                    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
                    byte [] apiSecret = inputKey.getBytes(Charset.defaultCharset());
                    JwtTokenUtil.key = key = new SecretKeySpec(apiSecret, signatureAlgorithm.getJcaName());
                }
            }
        }
        return key;
    }

    public String encodeJWTKey(ApiTokenRequest apiTokenRequest){
        long now = System.currentTimeMillis();
        long expire = System.currentTimeMillis() + apiTokenRequest.getTokenExpiry(); //5 minutes

        JwtBuilder builder = Jwts.builder()
                .setSubject(apiTokenRequest.getTokenString())
                .setIssuedAt(new Date(now))
                .signWith(SignatureAlgorithm.HS256, getKey(secret))
                .setExpiration(new Date(expire));
        return builder.compact();
    }

    public String generateRefreshToken(ApiRefreshTokenRequest refreshTokenRequest) throws ServletException{
        ApiTokenRequest tokenRequest = new ApiTokenRequest();
        try{
            String tokenString = verifyJWT(refreshTokenRequest.getToken(), secret);
            tokenRequest.setTokenString(tokenString);
            tokenRequest.setTokenExpiry(JWT_TOKEN_VALIDITY);
        }catch(IllegalArgumentException e){
           throw new ServletException("403 - JWT NOT VALID!") ;
        }catch (ExpiredJwtException e){
            tokenRequest.setTokenString("");
            tokenRequest.setTokenExpiry(JWT_TOKEN_VALIDITY);

            return encodeJWTKey(tokenRequest);
        }
        return encodeJWTKey(tokenRequest);
    }

    public String verifyJWT(String input, String inputKey){
        try{
            Jws<Claims> result = Jwts.parser().setSigningKey(getKey(inputKey)).parseClaimsJws(input);
            String tokenString = null;
            if(null != result.getBody().get("sub")){
                tokenString = result.getBody().get("sub").toString();
            }
            return result.getBody().getSubject();
        }catch(ExpiredJwtException| UnsupportedJwtException| MalformedJwtException| IllegalArgumentException e){
            LOGGER.error("Error verifying signature. ", e);
            throw e;
        }
    }
}
