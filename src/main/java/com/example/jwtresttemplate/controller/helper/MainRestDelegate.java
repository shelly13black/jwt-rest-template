package com.example.jwtresttemplate.controller.helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
@PropertySource(value = {"classpath:rest_to_rest_uri.properties"})
public class MainRestDelegate {

    private static final Logger LOGGER = LogManager.getLogger(MainRestDelegate.class.getName());

    @Autowired
    private Environment environment;

    private String constructWSEndpointUrl(String relativeUrl){
        return environment.getRequiredProperty("WS_ROOT_CONTEXT").concat(environment.getRequiredProperty(relativeUrl));
    }

    public String test(String parameter){
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("parameter", parameter);

        return (String)WebServiceHelper.buildWsReqAndRes(constructWSEndpointUrl("WS_TEST_ENDPOINT"), queryParams, String.class, HttpMethod.POST, null);
    }
}
