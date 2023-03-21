package com.issuetracker.tests.integration;

import com.issuetracker.dataJpa.entity.Issue;
import com.issuetracker.dataJpa.service.IssueService;
import com.issuetracker.helpers.issue_object_generator.IssuePOJO;
import com.issuetracker.helpers.sql_queries.DatabaseQueries;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;


@TestPropertySource("/dev.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=5007"})
@Tag("db_integration_tests")
@Tag("sanity")
@Slf4j
public class ServiceLayerDaoIntegrationTest {

    @Autowired
    private IssueService issueService;

    @Autowired
    private DatabaseQueries dbQueries;

    private Issue testIssue;


    @BeforeEach
    public void setup() {
        testIssue = IssuePOJO.issueGenerator();
    }

    @Test
    public void testSaveIssue() {
        //testing saving issue service
        Optional<Issue> createdDbEntry = Optional.ofNullable(issueService.save(testIssue));
        log.info(String.valueOf(createdDbEntry.get()));
        assertThat(createdDbEntry.get().equalsWithoutCheckingId(testIssue));


        //deleting issue by sql
        int deletedId = createdDbEntry.get().getId();
        dbQueries.deleteFromDbAndAssertDeletionSuccessful(deletedId);
    }

    @Test
    public void testFindIssueById() {
        //saving by sql
        Optional<Issue> createdDbEntry = Optional.ofNullable(dbQueries.saveIssue());

        //testing the service here
        Optional<Issue> foundIssue = Optional.ofNullable(issueService.findById(createdDbEntry.get().getId()));
        log.info(foundIssue.get().toString());
        assertThat(testIssue.equalsWithoutCheckingId(foundIssue.get()));

        //deleting issue by sql
        int deletedId = createdDbEntry.get().getId();
        dbQueries.deleteFromDbAndAssertDeletionSuccessful(deletedId);
    }
    @Test
    public void testDeleteIssue() {
        //saving by sql
        Optional<Issue> createdDbEntry = Optional.ofNullable(dbQueries.saveIssue());
        log.info(createdDbEntry.isPresent()?  createdDbEntry.get().toString() :" warning - not found");
        //testing the service here
        issueService.deleteById(createdDbEntry.get().getId());

        //assert deletion was successfull by sql
        int deletedId = createdDbEntry.get().getId();
        dbQueries.assertNotFoundInDb(deletedId);
    }

    @Test
    public void testUpdateIssue() {
        Optional<Issue> savedissue = Optional.ofNullable(dbQueries.saveIssue());
        if(savedissue.isPresent()){
            Issue issue = savedissue.get();
            int idOfIssue = issue.getId();
            Issue newIssue = IssuePOJO.issueGenerator();
            newIssue.setId(idOfIssue);
            Optional<Issue> foundIssue = Optional.ofNullable(issueService.updateById(idOfIssue, newIssue));
            if(foundIssue.isPresent()){
                assertTrue(foundIssue.get().equals(newIssue));
                dbQueries.deleteFromDbAndAssertDeletionSuccessful(idOfIssue);
            }
        }
    }
    @AfterEach
    public void tearDown(){
        testIssue = null;
    }
}
