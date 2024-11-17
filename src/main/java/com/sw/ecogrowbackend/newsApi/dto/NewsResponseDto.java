package com.sw.ecogrowbackend.newsApi.dto;

import lombok.Getter;

@Getter
public class NewsResponseDto {

    private final String title;        // 뉴스 제목
    private final String link;         // 뉴스 링크
    private final String description;  // 뉴스 요약
    private final String pubDate;      // 뉴스 발행 날짜

    private NewsResponseDto(Builder builder) {
        this.title = builder.title;
        this.link = builder.link;
        this.description = builder.description;
        this.pubDate = builder.pubDate;
    }

    public static class Builder {

        private String title;
        private String link;
        private String description;
        private String pubDate;

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder link(String link) {
            this.link = link;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder pubDate(String pubDate) {
            this.pubDate = pubDate;
            return this;
        }

        public NewsResponseDto build() {
            return new NewsResponseDto(this);
        }
    }
}