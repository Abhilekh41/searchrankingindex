package com.example.searchrankingindex.controller;

import com.example.searchrankingindex.model.AsinDTO;
import com.example.searchrankingindex.model.AsinKeyComboDTO;
import com.example.searchrankingindex.model.KeywordDTO;
import com.example.searchrankingindex.service.SearchRankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ranking")
public class RankController {

    private SearchRankService searchRankService;

    @Autowired
    public RankController(SearchRankService searchRankService) {
        this.searchRankService = searchRankService;
    }

    @GetMapping("/asinKeywordCombo/{keyword}/{asin}")
    public ResponseEntity<?> getRankingDataByKeywordAndAsin(@PathVariable("keyword") String keyword,
                                                                                @PathVariable("asin") String asin){
        List<AsinKeyComboDTO> asinKeyComboDTOS = searchRankService.getRankingDataByKeywordAndAsin(keyword, asin);
        if(!CollectionUtils.isEmpty(asinKeyComboDTOS)) {
            return new ResponseEntity<>(asinKeyComboDTOS, HttpStatus.OK);
        }
        return ResponseEntity.badRequest().body(Map.of("error", "No Ranking Data found for keyword and asin id provided"));
    }

    @GetMapping("keyword/{keyword}")
    public ResponseEntity<?> getRankingDataByKeyword(@PathVariable("keyword") String keyword){
        KeywordDTO keywordDTO = searchRankService.getRankingDataByKeyword(keyword);
        if(!CollectionUtils.isEmpty(keywordDTO.getTimestampRanks())) {
            return new ResponseEntity<>(searchRankService.getRankingDataByKeyword(keyword), HttpStatus.OK);
        }
        return ResponseEntity.badRequest().body(Map.of("error", "No Ranking Data Found for keyword"));
    }

    @GetMapping("asin/{asin}")
    public ResponseEntity<?> getRankingDataByAsin(@PathVariable("asin") String asin){
        AsinDTO asinDTO = searchRankService.getRankingDataByAsin(asin);
        if(!CollectionUtils.isEmpty(asinDTO.getTimestampRanks())) {
            return new ResponseEntity<>(searchRankService.getRankingDataByAsin(asin), HttpStatus.OK);
        }
        return ResponseEntity.badRequest().body(Map.of("error", "No Ranking Data Found for asin"));
    }
}
