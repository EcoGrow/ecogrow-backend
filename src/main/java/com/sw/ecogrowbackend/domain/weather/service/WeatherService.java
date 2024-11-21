package com.sw.ecogrowbackend.domain.weather.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.jsoup.Jsoup;
import java.io.IOException;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class WeatherService {

    @Value("${weather.api.url}")
    private String apiUrl;

    @Value("${weather.api.key}")
    private String apiKey;

    public String getTemperature() {
        // 현재 시간 가져오기
        Date current = new Date(System.currentTimeMillis());
        SimpleDateFormat d_format = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = d_format.format(current).substring(0, 8); // 날짜
        String time = d_format.format(current).substring(8, 10); // 시간

        // 기상청 API URL
        String url = apiUrl
                + "?serviceKey=" + apiKey
                + "&pageNo=1&numOfRows=1000"
                + "&dataType=XML"
                + "&base_date=" + date
                + "&base_time=" + time + "00"
                + "&base_time=" + time + "00"
                + "&nx=73"
                + "&ny=134";

        Document doc = null;
        String temp = "-99";

        try {
            doc = Jsoup.connect(url).get();
        } catch(IOException e) {
            e.printStackTrace();
        }

        // 기온 데이터 추출
        Elements elements = doc.select("item");
        for (Element e : elements) {
            if (e.select("category").text().equals("T1H")) {
                temp = e.select("obsrValue").text();
            }
        }
        return temp + "°C";
    }
}