package com.sw.ecogrowbackend.domain.usersearch.controller;

import com.sw.ecogrowbackend.domain.auth.entity.User;
import com.sw.ecogrowbackend.domain.usersearch.service.UserSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000") // 프론트엔드 도메인 주소 허용
public class SearchController {

    @Autowired
    private UserSearch userSearchService;

    @GetMapping
    public List<User> searchUsers(@RequestParam String email) {
        return userSearchService.searchUsersByEmail(email);
    }
}

