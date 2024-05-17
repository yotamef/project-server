package com.dev.objects;

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
    private Play play;

    @OneToMany(mappedBy = "phase", cascade = CascadeType.ALL)
    private List<PlayerPhase> playerPhases;



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
