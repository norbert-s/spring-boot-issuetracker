package com.issuetracker.tests.integration;

import com.issuetracker.dataJpa.dao.IssueDao;
import com.issuetracker.dataJpa.entity.Issue;
import com.issuetracker.sql_queries.DatabaseQueries;
import com.issuetracker.issue_object_generator.IssuePOJO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
@TestPropertySource("/dev.properties")
@SpringBootTest
@Tag("db_integration_tests")
@Tag("sanity")
public class DaoLayerDatabaseIntegrationTest {
    private static final Logger LOGGER = LogManager.getLogger(DaoLayerDatabaseIntegrationTest.class);

    @Autowired
    private JdbcTemplate jdbc;

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
        LOGGER.info(String.valueOf(createdDbEntry.get()));
        assertThat(createdDbEntry.get().equalsWithoutCheckingId(testIssue));


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
        assertThat(testIssue.equalsWithoutCheckingId(foundIssue.get()));

        //deleting issue by sql
        int deletedId = createdDbEntry.get().getId();
        dbQueries.deleteFromDbAndAssertDeletionSuccessful(deletedId);
    }

    @Test
    public void testDeleteIssueByDao() {
        //saving by sql
        Optional<Issue> createdDbEntry = Optional.ofNullable(dbQueries.saveIssue());
        LOGGER.info(createdDbEntry.isPresent()?  createdDbEntry.get().toString() :" warning - not found");
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