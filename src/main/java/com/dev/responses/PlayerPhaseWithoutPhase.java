package com.dev.responses;

import com.dev.objects.Phase;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerPhaseWithoutPhase {

    private int playerNumber;

    private boolean hasBall;

    private double x;

    private double y;

    private double cx;

    private double cy;

    private int action;

    public PlayerPhaseWithoutPhase() {
    }

    public PlayerPhaseWithoutPhase(int playerNumber, boolean hasBall, double x, double y, double cx, double cy, int action) {
        this.playerNumber = playerNumber;
        this.hasBall = hasBall;
        this.x = x;
        this.y = y;
        this.cx = cx;
        this.cy = cy;
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

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getCx() {
        return cx;
    }

    public void setCx(double cx) {
        this.cx = cx;
    }

    public double getCy() {
        return cy;
    }

    public void setCy(double cy) {
        this.cy = cy;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
