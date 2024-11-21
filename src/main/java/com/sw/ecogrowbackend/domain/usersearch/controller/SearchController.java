package com.sw.ecogrowbackend.domain.usersearch.controller;

import com.sw.ecogrowbackend.domain.auth.entity.User;
import com.sw.ecogrowbackend.domain.usersearch.service.UserSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class SearchController {

    @Autowired
    private UserSearch userSearchService;

    @GetMapping
    public List<User> searchUsers(@RequestParam String query) {
        return userSearchService.searchUsers(query);
    }
}

