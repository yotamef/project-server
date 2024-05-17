package com.dev.controllers;

import com.dev.Persist;
import com.dev.objects.PostObject;
import com.dev.objects.User;
import com.dev.objects.UserObject;
import com.dev.responses.BasicResponse;
import com.dev.utils.Utils;
import org.hibernate.engine.jdbc.StreamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.List;


@RestController
public class TestController {


    @Autowired
    private Persist persist;

    @PostConstruct
    private void init () {

    }

    @RequestMapping(value = "sign-up", method = {RequestMethod.GET, RequestMethod.POST})
    public BasicResponse signUp (String username, String password, String repeatPassword) {
        return persist.insertUser(username, password, repeatPassword);
    }

    @RequestMapping(value = "login", method = {RequestMethod.GET, RequestMethod.POST})
    public BasicResponse login (String username, String password) {
        return persist.login(username, password);
    }

    @RequestMapping(value = "search-user", method = {RequestMethod.GET, RequestMethod.POST})
    public BasicResponse searchUser (String secretFrom, String partOfUsername) {
        return persist.searchUser(secretFrom,partOfUsername);
    }

    @RequestMapping(value = "request-friend", method = {RequestMethod.GET, RequestMethod.POST})
    public BasicResponse requestFriend(String secretFrom, String usernameTo) {
        return persist.requestFriend(secretFrom,usernameTo);
    }

    @RequestMapping(value = "accept-friend", method = {RequestMethod.GET, RequestMethod.POST})
    public BasicResponse acceptFriend(String secretFrom, String usernameToAccept) {
        return persist.acceptFriend(secretFrom,usernameToAccept);
    }

    @RequestMapping(value = "get-friends", method = {RequestMethod.GET, RequestMethod.POST})
    public BasicResponse getCurrentFriends(String secretFrom) {
        return persist.getCurrentFriends(secretFrom);
    }

    @RequestMapping(value = "test", method = {RequestMethod.GET, RequestMethod.POST})
    public User test() {
        return persist.getUserByUsername("yotam");
    }











}
