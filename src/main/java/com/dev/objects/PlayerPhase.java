package com.dev.objects;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "players_in_phases")
public class PlayerPhase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "player_num")
    private int playerNumber;

    @ManyToOne
    @JoinColumn(name = "phase_id")
    @JsonBackReference
    private Phase phase;

    @Column(name = "ball")
    private boolean hasBall;

    @Column(name = "x")
    private int x;

    @Column(name = "y")
    private int y;

    @Column(name = "cx")
    private int cx;

    @Column(name = "cy")
    private int cy;


    @Column(name = "action")
    private int action;

    public PlayerPhase() {
    }

    public PlayerPhase(int playerNumber, Phase phase, boolean hasBall, int x, int y, int cx, int cy, int action) {
        this.playerNumber = playerNumber;
        this.phase = phase;
        this.hasBall = hasBall;
        this.x = x;
        this.y = y;
        this.cx = cx;
        this.cy = cy;
        this.action = action;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
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

    public int getCx() {
        return cx;
    }

    public void setCx(int cx) {
        this.cx = cx;
    }

    public int getCy() {
        return cy;
    }

    public void setCy(int cy) {
        this.cy = cy;
    }
}
