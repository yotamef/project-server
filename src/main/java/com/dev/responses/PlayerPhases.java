package com.dev.responses;

import com.dev.objects.PlayerPhase;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerPhases {

    private List<PlayerPhase> playerPhases;

    public List<PlayerPhase> getPlayerPhases() {
        return playerPhases;
    }

    public void setPlayerPhases(List<PlayerPhase> playerPhases) {
        this.playerPhases = playerPhases;
    }
}
