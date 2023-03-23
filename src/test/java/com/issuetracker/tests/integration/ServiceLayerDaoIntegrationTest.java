package com.issuetracker.tests.integration;

import com.issuetracker.dataJpa.entity.Issue;
import com.issuetracker.dataJpa.service.IssueService;
import com.issuetracker.helpers.issue_object_generator.IssuePOJO;
import com.issuetracker.helpers.sql_queries.DatabaseQueries;
import com.issuetracker.tests.exceptions.ThrowsWhenIssue;
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
        //save issue service will throw if unsuccessful
        Issue createdDbEntry = issueService.save(testIssue);
        log.info(String.valueOf(createdDbEntry));
        assertThat(createdDbEntry.equalsWithoutCheckingId(testIssue));


        //deleting issue by sql
        int deletedId = createdDbEntry.getId();
        dbQueries.deleteFromDbAndAssertDeletionSuccessful(deletedId);
    }

    @Test
    public void testFindIssueById() {
        //saving by sql
        Issue createdDbEntry = Optional.ofNullable(dbQueries.saveIssue()).orElseThrow(ThrowsWhenIssue.isNotPresent);

        //testing the service here
        //findbyid throws if not found
        Issue foundIssue = issueService.findById(createdDbEntry.getId());
        log.info(foundIssue.toString());
        assertThat(testIssue.equalsWithoutCheckingId(foundIssue));

        //deleting issue by sql
        int deletedId = createdDbEntry.getId();
        dbQueries.deleteFromDbAndAssertDeletionSuccessful(deletedId);
    }
    @Test
    public void testDeleteIssue() {
        //saving by sql
        Issue createdDbEntry = Optional.ofNullable(dbQueries.saveIssue()).orElseThrow(ThrowsWhenIssue.isNotPresent);

        //testing the service here
        issueService.deleteById(createdDbEntry.getId());

        //assert deletion was successfull by sql
        int deletedId = createdDbEntry.getId();
        dbQueries.assertNotFoundInDb(deletedId);
    }

    @Test
    public void testUpdateIssue() {
        Issue savedissue = Optional.ofNullable(dbQueries.saveIssue()).orElseThrow(ThrowsWhenIssue.isNotPresent);

        int idOfIssue = savedissue.getId();
        Issue newIssue = IssuePOJO.issueGenerator();
        newIssue.setId(idOfIssue);

        //updatebyid throws if unsuccessful
        Issue foundIssue = issueService.updateById(idOfIssue, newIssue);

        assertTrue(foundIssue.equals(newIssue));
        dbQueries.deleteFromDbAndAssertDeletionSuccessful(idOfIssue);


    }
    @AfterEach
    public void tearDown(){
        testIssue = null;
    }
}
