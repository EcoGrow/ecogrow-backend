package com.sw.ecogrowbackend.newsApi.dto;

import java.util.List;

public class NewsResponseDto {

    private String title;     // 뉴스 제목
    private String link;      // 뉴스 링크
    private String description;  // 뉴스 요약
    private String pubDate;   // 뉴스 발행 날짜

    // 생성자, getter, setter
    public NewsResponseDto(String title, String link, String description, String pubDate) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.pubDate = pubDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }
}