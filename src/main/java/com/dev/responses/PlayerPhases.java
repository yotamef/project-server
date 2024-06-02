package com.dev.responses;

import com.dev.objects.PlayerPhase;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerPhases {


    private String secret;
    private String playName;
    private int orderNum;
    private List<PlayerPhaseWithoutPhase> playerPhases;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getPlayName() {
        return playName;
    }

    public void setPlayName(String playName) {
        this.playName = playName;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public List<PlayerPhaseWithoutPhase> getPlayerPhases() {
        return playerPhases;
    }

    public void setPlayerPhases(List<PlayerPhaseWithoutPhase> playerPhases) {
        this.playerPhases = playerPhases;
    }


}
