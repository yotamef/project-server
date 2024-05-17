package com.dev.objects;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.javafaker.Faker;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String secret;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Play> plays;

    @OneToMany(mappedBy = "requester", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Friendship> requestedFriendships;

    @OneToMany(mappedBy = "accepter", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Friendship> acceptedFriendships;


    public User(String username, String password) {
        Faker faker = new Faker();
        this.username = username;
        this.password = password;
        this.secret = faker.internet().uuid();
    }

    public User() {
    }

    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public List<User> getCurrentFriends() {
        List<User> currentFriends = new ArrayList<>();
        for (Friendship friendship : requestedFriendships) {
            if (friendship.getStatus() == 1) { // Assuming 1 means 'friends'
                currentFriends.add(friendship.getAccepter());
            }
        }
        for (Friendship friendship : acceptedFriendships) {
            if (friendship.getStatus() == 1) { // Assuming 1 means 'friends'
                currentFriends.add(friendship.getRequester());
            }
        }
        return currentFriends;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public List<Play> getPlays() {
        return plays;
    }

    public void setPlays(List<Play> plays) {
        this.plays = plays;
    }

    public List<Friendship> getRequestedFriendships() {
        return requestedFriendships;
    }

    public void setRequestedFriendships(List<Friendship> requestedFriendships) {
        this.requestedFriendships = requestedFriendships;
    }

    public List<Friendship> getAcceptedFriendships() {
        return acceptedFriendships;
    }

    public void setAcceptedFriendships(List<Friendship> acceptedFriendships) {
        this.acceptedFriendships = acceptedFriendships;
    }
}
