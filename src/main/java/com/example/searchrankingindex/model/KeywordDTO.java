package com.example.searchrankingindex.model;

import java.util.List;

public class KeywordDTO {
    private String keyword;
    private List<TimestampDTO> timestampRanks;

    public KeywordDTO(String keyword, List<TimestampDTO> timestampRanks) {
        this.keyword = keyword;
        this.timestampRanks = timestampRanks;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<TimestampDTO> getTimestampRanks() {
        return timestampRanks;
    }

    public void setTimestampRanks(List<TimestampDTO> timestampRanks) {
        this.timestampRanks = timestampRanks;
    }
}
