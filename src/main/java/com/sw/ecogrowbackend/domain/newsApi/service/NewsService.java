package com.sw.ecogrowbackend.domain.newsApi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sw.ecogrowbackend.domain.newsApi.dto.NewsResponseDto;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class NewsService {

    @Value("${naver.client.id}")
    private String clientId;

    @Value("${naver.client.secret}")
    private String clientSecret;

    public Page<NewsResponseDto> searchNews(Pageable pageable) {
        String text;
        try {
            text = URLEncoder.encode("환경", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("검색어 인코딩 실패", e);
        }

        int start = (int) pageable.getOffset() + 1;
        int display = pageable.getPageSize();

        String apiURL = "https://openapi.naver.com/v1/search/news?query=" + text + "&start=" + start
            + "&display=" + display;

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);

        String responseBody = get(apiURL, requestHeaders);
        List<NewsResponseDto> newsList = parseResponseToDto(responseBody);

        long totalElements = 100;
        return new PageImpl<>(newsList, pageable, totalElements);
    }

    private static String get(String apiUrl, Map<String, String> requestHeaders) {
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 오류 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private static HttpURLConnection connect(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            throw new RuntimeException("API URL 연결 실패: " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body) {
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();
            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는 데 실패했습니다.", e);
        }
    }

    private List<NewsResponseDto> parseResponseToDto(String responseBody) {
        List<NewsResponseDto> newsList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode items = root.path("items");

            for (JsonNode item : items) {
                NewsResponseDto newsDto = new NewsResponseDto.Builder()
                    .title(item.path("title").asText())
                    .link(item.path("link").asText())
                    .description(item.path("description").asText())
                    .pubDate(item.path("pubDate").asText())
                    .build();
                newsList.add(newsDto);
            }
        } catch (IOException e) {
            throw new RuntimeException("응답 파싱 중 오류 발생", e);
        }

        return newsList;
    }
}