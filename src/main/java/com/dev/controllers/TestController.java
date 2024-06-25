package com.dev.controllers;

import com.dev.Persist;
import com.dev.objects.Phase;
import com.dev.objects.Play;
import com.dev.objects.PlayerPhase;
import com.dev.objects.User;
import com.dev.responses.BasicResponse;
import com.dev.responses.PlayerPhaseWithoutPhase;
import com.dev.responses.PlayerPhases;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@RestController
public class TestController {

    @Autowired
    private Persist persist;

    @PostConstruct
    private void init() {

    }

    @RequestMapping(value = "sign-up", method = {RequestMethod.GET, RequestMethod.POST})
    public BasicResponse signUp(String username, String password, String repeatPassword) {
        return persist.insertUser(username, password, repeatPassword);
    }

    @RequestMapping(value = "login", method = {RequestMethod.GET, RequestMethod.POST})
    public BasicResponse login(String username, String password) {
        return persist.login(username, password);
    }

    @RequestMapping(value = "search-user", method = {RequestMethod.GET, RequestMethod.POST})
    public BasicResponse searchUser(String secretFrom, String partOfUsername) {
        return persist.searchUser(secretFrom, partOfUsername);
    }

    @RequestMapping(value = "request-friend", method = {RequestMethod.GET, RequestMethod.POST})
    public BasicResponse requestFriend(String secretFrom, String usernameTo) {
        return persist.requestFriend(secretFrom, usernameTo);
    }

    @RequestMapping(value = "get-friend-requests", method = {RequestMethod.GET, RequestMethod.POST})
    public BasicResponse getRequestsToAccept(String secretFrom) {
        return persist.getRequestsToAccept(secretFrom);
    }

    @RequestMapping(value = "accept-friend", method = {RequestMethod.GET, RequestMethod.POST})
    public BasicResponse acceptFriend(String secretFrom, String usernameToAccept) {
        return persist.acceptFriend(secretFrom, usernameToAccept);
    }

    @RequestMapping(value = "get-friends", method = {RequestMethod.GET, RequestMethod.POST})
    public BasicResponse getCurrentFriends(String secretFrom) {
        return persist.getCurrentFriends(secretFrom);
    }

    @RequestMapping(value = "add-play", method = {RequestMethod.GET, RequestMethod.POST})
    public BasicResponse addPlay(String secret, String playName) {
        return persist.addPlay(secret,playName);
    }

    @RequestMapping(value = "add-phase", method = {RequestMethod.GET, RequestMethod.POST})
    public BasicResponse test(HttpServletRequest request) {
        try {
            String bodyData = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            ObjectMapper objectMapper = new ObjectMapper();
            PlayerPhases myObject = objectMapper.readValue(bodyData, PlayerPhases.class);
            System.out.println(bodyData);

            String secret = myObject.getSecret();
            String playName = myObject.getPlayName();
            int orderNum = myObject.getOrderNum();
            List<PlayerPhaseWithoutPhase> playerPhases = myObject.getPlayerPhases();

            System.out.println("Secret: " + secret);
            System.out.println("PlayName: " + playName);
            System.out.println("OrderNum: " + orderNum);
            System.out.println("PlayerPhases: " + playerPhases);


            return persist.addPhaseToPlay(secret, playName, orderNum, playerPhases);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @RequestMapping(value = "get-play", method = {RequestMethod.GET, RequestMethod.POST})
    public Play getPlay(String secret, String playName) {
        //TODO
        //validates that user is approved to get the play
        return persist.getPlay(playName);
    }




}
