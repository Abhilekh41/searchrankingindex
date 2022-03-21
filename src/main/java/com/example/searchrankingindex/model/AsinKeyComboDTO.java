package com.example.searchrankingindex.model;

import java.util.Date;

public class AsinKeyComboDTO {

    private Date timestamp;
    private int rank;

    public AsinKeyComboDTO(Date timestamp, int rank) {
        this.timestamp = timestamp;
        this.rank = rank;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
