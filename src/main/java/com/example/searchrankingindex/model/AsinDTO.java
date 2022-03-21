package com.example.searchrankingindex.model;

import java.util.List;

public class AsinDTO {
    private String asin;
    private List<TimestampDTO> timestampRanks;

    public AsinDTO(String asin, List<TimestampDTO> timestampRanks) {
        this.asin = asin;
        this.timestampRanks = timestampRanks;
    }

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    public List<TimestampDTO> getTimestampRanks() {
        return timestampRanks;
    }

    public void setTimestampRanks(List<TimestampDTO> timestampRanks) {
        this.timestampRanks = timestampRanks;
    }
}
