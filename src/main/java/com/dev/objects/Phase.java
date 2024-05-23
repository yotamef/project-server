package com.dev.objects;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "phases")
public class Phase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "order_num")
    private int orderNum;

    @ManyToOne
    @JoinColumn(name = "play_id")
    @JsonBackReference
    private Play play;

    @OneToMany(mappedBy = "phase", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<PlayerPhase> playerPhases;
    public Phase() {
    }

    public Phase(int order, Play play) {
        this.orderNum = order;
        this.play = play;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public Play getPlay() {
        return play;
    }

    public void setPlay(Play play) {
        this.play = play;
    }

    public List<PlayerPhase> getPlayerPhases() {
        return playerPhases;
    }

    public void setPlayerPhases(List<PlayerPhase> playerPhases) {
        this.playerPhases = playerPhases;
    }
}
