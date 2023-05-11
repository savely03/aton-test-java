package com.savely.repository;

import com.savely.exception.UserAlreadyAddedException;
import com.savely.exception.UserNotFoundException;
import com.savely.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class UserRepository {

    private final Map<Long, User> usersByAccount;
    private final Map<String, User> usersByName;
    private final Map<Double, User> usersByValue;

    public UserRepository() {
        this.usersByAccount = new HashMap<>();
        this.usersByName = new HashMap<>();
        this.usersByValue = new HashMap<>();
    }

    public void addNewUser(long account, String name, double value) {
        if (Objects.isNull(usersByAccount.get(account)) && Objects.isNull(usersByName.get(name))
                && Objects.isNull(usersByValue.get(value))) {
            User user = new User(account, name, value);
            usersByAccount.put(account, user);
            usersByName.put(name, user);
            usersByValue.put(value, user);
            return;
        }
        throw new UserAlreadyAddedException();
    }


    private void deleteUser(User user) {
        if (Objects.nonNull(user)) {
            usersByAccount.remove(user.getAccount());
            usersByName.remove(user.getName());
            usersByValue.remove(user.getValue());
            return;
        }
        throw new UserNotFoundException();
    }


    public void deleteByAccount(long account) {
        User user = usersByAccount.get(account);
        deleteUser(user);
    }

    public void deleteByName(String name) {
        User user = usersByName.get(name);
        deleteUser(user);
    }

    public void deleteByValue(double value) {
        User user = usersByValue.get(value);
        deleteUser(user);
    }


    public void updateUserValueByAccount(long account, double newValue) {
        User user = usersByAccount.get(account);
        if (Objects.nonNull(user)) {
            user.setValue(newValue);
            return;
        }
        throw new UserNotFoundException();
    }


    public Optional<User> getByAccount(long account) {
        return Optional.ofNullable(usersByAccount.get(account));
    }

    public Optional<User> getByName(String name) {
        return Optional.ofNullable(usersByName.get(name));
    }

    public Optional<User> getByValue(double value) {
        return Optional.ofNullable(usersByValue.get(value));
    }

    public void clearRepository() {
        usersByAccount.clear();
        usersByName.clear();
        usersByValue.clear();
    }

}
