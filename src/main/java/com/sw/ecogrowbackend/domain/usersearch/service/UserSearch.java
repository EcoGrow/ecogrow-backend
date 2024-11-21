package com.sw.ecogrowbackend.domain.usersearch.service;

import com.sw.ecogrowbackend.domain.auth.entity.User;
import com.sw.ecogrowbackend.domain.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserSearch {

    @Autowired
    private UserRepository userRepository;

    // 유저 ID로 검색
    public List<User> searchUsers(String query) {
        return userRepository.findAll().stream()
            .filter(user -> user.getId().toString().contains(query))
            .collect(Collectors.toList());
    }
}
