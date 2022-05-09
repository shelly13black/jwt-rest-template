package com.example.jwtresttemplate.controller.helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;

public class WebServiceHelper {

    public WebServiceHelper() {
    }

    private static final Logger LOGGER = LogManager.getLogger(WebServiceHelper.class.getName());

    public static Object buildWsReqAndRes(String endPointUrl, MultiValueMap<String, String> queryParams, Class<?> returnObject, HttpMethod httpMethodName, Object postObject){

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        URI uri;
        HttpEntity<Object> entity;
        HttpHeaders headers = getHeaders();
        ResponseEntity<?> response = null;
        try{
            if(httpMethodName.equals(HttpMethod.POST) || httpMethodName.equals(HttpMethod.PUT)){
                uri = UriComponentsBuilder.fromHttpUrl(endPointUrl).queryParams(queryParams).build().encode().toUri();
                entity = new HttpEntity<>(postObject, headers);
            }else{
                uri = UriComponentsBuilder.fromHttpUrl(endPointUrl).queryParams(queryParams).build().encode().toUri();
                entity = new HttpEntity<>(headers);
            }
            response = restTemplate.exchange(uri, httpMethodName, entity, returnObject);
            return response.getBody();
        }catch(Exception e){
            LOGGER.error(e.getMessage());
        }
        return response;
    }

    private static HttpHeaders getHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return headers;
    }
}
