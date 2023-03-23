package com.issuetracker.tests.integration;

import com.issuetracker.dataJpa.dao.IssueDao;
import com.issuetracker.dataJpa.entity.Issue;
import com.issuetracker.helpers.MessagesOnFailingAssertions;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestPropertySource("/dev.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port= "})
@Tag("db_integration_tests")
@Tag("sanity")
@Slf4j
public class DaoLayerDatabaseIntegrationTest {
    //private static final Logger LOGGER = LogManager.getLogger(DaoLayerDatabaseIntegrationTest.class);

    @Autowired
    private IssueDao issueDao;

    @Autowired
    private DatabaseQueries dbQueries;

    private Issue testIssue;


    @BeforeEach
    public void setup() {
        testIssue = IssuePOJO.issueGenerator();
    }

    @Test
    public void testSaveIssueByDao() {
        //testing saving issue service
        Issue createdDbEntry = Optional.ofNullable(issueDao.save(testIssue)).orElseThrow(ThrowsWhenIssue.isNotPresent);
        log.info(String.valueOf(createdDbEntry));
        assertTrue(createdDbEntry.equalsWithoutCheckingId(testIssue), MessagesOnFailingAssertions.getTwoObjectsDoNotEqual());


        //deleting issue by sql
        int deletedId = createdDbEntry.getId();
        dbQueries.deleteFromDbAndAssertDeletionSuccessful(deletedId);
    }

    @Test
    public void testFindIssueByIdByDao() {
        //saving by sql
        Issue createdDbEntry = Optional.ofNullable(dbQueries.saveIssue()).orElseThrow(ThrowsWhenIssue.isNotPresent);

        //testing dao layer here
        Issue foundIssue = Optional.ofNullable(issueDao.findById(createdDbEntry.getId())).orElseThrow(ThrowsWhenIssue.isNotPresent);
        assertTrue(createdDbEntry.equalsWithoutCheckingId(foundIssue),MessagesOnFailingAssertions.getTwoObjectsDoNotEqual());

        //deleting issue by sql
        int deletedId = createdDbEntry.getId();
        dbQueries.deleteFromDbAndAssertDeletionSuccessful(deletedId);
    }

    @Test
    public void testDeleteIssueByDao() {
        //saving by sql
        Issue createdDbEntry = Optional.ofNullable(dbQueries.saveIssue()).orElseThrow(ThrowsWhenIssue.isNotPresent);

        //testing the service here
        issueDao.deleteById(createdDbEntry.getId());

        //assert deletion was successfull by sql
        int deletedId = createdDbEntry.getId();
        dbQueries.assertNotFoundInDb(deletedId);
    }
    @AfterEach
    public void tearDown(){
        testIssue = null;
    }
}
