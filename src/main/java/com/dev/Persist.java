package com.dev;

import com.dev.objects.*;
import com.dev.responses.BasicResponse;
import com.dev.responses.ListUserResponse;
import com.dev.responses.LoginResponse;
import com.dev.utils.Errors;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.dev.utils.Errors.*;

@Component
public class Persist {

    private final SessionFactory sessionFactory;

    @Autowired
    public Persist(SessionFactory sf) {
        this.sessionFactory = sf;
    }

    public Session getQuerySession() {
        return this.sessionFactory.getCurrentSession();
    }

    private Session openSession() {
        return sessionFactory.openSession();
    }

    private void closeSession(Session session) {
        if (session != null && session.isOpen()) {
            session.close();
        }
    }

    private void beginTransaction(Session session) {
        session.beginTransaction();
    }

    private void commitTransaction(Session session) {
        if (session.getTransaction().isActive()) {
            session.getTransaction().commit();
        }
    }

    private void rollbackTransaction(Session session) {
        if (session.getTransaction().isActive()) {
            session.getTransaction().rollback();
        }
    }

    public void save(Object object) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.saveOrUpdate(object);
        session.getTransaction().commit();
        session.close();
    }

    public <T> T loadObject(Class<T> clazz, int oid) {
        Session session = sessionFactory.openSession();
        T obj = session.get(clazz, oid);
        session.close();
        return obj;
    }

    public User getUserByUsername(String username) {
        Session session = sessionFactory.openSession();
        User user = session.createQuery("FROM User WHERE username = :username", User.class)
                .setParameter("username", username)
                .uniqueResult();
        session.close();
        return user;
    }

    public User getUserBySecret(String secret) {
        Session session = sessionFactory.openSession();
        User user = session.createQuery("FROM User WHERE secret = :secret", User.class)
                .setParameter("secret", secret)
                .uniqueResult();
        session.close();
        return user;
    }


    public boolean friendshipExists(User requester, User accepter) {
        Session session = sessionFactory.openSession();
        boolean exists = !session.createQuery("FROM Friendship WHERE requester = :requester AND accepter = :accepter", Friendship.class)
                .setParameter("requester", requester)
                .setParameter("accepter", accepter)
                .getResultList()
                .isEmpty();
        session.close();
        return exists;
    }

    @Transactional
    public BasicResponse insertUser(String username, String password, String repeatPassword) {
        if (username == null) {
            return new BasicResponse(false, ERROR_NO_USERNAME);
        }
        if (password == null) {
            return new BasicResponse(false, ERROR_NO_PASSWORD);
        }
        if (repeatPassword == null) {
            return new BasicResponse(false, ERROR_NO_REPEAT_PASSWORD);
        }
        if (!password.equals(repeatPassword)) {
            return new BasicResponse(false, Errors.ERROR_PASSWORD_NOT_MATCH);
        }

        if (getUserByUsername(username) != null) {
            return new BasicResponse(false, Errors.ERROR_USERNAME_TAKEN);
        }

        User newUser = new User(username, password);
        save(newUser);
        return new BasicResponse(true, Errors.NO_ERRORS);
    }

    @Transactional
    public BasicResponse login(String username, String password) {
        if (username == null) {
            return new BasicResponse(false, Errors.ERROR_NO_USERNAME);
        }
        if (password == null) {
            return new BasicResponse(false, Errors.ERROR_NO_PASSWORD);
        }

        Session session = sessionFactory.openSession();
        User user = session.createQuery("FROM User WHERE username = :username AND password = :password", User.class)
                .setParameter("username", username)
                .setParameter("password", password)
                .uniqueResult();
        session.close();

        if (user == null) {
            return new BasicResponse(false, Errors.ERROR_INCORRECT_PASSWORD);
        }

        return new LoginResponse(true, Errors.NO_ERRORS, user.getId(), user.getSecret(), user);
    }

    @Transactional
    public ListUserResponse searchUser(String secretFrom, String partOfUsername) {
        User requestingUser = getUserBySecret(secretFrom);
        if (partOfUsername == null) {
            return new ListUserResponse(false, ERROR_NO_PART_OF_USERNAME, List.of());
        }
        if (requestingUser == null) {
            return new ListUserResponse(false, ERROR_NO_SUCH_SECRET, List.of());
        }

        Session session = sessionFactory.openSession();
        List<User> users = session.createQuery("FROM User WHERE username LIKE :username AND id != :requestingUserId", User.class)
                .setParameter("username", "%" + partOfUsername + "%")
                .setParameter("requestingUserId", requestingUser.getId())
                .getResultList();
        session.close();

        return new ListUserResponse(true, Errors.NO_ERRORS, users);
    }

    @Transactional
    public BasicResponse requestFriend(String secretFrom, String usernameTo) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        User requester = getUserBySecret(secretFrom);
        if (requester == null) {
            session.getTransaction().rollback();
            session.close();
            return new BasicResponse(false, ERROR_NO_SUCH_SECRET);
        }

        User accepter = getUserByUsername(usernameTo);
        if (accepter == null) {
            session.getTransaction().rollback();
            session.close();
            return new BasicResponse(false, Errors.ERROR_NO_SUCH_USERNAME);
        }

        if (friendshipExists(requester, accepter)) {
            session.getTransaction().rollback();
            session.close();
            return new BasicResponse(false, ERROR_ALREADY_SENT_REQUEST);
        }

        if (friendshipExists(accepter, requester)) {
            session.getTransaction().rollback();
            session.close();
            return new BasicResponse(false, ERROR_REQUEST_EXIST);
        }

        Friendship friendship = new Friendship(requester, accepter, 2); // 2 - pending
        session.save(friendship);
        session.getTransaction().commit();
        session.close();
        return new BasicResponse(true, Errors.NO_ERRORS);
    }

    @Transactional
    public BasicResponse acceptFriend(String secretFrom, String usernameToAccept) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        User accepter = getUserBySecret(secretFrom);
        if (accepter == null) {
            session.getTransaction().rollback();
            session.close();
            return new BasicResponse(false, ERROR_NO_SUCH_SECRET);
        }

        User requester = getUserByUsername(usernameToAccept);
        if (requester == null) {
            session.getTransaction().rollback();
            session.close();
            return new BasicResponse(false, Errors.ERROR_NO_SUCH_USERNAME);
        }

        Friendship friendship = session.createQuery("FROM Friendship WHERE requester = :requester AND accepter = :accepter AND status = 2", Friendship.class)
                .setParameter("requester", requester)
                .setParameter("accepter", accepter)
                .uniqueResult();

        if (friendship == null) {
            session.getTransaction().rollback();
            session.close();
            return new BasicResponse(false, Errors.ERROR_FRIENDSHIP_DOESNT_EXIST);
        }

        friendship.setStatus(1); // 1 - accepted
        session.saveOrUpdate(friendship);
        session.getTransaction().commit();
        session.close();

        return new BasicResponse(true, Errors.NO_ERRORS);
    }

    @Transactional
    public ListUserResponse getRequestsToAccept(String secret) {
        Session session = sessionFactory.openSession();
        User accepter = getUserBySecret(secret);
        List<Friendship> friendships = session.createQuery("FROM Friendship WHERE accepter = :accepter AND status = 2", Friendship.class)
                .setParameter("accepter", accepter)
                .list();
        if (friendships == null) {
            return new ListUserResponse(false, ERROR_NO_FRIEND_REQUESTS, List.of());
        } else {
            return new ListUserResponse(true, NO_ERRORS, getUsers(friendships));
        }
    }


    @Transactional
    public List<User> getUsers(List<Friendship> friendships) {
        List<User> users = new ArrayList<>();
        for (Friendship friendship : friendships) {
            users.add(friendship.getRequester());
        }
        return users;
    }

    @Transactional
    public ListUserResponse getCurrentFriends(String secret) {
        User user = getUserBySecret(secret);
        if (user == null) {
            return new ListUserResponse(false, ERROR_NO_SUCH_SECRET, List.of());
        } else {
            return new ListUserResponse(true, NO_ERRORS, user.getCurrentFriends());
        }
    }


    @Transactional
    public BasicResponse addPlay(String secret, String playName) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        // Find the user by secret
        User user = getUserBySecret(secret);
        if (user == null) {
            session.getTransaction().rollback();
            session.close();
            return new BasicResponse(false, Errors.ERROR_NO_SUCH_SECRET);
        }

        // Check if a play with the same name already exists for the user
        Play play = getPlayByUserAndName(user,playName);

        if (play!=null) {
            session.getTransaction().rollback();
            session.close();
            return new BasicResponse(false, Errors.THERE_IS_ALREADY_PLAY_WITH_THIS_NAME);
        }

        // Create new play and add it to the user
        Play newPlay = new Play(user, playName);
        user.getPlays().add(newPlay);

        // Save the new play
        session.save(newPlay);
        session.getTransaction().commit();
        session.close();

        return new BasicResponse(true, Errors.NO_ERRORS);
    }

    @Transactional
    public BasicResponse addPhaseToPlay(String secret, String playName, int orderNum, List<PlayerPhase> playerPhases) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        // Find the user by secret
        User user = getUserBySecret(secret);
        if (user == null) {
            session.getTransaction().rollback();
            session.close();
            return new BasicResponse(false, Errors.ERROR_NO_SUCH_SECRET);
        }

        // Find the play by name and user
        Play play = getPlayByUserAndName(user,playName);

        if (play == null) {
            session.getTransaction().rollback();
            session.close();
            return new BasicResponse(false, Errors.ERROR_NO_SUCH_PLAY);
        }

        // Create new phase and set the play
        Phase newPhase = new Phase(orderNum, play);
        play.getPhases().add(newPhase);

        // Set the phase for each playerPhase and add to the phase
        for (PlayerPhase playerPhase : playerPhases) {
            playerPhase.setPhase(newPhase);
            session.save(playerPhase); // Save each PlayerPhase
        }

        // Save the new phase
        session.save(newPhase);
        session.getTransaction().commit();
        session.close();

        return new BasicResponse(true, Errors.NO_ERRORS);
    }


    private Play getPlayByUserAndName(User user, String name) {
        Session session = this.sessionFactory.openSession();
        Play play = session.createQuery("FROM Play WHERE owner.id = :userId AND name = :playName", Play.class)
                .setParameter("userId", user.getId())
                .setParameter("playName", name)
                .uniqueResult();
        session.close();
        return play;
    }




}
