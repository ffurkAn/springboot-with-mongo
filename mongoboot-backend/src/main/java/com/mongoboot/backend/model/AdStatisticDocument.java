package com.sahibinden.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(value = "ad_statistic_document")
public class AdStatisticDocument {

    @Id
    private String adId;
    private Long impressionCount;
    private Long clickCount;
    private Map<String, Integer> visitorImpressionMap;
    @Version Long version;

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public Map<String, Integer> getVisitorImpressionMap() {
        return visitorImpressionMap;
    }

    public void setVisitorImpressionMap(Map<String, Integer> visitorImpressionMap) {
        this.visitorImpressionMap = visitorImpressionMap;
    }

    public Long getImpressionCount() {
        return impressionCount;
    }

    public void setImpressionCount(Long impressionCount) {
        this.impressionCount = impressionCount;
    }

    public Long getClickCount() {
        return clickCount;
    }

    public void setClickCount(Long clickCount) {
        this.clickCount = clickCount;
    }

    public void incrementImpressionCount() {
        this.impressionCount += 1;
    }


    public void incrementClickCount() {
        this.clickCount += 1;
    }
}
