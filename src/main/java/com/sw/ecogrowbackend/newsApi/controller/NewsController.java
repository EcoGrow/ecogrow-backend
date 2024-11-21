package com.sw.ecogrowbackend.newsApi.controller;

import com.sw.ecogrowbackend.newsApi.dto.NewsResponseDto;
import com.sw.ecogrowbackend.newsApi.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
     * @param start  검색 결과의 시작 인덱스를 지정합니다 (예: 첫 번째 페이지의 경우 1).
     * @param display 페이지당 표시할 뉴스 항목의 수를 지정합니다.
     * @return 뉴스 데이터를 포함한 NewsResponseDto 객체 리스트를 반환합니다.
     *
     * 페이지당 결과 수를 조절할 수 있습니다.
     */
    @GetMapping("/search")public List<NewsResponseDto> searchNews(
            @RequestParam(defaultValue = "1") int start,
            @RequestParam(defaultValue = "9") int display) {
        return newsService.searchNews(start, display);
    }
}