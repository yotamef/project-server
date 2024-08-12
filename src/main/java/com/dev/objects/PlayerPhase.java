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
    private double x;

    @Column(name = "y")
    private double y;

    @Column(name = "cx")
    private double cx;

    @Column(name = "cy")
    private double cy;


    @Column(name = "action")
    private int action;

    public PlayerPhase() {
    }

    public PlayerPhase(int playerNumber, Phase phase, boolean hasBall, double x, double y, double cx, double cy, int action) {
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
