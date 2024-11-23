package com.sw.ecogrowbackend.domain.newsApi.controller;

import com.sw.ecogrowbackend.domain.newsApi.dto.NewsResponseDto;
import com.sw.ecogrowbackend.domain.newsApi.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/news")
public class NewsController {

    private final NewsService newsService;

    /**
     * 페이지네이션을 지원하는 뉴스 검색 엔드포인트.
     *
     * @param pageable 페이지 정보
     * @return 페이지화된 뉴스 데이터를 포함한 Page 객체를 반환합니다.
     */
    @GetMapping("/search")
    public Page<NewsResponseDto> searchNews(Pageable pageable) {
        return newsService.searchNews(pageable);
    }
}