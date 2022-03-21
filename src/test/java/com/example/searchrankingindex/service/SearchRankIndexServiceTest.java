package com.example.searchrankingindex.service;

import com.example.searchrankingindex.client.S3Client;
import com.example.searchrankingindex.model.AsinDTO;
import com.example.searchrankingindex.model.AsinKeyComboDTO;
import com.example.searchrankingindex.model.KeywordDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Assertions;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SearchRankIndexServiceTest {

    @InjectMocks
    private SearchRankServiceImpl searchRankService;

    @Mock
    private S3Client s3Client;

    @Test
    void getRankingDataByKeywordAndAsinSuccess() {
        Map<String, List<AsinKeyComboDTO>> map = new HashMap<>();
        String keyword = "ABCD";
        String asin = "ABCFD";
        int rank = 1;

        String key = keyword+asin;
        map.put(key, new ArrayList<>() {
            {
                add(new AsinKeyComboDTO(getDateObject(), rank));
            }
        });

        when(s3Client.getAsinKeyComboDTOMap()).thenReturn(map);

        List<AsinKeyComboDTO> asinKeyComboDTOS = searchRankService.getRankingDataByKeywordAndAsin(keyword, asin);
        Assertions.assertEquals(asinKeyComboDTOS.size(), 1);
        Assertions.assertEquals(asinKeyComboDTOS.get(0).getRank(),1);
        Assertions.assertEquals(asinKeyComboDTOS.get(0).getTimestamp(), getDateObject());
    }

    @Test
    void getRankingDataByKeywordAndAsinFailure() {
        Map<String, List<AsinKeyComboDTO>> map = new HashMap<>();

        when(s3Client.getAsinKeyComboDTOMap()).thenReturn(map);

        List<AsinKeyComboDTO> asinKeyComboDTOS = searchRankService.getRankingDataByKeywordAndAsin("TARGET", "RETAIL");
        Assertions.assertEquals(asinKeyComboDTOS.size(), 0);
    }

    @Test
    void getRankingDataByKeywordSuccess() {
        String keyword = "ABCD";
        int rank = 1;
        long timestamp = 1637024931000L;
        Map<String, Map<Long, List<Integer>>> map = new HashMap<>();
        map.put(keyword, new HashMap<>());
        map.get(keyword).put(timestamp, new ArrayList<>() {
            {
                add(rank);
            }
        });

        when(s3Client.getKeyWordsMap()).thenReturn(map);

        KeywordDTO keywordDTO = searchRankService.getRankingDataByKeyword(keyword);
        Assertions.assertEquals(keywordDTO.getKeyword(), "ABCD");
        Assertions.assertEquals(keywordDTO.getTimestampRanks().get(0).getRanks().get(0), 1);
    }

    @Test
    void getRankingDataByKeywordFailure() {
        Map<String, Map<Long, List<Integer>>> keyWordsMap = new HashMap<>();

        when(s3Client.getKeyWordsMap()).thenReturn(keyWordsMap);

        KeywordDTO keywordDTO = searchRankService.getRankingDataByKeyword("ABCD");
        Assertions.assertEquals(keywordDTO.getKeyword(), "ABCD");
    }

    @Test
    void testGetByAsinSuccess() {
        String asin = "ABCD";
        int rank = 1;
        long timestamp = 1637024931000L;
        Map<String, Map<Long, List<Integer>>> asinMap = new HashMap<>();
        asinMap.put(asin, new HashMap<>());
        asinMap.get(asin).put(timestamp, new ArrayList<>() {
            {
                add(rank);
            }
        });

        when(s3Client.getAsinMap()).thenReturn(asinMap);
        AsinDTO asinDTO = searchRankService.getRankingDataByAsin(asin);

        Assertions.assertEquals(asinDTO.getAsin(), "ABCD");
        Assertions.assertEquals(asinDTO.getTimestampRanks().size(),1);
        Assertions.assertEquals(asinDTO.getTimestampRanks().get(0).getRanks().get(0), 1);
    }

    @Test
    void testGetByAsinFailure() {
        Map<String, Map<Long, List<Integer>>> asinMap = new HashMap<>();
        when(s3Client.getAsinMap()).thenReturn(asinMap);
        AsinDTO asinDTO = searchRankService.getRankingDataByAsin("ABCD");
        Assertions.assertEquals(asinDTO.getAsin(), "ABCD");
        Assertions.assertEquals(asinDTO.getTimestampRanks().size(),0);
    }

    private Date getDateObject() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,17);
        cal.set(Calendar.MINUTE,30);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        return cal.getTime();
    }
}
