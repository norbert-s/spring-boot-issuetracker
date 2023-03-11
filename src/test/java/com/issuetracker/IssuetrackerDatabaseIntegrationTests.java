package com.issuetracker;

import com.issuetracker.dataJpa.dao.IssueDao;
import com.issuetracker.dataJpa.entity.Issue;
import com.issuetracker.dataJpa.service.IssueService;
import com.issuetracker.database_integration.DatabaseQueries;
import com.issuetracker.issue_object_generator.IssuePOJO;
import com.issuetracker.listeners.Listener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@TestPropertySource("/application-test.properties")
@ExtendWith(Listener.class)
@SpringBootTest
@Tag("db_integration_tests")
public class IssuetrackerDatabaseIntegrationTests {

    protected static final Logger LOGGER = LogManager.getLogger(IssuetrackerDatabaseIntegrationTests.class);
    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private IssueDao issueDao;

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
        assertThat(createdDbEntry.get().equalsWithoutCheckingId(testIssue));


        //deleting issue by sql
        int deletedId = createdDbEntry.get().getId();
        dbQueries.deleteFromDbAndCheckIfPasses(deletedId);
    }

    @Test
    public void testFindIssueById() {
        //saving by sql
        Optional<Issue> createdDbEntry = Optional.ofNullable(dbQueries.saveIssue());

        //testing the service here
        Optional<Issue> foundIssue = Optional.ofNullable(issueService.findById(createdDbEntry.get().getId()));
        assertThat(testIssue.equalsWithoutCheckingId(foundIssue.get()));

        //deleting issue by sql
        int deletedId = createdDbEntry.get().getId();
        dbQueries.deleteFromDbAndCheckIfPasses(deletedId);
    }

    @Test
    public void testDeleteIssue() {
        //saving by sql
        Optional<Issue> createdDbEntry = Optional.ofNullable(dbQueries.saveIssue());

        //testing the service here
        issueService.deleteById(createdDbEntry.get().getId());

        //deletion was successfull by sql
        int deletedId = createdDbEntry.get().getId();
        dbQueries.selectAllFromDbByIdAndAssertIfItIsEmpty(deletedId);
    }
}
