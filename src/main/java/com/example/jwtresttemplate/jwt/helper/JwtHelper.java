package com.example.jwtresttemplate.jwt.helper;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.Key;
import java.util.Date;

public class JwtHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtHelper.class.getName());
    private static volatile Key key;

    private static Key getKey(String inputKey){
        Key localRefKey = JwtHelper.key;
        if(null == localRefKey){
            synchronized (JwtHelper.class){
                localRefKey = JwtHelper.key;
                if(null == localRefKey){
                    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
                    byte [] apiSecret = inputKey.getBytes(Charset.defaultCharset());
                    JwtHelper.key = key = new SecretKeySpec(apiSecret, signatureAlgorithm.getJcaName());
                }
            }
        }
        return key;
    }

    public static String encodeJWTKey(String subject, String inputKey){
        long now = System.currentTimeMillis();
        long expire = System.currentTimeMillis() + 300000; //5 minutes

        JwtBuilder builder = Jwts.builder().setSubject(subject).setIssuedAt(new Date(now)).signWith(SignatureAlgorithm.HS256, getKey(inputKey))
                .setExpiration(new Date(expire));
        return builder.compact();
    }

    public static String verifyJWT(String input, String inputKey){
        try{
            Jws<Claims> result = Jwts.parser().setSigningKey(getKey(inputKey)).parseClaimsJws(input);
            return result.getBody().getSubject();
            //Okay, we can trust this JWT
        }catch(ExpiredJwtException| UnsupportedJwtException| MalformedJwtException| IllegalArgumentException e){
            LOGGER.error("Error verifying signature. ", e);
            throw e;
        }
    }

    public static void main(String [] args){
        String key = "bxydebjsjAJjjsfhAJSKkdjdb8jvdh2";
        String token = encodeJWTKey("tokenString", key);
        System.out.println("Token: " + token);
        System.out.println("Subject: " + verifyJWT(token, key));
    }
}
