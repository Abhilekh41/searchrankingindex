package com.example.searchrankingindex.model;

import java.util.Date;
import java.util.List;

public class TimestampDTO {

    private Date timestamp;
    private List<Integer> ranks;

    public TimestampDTO(Date timestamp, List<Integer> ranks) {
        this.timestamp = timestamp;
        this.ranks = ranks;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public List<Integer> getRanks() {
        return ranks;
    }

    public void setRanks(List<Integer> ranks) {
        this.ranks = ranks;
    }
}
