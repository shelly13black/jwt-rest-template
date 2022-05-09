package com.example.jwtresttemplate.service.impl;

import com.example.jwtresttemplate.controller.helper.MainRestDelegate;
import com.example.jwtresttemplate.service.MainService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("mainService")
public class MainServiceImpl implements MainService {

    private static final Logger LOGGER = LogManager.getLogger(MainServiceImpl.class.getName());

    @Autowired
    private MainRestDelegate mainRestDelegate;

    @Override
    public String test(){
        return mainRestDelegate.test("From jwt project");
    }
}
