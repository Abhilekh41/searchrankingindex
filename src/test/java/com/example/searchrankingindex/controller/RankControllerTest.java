package com.example.searchrankingindex.controller;

import com.example.searchrankingindex.model.AsinDTO;
import com.example.searchrankingindex.model.AsinKeyComboDTO;
import com.example.searchrankingindex.model.KeywordDTO;
import com.example.searchrankingindex.model.TimestampDTO;
import com.example.searchrankingindex.service.SearchRankServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Calendar;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RankController.class)
public class RankControllerTest {

    private static final String API_BASE_PATH_V1 = "/api/v1/ranking";

    @MockBean
    private SearchRankServiceImpl searchRankService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetRankingDataByKeywordAndAsinSuccess() throws Exception {

        AsinKeyComboDTO asinKeyComboDTO = new AsinKeyComboDTO(getDateObject(),Integer.valueOf(23));

        when(searchRankService.getRankingDataByKeywordAndAsin(any(String.class), any(String.class))).thenReturn(Arrays.asList(asinKeyComboDTO));

        mockMvc.perform(get(API_BASE_PATH_V1+"/asinKeywordCombo/ABCD/EDFS"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON));
    }

    @Test
    void testGetRankingDataByKeywordAndAsinFailure() throws Exception {

        when(searchRankService.getRankingDataByKeywordAndAsin(any(String.class), any(String.class))).thenReturn(new ArrayList<>());

        mockMvc.perform(get(API_BASE_PATH_V1+"/asinKeywordCombo/ABCD/EDFS"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getRankingDataByKeywordSuccess() throws Exception {
        List<TimestampDTO> timestampDTOList = Arrays.asList(new TimestampDTO(getDateObject(),Arrays.asList(12)));
        KeywordDTO keywordDTO = new KeywordDTO("ABCD", timestampDTOList);
        when(searchRankService.getRankingDataByKeyword(any(String.class))).thenReturn(keywordDTO);

        mockMvc.perform(get(API_BASE_PATH_V1+"/keyword/ABCD"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON));
    }

    @Test
    void getRankingDataByKeywordFailure() throws Exception {
        when(searchRankService.getRankingDataByKeyword(any(String.class))).thenReturn(null);

        mockMvc.perform(get(API_BASE_PATH_V1+"/keyword/ABCD"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getRankingDataByAsinSuccess() throws Exception {
        List<TimestampDTO> timestampDTOList = Arrays.asList(new TimestampDTO(getDateObject(),Arrays.asList(12)));
        AsinDTO asinDTO = new AsinDTO("ABCD", timestampDTOList);

        when(searchRankService.getRankingDataByAsin(any(String.class))).thenReturn(asinDTO);

        mockMvc.perform(get(API_BASE_PATH_V1+"/asin/ABCD"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON));
    }

    @Test
    void getRankingDataByAsinFailure() throws Exception {
        when(searchRankService.getRankingDataByAsin(any(String.class))).thenReturn(null);

        mockMvc.perform(get(API_BASE_PATH_V1+"/asin/ABCD"))
                .andExpect(status().isBadRequest());
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
