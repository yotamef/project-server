package com.dev;

import com.dev.objects.Friendship;
import com.dev.objects.User;
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
        if (username == null || password == null || repeatPassword == null) {
            return new BasicResponse(false, Errors.ERROR_MISSING_FIELDS);
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
    public ListUserResponse getCurrentFriends(String secret) {
        User user = getUserBySecret(secret);
        if (user == null) {
            return new ListUserResponse(false, ERROR_NO_SUCH_SECRET, List.of());
        } else {
            return new ListUserResponse(true, NO_ERRORS, user.getCurrentFriends());
        }
    }
}
