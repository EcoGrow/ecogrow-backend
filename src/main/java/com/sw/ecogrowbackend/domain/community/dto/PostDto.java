package com.sw.ecogrowbackend.domain.community.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private String title;
    private String content;
    private boolean isNotice;
}