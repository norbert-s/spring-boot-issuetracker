package com.issuetracker;

import com.issuetracker.config.MyTestConfig;
import com.issuetracker.dataJpa.entity.Issue;
import com.issuetracker.database_integration.DatabaseQueries;
import com.issuetracker.issue_object_generator.IssuePOJO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Import(MyTestConfig.class)
public class TestRealEndPoints {
    protected static final Logger LOGGER = LogManager.getLogger(TestRealEndPoints.class);

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    DatabaseQueries dbQueries;
    private Issue testIssue;

    @BeforeEach
    public  void setup() {
        testIssue = IssuePOJO.issueGenerator();
    }


    @Test
    public void returningIssueByIdEndpoint() {
        Optional<Issue> createdIssue = Optional.ofNullable(dbQueries.saveIssue());
        if(createdIssue.isPresent()){
            ResponseEntity<Issue> responseEntity = restTemplate.exchange(
                    "http://localhost:8080/api/issues/{id}",
                    HttpMethod.GET,
                    null,
                    Issue.class,
                    createdIssue.get().getId()
            );
            Optional<Issue> foundIssue = Optional.ofNullable(responseEntity.getBody());
            assertTrue(foundIssue.get().equalsWithoutCheckingId(createdIssue.get()));
        }else{
            throw new RuntimeException("issue is not present");
        }


    }
    @Test
    public void creatingIssueEndpoint() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Issue> requestEntity = new HttpEntity<>(testIssue, headers);

        ResponseEntity<Issue> responseEntity = restTemplate.exchange(
                "http://localhost:8080/api/issues",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<Issue>() {}
        );
        Issue returnedIssue = responseEntity.getBody();
        assertTrue(returnedIssue.equalsWithoutCheckingId(testIssue));

        //delete from db and check if passes
        dbQueries.deleteFromDbAndCheckIfPasses(returnedIssue.getId());

    }

    @Test
    public void deletingIssueByIdEndpoint() {
        Issue issue = dbQueries.saveIssue();
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
        //assert correct issue has been deleted

        LOGGER.info(responseEntity.getBody());
        LOGGER.info(issue);
        assertTrue(responseEntity.getBody().equalsWithoutCheckingId(issue));

        //assert issue was not found after deletion
        dbQueries.selectAllFromDbByIdAndAssertThatItIsEmpty(id);
    }
}
