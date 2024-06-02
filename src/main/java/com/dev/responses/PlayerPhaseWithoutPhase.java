package com.dev.responses;

import com.dev.objects.Phase;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerPhaseWithoutPhase {

    private int playerNumber;

    private boolean hasBall;

    private int x;

    private int y;

    private int action;

    public PlayerPhaseWithoutPhase() {
    }

    public PlayerPhaseWithoutPhase(int playerNumber, boolean hasBall, int x, int y, int action) {
        this.playerNumber = playerNumber;
        this.hasBall = hasBall;
        this.x = x;
        this.y = y;
        this.action = action;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public boolean isHasBall() {
        return hasBall;
    }

    public void setHasBall(boolean hasBall) {
        this.hasBall = hasBall;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
