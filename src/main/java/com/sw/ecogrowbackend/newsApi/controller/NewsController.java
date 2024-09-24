package com.sw.ecogrowbackend.newsApi.controller;

import com.sw.ecogrowbackend.newsApi.dto.NewsResponseDto;
import com.sw.ecogrowbackend.newsApi.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping("/search")
    public List<NewsResponseDto> searchNews() {
        return newsService.searchNews();
    }
}