package com.issuetracker;

import com.issuetracker.dataJpa.dao.IssueDao;
import com.issuetracker.dataJpa.entity.Issue;
import com.issuetracker.dataJpa.service.IssueService;
import com.issuetracker.database_integration.UtilityMethods;
import com.issuetracker.issue_object_generator.IssuePOJO;
import com.issuetracker.listeners.Listener;
import com.issuetracker.row_mapper.IssueRowMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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


    @Value("${sql.script.create.issue}")
    private String sqlCreateIssue;

    @Value("${sql.script.delete.issue}")
    private String sqlDeleteIssue;

    @Autowired
    UtilityMethods utilityMethods;

    @BeforeEach
    public void setupDatabase() {


    }

    @Tag("db_integration_tests")
    @Test
    public void testingCreatingIssue() {
        Issue generatedIssue = IssuePOJO.issueGenerator();
        Issue returnedIssue = issueService.save(generatedIssue);
        assertEquals(returnedIssue.getAssigneeName(), generatedIssue.getAssigneeName(), "assigneeNames are different");
        assertEquals(returnedIssue.getDescription(), generatedIssue.getDescription(), "descriptions are different");
        assertEquals(returnedIssue.getTitle(), generatedIssue.getTitle(), "titles are different");
        assertEquals(returnedIssue.getStatus(), generatedIssue.getStatus(), "statuses are different");
        int idToDelete = returnedIssue.getId();
        jdbc.execute(" delete from issue where id=" + idToDelete);
    }

    @Tag("db_integration_tests")
    @Test
    public void testingDeletingIssue() {
        Issue generatedIssue = IssuePOJO.issueGenerator();
        Issue returnedIssue = issueService.save(generatedIssue);
        LOGGER.info(returnedIssue.getId());
        assertEquals(returnedIssue.getAssigneeName(), generatedIssue.getAssigneeName(), "assigneeNames are different");
        assertEquals(returnedIssue.getDescription(), generatedIssue.getDescription(), "descriptions are different");
        assertEquals(returnedIssue.getTitle(), generatedIssue.getTitle(), "titles are different");
        assertEquals(returnedIssue.getStatus(), generatedIssue.getStatus(), "statuses are different");

        int idToDelete = returnedIssue.getId();
        issueService.deleteById(idToDelete);
        List<Issue> returnedIssueAfterDeletion = jdbc.query("SELECT * from issue where id=" + idToDelete, new IssueRowMapper());

        assertEquals(0, returnedIssueAfterDeletion.size());
    }

    @Tag("db_integration_tests")
    @Test
    public void testingFindingIssueById() {
        Issue generatedIssue = IssuePOJO.issueGenerator();
        Issue returnedIssue = issueService.save(generatedIssue);
        returnedIssue = issueService.findById(returnedIssue.getId());
        assertEquals(returnedIssue.getAssigneeName(), generatedIssue.getAssigneeName(), "assigneeNames are different");
        assertEquals(returnedIssue.getDescription(), generatedIssue.getDescription(), "descriptions are different");
        assertEquals(returnedIssue.getTitle(), generatedIssue.getTitle(), "titles are different");
        assertEquals(returnedIssue.getStatus(), generatedIssue.getStatus(), "statuses are different");
        LOGGER.info(returnedIssue.getId());

        issueService.deleteById(returnedIssue.getId());
        List<Issue> returnedIssueAfterDeletion = jdbc.query("SELECT * from issue where id=" + returnedIssue.getId(), new IssueRowMapper());
        assertEquals(0, returnedIssueAfterDeletion.size());
    }

    @AfterEach
    public void setupAfterTransaction() {
        //jdbc.execute(sqlDeleteIssue);

    }
}
