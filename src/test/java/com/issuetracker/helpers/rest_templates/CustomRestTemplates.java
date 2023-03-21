package com.issuetracker.helpers.rest_templates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CustomRestTemplates {
    @Value("${api.protocol}")
    private String protocol;
    @Value("${api.hostname}")
    private String hostName;
    @Value("${server.port}")
    private String serverPort;

    @Autowired
    private TestRestTemplate restTemplate;

    public <T> ResponseEntity<T> post(String endPoint, Object requestBody, Class<T> responseType){
        String url = protocol + hostName + ":" + serverPort +endPoint;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Object> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<T> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<T>() {}
        );
        return (ResponseEntity<T>) responseEntity;
        //return restTemplate.postForEntity(url, requestEntity, responseType);
    }
//    public <T> ResponseEntity<T> get(String endPoint, Class<T> responseType){
//        String url = protocol + hostName + ":" + serverPort +endPoint;
//
//        ResponseEntity<Issue> responseEntity = restTemplate.exchange(
//                url,
//                HttpMethod.GET,
//                null,
//                Issue.class,
//                createdIssue.get().getId()
//        );
//        return (ResponseEntity<T>) responseEntity;
//        //return restTemplate.postForEntity(url, request, responseType);
//    }
}
