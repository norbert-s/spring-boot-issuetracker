package com.issuetracker;

import com.issuetracker.config.MyTestConfig;
import com.issuetracker.dataJpa.entity.Issue;
import com.issuetracker.database_integration.UtilityMethods;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Import(MyTestConfig.class)
public class TestRealEndPoints {
    protected static final Logger LOGGER = LogManager.getLogger(TestRealEndPoints.class);

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    UtilityMethods utilityMethods;

    @BeforeAll
    public static void setup(){

    }

    @Test
    public void returningAllIssuesEndpoint() {
        ResponseEntity<List<Issue>> responseEntity = restTemplate.exchange(
                "http://localhost:8080/api/issues",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Issue>>() {}
        );
        List<Issue> issues = responseEntity.getBody();
        assertNotNull(issues);
    }
    @Test
    public void creatingIssueEndpoint() {
        Issue issue = new Issue();
        issue.setAssigneeName("Robert Redford");
        issue.setDescription("the application crashes after the login screen appears");
        issue.setTitle("app crashes");
        issue.setStatus("open");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Issue> requestEntity = new HttpEntity<>(issue, headers);

        ResponseEntity<Issue> responseEntity = restTemplate.exchange(
                "http://localhost:8080/api/issues",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<Issue>() {}
        );
        Issue issue1 = responseEntity.getBody();
        assertNotNull(issue1);

    }

    @Test
    public void deletingIssueByIdEndpoint() {
        Issue issue = utilityMethods.createIssue();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        //HttpEntity<Issue> requestEntity = new HttpEntity<>(issue, headers);
        int id = issue.getId();
        LOGGER.info("id -> "+id);
        ResponseEntity<Issue> responseEntity = restTemplate.exchange(
                "http://localhost:8080/api/issues/{id}",
                HttpMethod.DELETE,
                null,
                Issue.class,
                id
        );
    }
}
