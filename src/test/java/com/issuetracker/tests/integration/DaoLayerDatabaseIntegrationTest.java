package com.issuetracker.tests.integration;

import com.issuetracker.dataJpa.dao.IssueDao;
import com.issuetracker.dataJpa.entity.Issue;
import com.issuetracker.helpers.MessagesOnFailingAssertions;
import com.issuetracker.helpers.issue_object_generator.IssuePOJO;
import com.issuetracker.helpers.sql_queries.DatabaseQueries;
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
        Optional<Issue> createdDbEntry = Optional.ofNullable(issueDao.save(testIssue));
        log.info(String.valueOf(createdDbEntry.get()));
        assertTrue(createdDbEntry.get().equalsWithoutCheckingId(testIssue), MessagesOnFailingAssertions.getTwoObjectsDoNotEqual());


        //deleting issue by sql
        int deletedId = createdDbEntry.get().getId();
        dbQueries.deleteFromDbAndAssertDeletionSuccessful(deletedId);
    }

    @Test
    public void testFindIssueByIdByDao() {
        //saving by sql
        Optional<Issue> createdDbEntry = Optional.ofNullable(dbQueries.saveIssue());

        //testing the service here
        Optional<Issue> foundIssue = Optional.ofNullable(issueDao.findById(createdDbEntry.get().getId()));
        assertTrue(createdDbEntry.get().equalsWithoutCheckingId(foundIssue.get()),MessagesOnFailingAssertions.getTwoObjectsDoNotEqual());

        //deleting issue by sql
        int deletedId = createdDbEntry.get().getId();
        dbQueries.deleteFromDbAndAssertDeletionSuccessful(deletedId);
    }

    @Test
    public void testDeleteIssueByDao() {
        //saving by sql
        Optional<Issue> createdDbEntry = Optional.ofNullable(dbQueries.saveIssue());
        log.info(createdDbEntry.isPresent()?  createdDbEntry.get().toString() :" warning - not found");
        //testing the service here
        issueDao.deleteById(createdDbEntry.get().getId());

        //assert deletion was successfull by sql
        int deletedId = createdDbEntry.get().getId();
        dbQueries.assertNotFoundInDb(deletedId);
    }
    @AfterEach
    public void tearDown(){
        testIssue = null;
    }
}
