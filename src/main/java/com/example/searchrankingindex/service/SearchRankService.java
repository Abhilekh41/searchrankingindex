package com.example.searchrankingindex.service;

import com.example.searchrankingindex.model.AsinDTO;
import com.example.searchrankingindex.model.AsinKeyComboDTO;
import com.example.searchrankingindex.model.KeywordDTO;

import java.util.List;

public interface SearchRankService {

    /**
     *
     * @param keyword keyword of the product
     * @param asin asin of the product
     * @return list of AsinKeyComboDTO
     */
    List<AsinKeyComboDTO> getRankingDataByKeywordAndAsin(String keyword, String asin);

    /**
     *
     * @param keyword keyword of the product
     * @return KeywordDTO
     */
    KeywordDTO getRankingDataByKeyword(String keyword);

    /**
     *
     * @param asin asin of the product
     * @return AsinDTO
     */
    AsinDTO getRankingDataByAsin(String asin);
}
