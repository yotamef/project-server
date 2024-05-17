package com.dev.objects;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "friendships")
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    @JsonBackReference
    private User requester;

    @ManyToOne
    @JoinColumn(name = "accepter_id")
    @JsonBackReference
    private User accepter;

    @Column
    private int status; // 0 - not friends, 1 - friends, 2 - pending

    public Friendship() {
    }

    public Friendship(User requester, User accepter, int status) {
        this.requester = requester;
        this.accepter = accepter;
        this.status = status;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getRequester() {
        return requester;
    }

    public void setRequester(User requester) {
        this.requester = requester;
    }

    public User getAccepter() {
        return accepter;
    }

    public void setAccepter(User accepter) {
        this.accepter = accepter;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
