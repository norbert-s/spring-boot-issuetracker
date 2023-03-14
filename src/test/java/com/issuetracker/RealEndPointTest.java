package com.issuetracker;

import com.issuetracker.config.MyTestConfig;
import com.issuetracker.dataJpa.entity.Issue;
import com.issuetracker.dataJpa.exceptionhandling.ErrorResponse;
import com.issuetracker.database_integration.DatabaseQueries;
import com.issuetracker.issue_object_generator.IssuePOJO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import org.springframework.test.context.TestPropertySource;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=8080"})
@Import(MyTestConfig.class)
@TestPropertySource("/dev.properties")
@Tag("sanity")
public class RealEndPointTest {
    protected static final Logger LOGGER = LogManager.getLogger(RealEndPointTest.class);

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
        Optional<Issue> createdIssue = Optional.ofNullable(dbQueries.saveIssue());
        if (createdIssue.isPresent()) {
            ResponseEntity<Issue> responseEntity = restTemplate.exchange(
                    protocol + hostName + ":" + serverPort + "/api/issues/{id}",
                    HttpMethod.GET,
                    null,
                    Issue.class,
                    createdIssue.get().getId()
            );

            Optional<Issue> foundIssue = Optional.ofNullable(responseEntity.getBody());
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertTrue(foundIssue.get().equalsWithoutCheckingId(createdIssue.get()));
            dbQueries.deleteFromDbAndAssertDeletionSuccessful(createdIssue.get().getId());
        } else {
            throw new RuntimeException("issue is not present");
        }

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
                new ParameterizedTypeReference<Issue>() {
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
        LOGGER.info("id -> " + id);

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
                "http://localhost:8080/api/issues1",
                HttpMethod.GET,
                null,
                Issue.class);
        LOGGER.info("status code should be 404 and it is ->" + response.getStatusCodeValue());
        assertTrue(response.getStatusCode().is4xxClientError());
        assertTrue(response.getStatusCodeValue() == 404);
    }

    @Test
    @Tag("current")
    public void idShouldNotBeFound() {
        int idToFind = 0;
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                protocol + hostName + ":" + serverPort + "/api/issues/{id}",
                HttpMethod.GET,
                null,
                ErrorResponse.class,
                idToFind
        );
        LOGGER.info("status code should be 404 and it is ->" + response.getStatusCodeValue());
        LOGGER.info(response.getBody());
        assertTrue(response.getStatusCode().is4xxClientError());
        assertTrue(response.getStatusCodeValue() == 404);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ErrorResponse errorResponse = response.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), errorResponse.getStatus());
        assertEquals("Did not find issue with given id " + idToFind, errorResponse.getMessage());
        assertNotNull(errorResponse.getTimestamp());
    }

    @AfterEach
    public void teardown() {
        testIssue = null;
    }
}
