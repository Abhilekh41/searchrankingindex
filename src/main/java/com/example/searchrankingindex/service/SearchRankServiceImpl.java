package com.example.searchrankingindex.service;

import com.example.searchrankingindex.client.S3Client;
import com.example.searchrankingindex.model.AsinDTO;
import com.example.searchrankingindex.model.AsinKeyComboDTO;
import com.example.searchrankingindex.model.KeywordDTO;
import com.example.searchrankingindex.model.TimestampDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SearchRankServiceImpl implements SearchRankService {

    private S3Client s3Client;

    @Autowired
    public SearchRankServiceImpl(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public List<AsinKeyComboDTO> getRankingDataByKeywordAndAsin(String keyword, String asin) {
        Map<String, List<AsinKeyComboDTO>> keywordMap = s3Client.getAsinKeyComboDTOMap();
        String key = keyword+asin;
        if (keywordMap.containsKey(key)) {
            return keywordMap.get(key)
                    .stream()
                    .map(value -> new AsinKeyComboDTO(value.getTimestamp(), value.getRank()))
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public KeywordDTO getRankingDataByKeyword(String keyword) {
        Map<String, Map<Long, List<Integer>>> keywordMap = s3Client.getKeyWordsMap();
        List<TimestampDTO> values = new ArrayList<>();

        if(keywordMap.containsKey(keyword)){
            for (var entry : keywordMap.get(keyword).entrySet()) {
                values.add(new TimestampDTO(new Date(entry.getKey()), entry.getValue()));
            }
        }
        return new KeywordDTO(keyword, values);
    }

    @Override
    public AsinDTO getRankingDataByAsin(String asin) {
        Map<String, Map<Long, List<Integer>>> asinMap = s3Client.getAsinMap();
        List<TimestampDTO> values = new ArrayList<>();

        if(asinMap.containsKey(asin)){
            for (var entry : asinMap.get(asin).entrySet()) {
                values.add(new TimestampDTO(new Date(entry.getKey()), entry.getValue()));
            }
        }
        return new AsinDTO(asin, values);
    }
}
