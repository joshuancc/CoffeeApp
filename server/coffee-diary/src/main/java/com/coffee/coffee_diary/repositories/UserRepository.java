package com.coffee.coffee_diary.repositories;

import com.coffee.coffee_diary.containers.GetLoginPasswordResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class UserRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public UserRepository() {
    }

    public boolean existsWithEmail(String email) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject("SELECT EXISTS(SELECT 1 FROM users WHERE email=?)", Boolean.class, email));
    }

    public void addUser(String email, String password) {
        jdbcTemplate.update("INSERT INTO users (user_id, email, password) VALUES (?, ?, ?)", UUID.randomUUID(), email, password);
    }

    public GetLoginPasswordResult getLoginPassword(String email) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT user_id, password FROM users WHERE email=?",
                    (resultSet, _) -> new GetLoginPasswordResult(resultSet.getString("user_id"), resultSet.getString("password")),
                    email
            );
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }
}
