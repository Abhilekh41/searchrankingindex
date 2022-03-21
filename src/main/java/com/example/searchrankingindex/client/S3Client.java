package com.example.searchrankingindex.client;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.example.searchrankingindex.exception.CSVReaderException;
import com.example.searchrankingindex.model.AsinKeyComboDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class S3Client {

    @Value("${accessKey}")
    private String accessKey;

    @Value("${secretKey}")
    private String secretKey;

    @Value("${caseStudyBucketName}")
    private String caseStudyBucketName;

    private Map<String, List<AsinKeyComboDTO>> asinKeyComboDTOMap = new ConcurrentHashMap<>();
    private Map<String, Map<Long, List<Integer>>> keyWordsMap = new ConcurrentHashMap<>();
    private Map<String, Map<Long, List<Integer>>> asinMap = new ConcurrentHashMap<>();

    public Map<String, List<AsinKeyComboDTO>> getAsinKeyComboDTOMap() {
        return asinKeyComboDTOMap;
    }

    public Map<String, Map<Long, List<Integer>>> getKeyWordsMap() {
        return keyWordsMap;
    }

    public Map<String, Map<Long, List<Integer>>> getAsinMap() {
        return asinMap;
    }

    @PostConstruct
    private void establishConnectionAndFetchCSV() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        AmazonS3 s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.EU_CENTRAL_1)
                .build();

        S3Object s3object = s3client.getObject(caseStudyBucketName, "public/case-keywords.csv");
        S3ObjectInputStream inputStream = s3object.getObjectContent();

        try {
            BufferedReader csvReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String row = csvReader.readLine();

            while((row = csvReader.readLine()) != null) {
                String[] values = row.split(";");
                Long timestamp = Long.parseLong(values[0]) * 1000;
                String ASIN = values[1];
                String[] keywords = values[2].split(" ");
                Integer rank = Integer.parseInt(values[3]);

                populateAsinKeyComboDTOMap(timestamp, ASIN, keywords, rank);
                populateKeyWordsMap(timestamp, keywords, rank);
                populateAsinMap(ASIN, timestamp, rank);
            }
        } catch(IOException ioException) {
                throw new CSVReaderException("Could not parse CSV");
        }
    }

    private void populateAsinKeyComboDTOMap(Long timestamp, String asin, String[] keywords, Integer rank) {
        for (String keyword : keywords) {
            String key = keyword+asin;
            if (asinKeyComboDTOMap.containsKey(key)) {
                asinKeyComboDTOMap.get(key).add(new AsinKeyComboDTO(new Date(timestamp), rank));
            } else {
                asinKeyComboDTOMap.put(key, new ArrayList<>(Collections.singletonList(new AsinKeyComboDTO(new Date(timestamp), rank))));
            }
        }
    }

    private void populateKeyWordsMap(Long timestamp, String[] keywords, Integer rank) {
        for (String keyword : keywords) {
            if (keyWordsMap.containsKey(keyword)) {
                if (keyWordsMap.get(keyword).containsKey(timestamp)) {
                    keyWordsMap.get(keyword).get(timestamp).add(rank);
                } else {
                    keyWordsMap.get(keyword).put(timestamp, new ArrayList<>(Collections.singletonList(rank)));
                }
            } else {
                keyWordsMap.put(keyword, new HashMap<>());
                keyWordsMap.get(keyword).put(timestamp, new ArrayList<>(Collections.singletonList(rank)));
            }
        }
    }

    private void populateAsinMap(String asin, Long time, Integer rank) {
        if (asinMap.containsKey(asin)) {
            if (asinMap.get(asin).containsKey(time)) {
                asinMap.get(asin).get(time).add(rank);
            } else {
                asinMap.get(asin).put(time, new ArrayList<>(Collections.singletonList(rank)));
            }
        } else {
            asinMap.put(asin, new HashMap<>());
            asinMap.get(asin).put(time, new ArrayList<>(Collections.singletonList(rank)));
        }
    }
}
