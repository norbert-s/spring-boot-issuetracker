package com.issuetracker.tests.integration;

import com.issuetracker.dataJpa.entity.Issue;
import com.issuetracker.dataJpa.exceptionhandling.ErrorResponse;
import com.issuetracker.helpers.config.MyTestConfig;
import com.issuetracker.helpers.issue_object_generator.IssuePOJO;
import com.issuetracker.helpers.sql_queries.DatabaseQueries;
import com.issuetracker.helpers.exceptions.ThrowsWhenIssue;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=${test.server.port}"})
@Import(MyTestConfig.class)
@TestPropertySource("/${environmentParam}.properties")
@Tag("sanity")
@Tag("real-endpoint")
@Slf4j
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class RealEndPointDatabaseIntegrationTest {
    //private static final Logger LOGGER = LogManager.getLogger(RealEndPointDatabaseIntegrationTest.class);

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    DatabaseQueries dbQueries;
    private Issue testIssue;

    @BeforeEach
    public void setup() {
        testIssue = IssuePOJO.issueGenerator();
    }

    @Value("${api.protocol}")
    String protocol;
    @Value("${api.hostname}")
    String hostName;
    @Value("${server.port}")
    String serverPort;

    @Test
    public void returningIssueByIdEndpoint() {
        Issue createdIssue = Optional.ofNullable(dbQueries.saveIssue()).orElseThrow(ThrowsWhenIssue.isNotPresent);

        ResponseEntity<Issue> responseEntity = restTemplate.exchange(
                protocol + hostName + ":" + serverPort + "/api/issues/{id}",
                HttpMethod.GET,
                null,
                Issue.class,
                createdIssue.getId()
        );

        Issue foundIssue = Optional.ofNullable(responseEntity.getBody()).orElseThrow(ThrowsWhenIssue.isNotPresent);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(foundIssue.equalsWithoutCheckingId(createdIssue));
        dbQueries.deleteFromDbAndAssertDeletionSuccessful(createdIssue.getId());
    }

    @Test
    public void creatingIssueEndpoint() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Issue> requestEntity = new HttpEntity<>(testIssue, headers);

        ResponseEntity<Issue> responseEntity = restTemplate.exchange(
                protocol + hostName + ":" + serverPort + "/api/issues",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Issue returnedIssue = responseEntity.getBody();
        assertTrue(returnedIssue.equalsWithoutCheckingId(testIssue));

        //delete from db and check if passes
        dbQueries.deleteFromDbAndAssertDeletionSuccessful(returnedIssue.getId());
    }

    @Test
    public void deletingIssueByIdEndpoint() {
        Issue issue = dbQueries.saveIssue();
        int id = issue.getId();
        log.info("id -> " + id);

        ResponseEntity<Void> responseEntity = restTemplate.exchange(
                protocol + hostName + ":" + serverPort + "/api/issues/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                id
        );

        //assert correct issue has been deleted
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        //assert issue was not found after deletion
        dbQueries.assertNotFoundInDb(id);
    }

    @Test
    public void notFound() {
        ResponseEntity<Issue> response = restTemplate.exchange(
                protocol + hostName + ":" + serverPort + "/api/issues1",
                HttpMethod.GET,
                null,
                Issue.class);
        log.info("status code should be 404 and it is ->" + response.getStatusCodeValue());
        assertTrue(response.getStatusCode().is4xxClientError());
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void idShouldNotBeFound() {
        int idToFind = 0;
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                protocol + hostName + ":" + serverPort + "/api/issues/{id}",
                HttpMethod.GET,
                null,
                ErrorResponse.class,
                idToFind
        );
        log.info("status code should be 404 and it is ->" + response.getStatusCodeValue());
        log.info(String.valueOf(response.getBody()));
        assertTrue(response.getStatusCode().is4xxClientError());
        assertEquals(404, response.getStatusCodeValue());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ErrorResponse errorResponse = response.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), errorResponse.getStatus());
        assertEquals("Did not find issue with given id " + idToFind, errorResponse.getMessage());
    }

    @Test
    public void updateIssue() {
        //save an issue to db
        Issue savedIssue = Optional.ofNullable(dbQueries.saveIssue()).orElseThrow(ThrowsWhenIssue.isNotPresent);
        int idToFind = savedIssue.getId();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        //set up a new Issue
        testIssue.setId(idToFind);
        HttpEntity<Issue> requestEntity = new HttpEntity<>(testIssue, headers);

        ResponseEntity<Issue> response = restTemplate.exchange(
                protocol + hostName + ":" + serverPort + "/api/issues/{id}",
                HttpMethod.PUT,
                requestEntity,
                Issue.class,
                idToFind
        );

        //find the new issue
        Issue foundIssue = Optional.ofNullable(dbQueries.findIssueById(idToFind)).orElseThrow(ThrowsWhenIssue.isNotPresent);
        assertTrue(foundIssue.equals(testIssue));

        //delete the issue
        dbQueries.deleteFromDbAndAssertDeletionSuccessful(idToFind);
    }

    @AfterEach
    public void teardown() {
        testIssue = null;
    }
}
