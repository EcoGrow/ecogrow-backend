package com.sw.ecogrowbackend.domain.usersearch.service;

import com.sw.ecogrowbackend.domain.auth.entity.User;
import com.sw.ecogrowbackend.domain.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserSearch {

    @Autowired
    private UserRepository userRepository;

    // 이메일로 유저 검색
    public List<User> searchUsersByEmail(String email) {
        return userRepository.findByEmail(email)
            .map(Collections::singletonList)
            .orElse(Collections.emptyList());
    }
}