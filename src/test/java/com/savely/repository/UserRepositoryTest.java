package com.savely.repository;

import com.savely.exception.UserAlreadyAddedException;
import com.savely.exception.UserNotFoundException;
import com.savely.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static com.savely.constants.UserRepositoryConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    private final UserRepository repository = new UserRepository();

    @AfterEach
    void clearRepository() {
        repository.clearRepository();
    }

    @Test
    void addNewUser() {
        repository.addNewUser(ACCOUNT, NAME, VALUE);

        assertAll(
                () -> assertThat(repository.getByAccount(ACCOUNT)).isPresent(),
                () -> assertThat(repository.getByName(NAME)).isPresent(),
                () -> assertThat(repository.getByValue(VALUE)).isPresent()
        );
    }

    @Test
    void addNewUser_WhenUserAlreadyAdded() {
        repository.addNewUser(ACCOUNT, NAME, VALUE);

        assertThrows(UserAlreadyAddedException.class, () -> repository.addNewUser(ACCOUNT, NAME, VALUE));
    }

    @Test
    void deleteByAccount() {
        repository.addNewUser(ACCOUNT, NAME, VALUE);

        repository.deleteByAccount(ACCOUNT);

        assertAll(
                () -> assertThat(repository.getByAccount(ACCOUNT)).isEmpty(),
                () -> assertThat(repository.getByName(NAME)).isEmpty(),
                () -> assertThat(repository.getByValue(VALUE)).isEmpty()
        );

    }

    @Test
    void deleteByAccount_WhenUserNotFound() {
        assertThrows(UserNotFoundException.class, () -> repository.deleteByAccount(ACCOUNT));
    }

    @Test
    void deleteByName() {
        repository.addNewUser(ACCOUNT, NAME, VALUE);

        repository.deleteByName(NAME);

        assertAll(
                () -> assertThat(repository.getByName(NAME)).isEmpty(),
                () -> assertThat(repository.getByAccount(ACCOUNT)).isEmpty(),
                () -> assertThat(repository.getByValue(VALUE)).isEmpty()
        );
    }

    @Test
    void deleteByName_WhenUserNotFound() {
        assertThrows(UserNotFoundException.class, () -> repository.deleteByName(NAME));
    }

    @Test
    void deleteByValue() {
        repository.addNewUser(ACCOUNT, NAME, VALUE);

        repository.deleteByValue(VALUE);

        assertAll(
                () -> assertThat(repository.getByValue(VALUE)).isEmpty(),
                () -> assertThat(repository.getByAccount(ACCOUNT)).isEmpty(),
                () -> assertThat(repository.getByName(NAME)).isEmpty()
        );
    }

    @Test
    void deleteByValue_WhenUserNotFound() {
        assertThrows(UserNotFoundException.class, () -> repository.deleteByValue(VALUE));
    }

    @Test
    void updateUserValueByAccount() {
        repository.addNewUser(ACCOUNT, NAME, VALUE);
        double newValue = ThreadLocalRandom.current().nextDouble(Double.MAX_VALUE);

        repository.updateUserValueByAccount(ACCOUNT, newValue);
        Optional<User> user = repository.getByAccount(ACCOUNT);

        user.ifPresent(u -> assertThat(u.getValue()).isEqualTo(newValue));
    }

    @Test
    void updateUserValueByAccount_WhenUserNotFound() {
        double newValue = ThreadLocalRandom.current().nextDouble(Double.MAX_VALUE);

        assertThrows(UserNotFoundException.class, () -> repository.updateUserValueByAccount(ACCOUNT, newValue));
    }

}