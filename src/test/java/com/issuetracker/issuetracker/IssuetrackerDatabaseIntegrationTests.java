package com.issuetracker.issuetracker;

import com.issuetracker.dataJpa.dao.IssueDao;
import com.issuetracker.dataJpa.entity.Issue;
import com.issuetracker.dataJpa.service.IssueService;
import com.issuetracker.issuetracker.assertion_helpers.AssertionHelper;
import com.issuetracker.issuetracker.database_integration.UtilityMethods;
import com.issuetracker.issuetracker.issue_object_generator.IssuePOJO;
import com.issuetracker.issuetracker.row_mapper.IssueRowMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.slf4j.SLF4JLogger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;


@TestPropertySource("/application-test.properties")
@SpringBootTest
public class IssuetrackerDatabaseIntegrationTests {

    protected static final Logger LOGGER = LogManager.getLogger();
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

    @Test
    public void testingCreatingIssue() {
        Issue generatedIssue = IssuePOJO.issueGenerator();
        Issue returnedIssue = issueService.save(generatedIssue);
        assertEquals(returnedIssue.getAssigneeName(),generatedIssue.getAssigneeName(),"assigneeNames are different");
        assertEquals(returnedIssue.getDescription(),generatedIssue.getDescription(),"descriptions are different");
        assertEquals(returnedIssue.getTitle(),generatedIssue.getTitle(),"titles are different");
        assertEquals(returnedIssue.getStatus(),generatedIssue.getStatus(),"statuses are different");
        int idToDelete = returnedIssue.getId();
        jdbc.execute(" delete from issue where id="+idToDelete);

    }
    @Test
    public void testingDeletingIssue() {
        Issue generatedIssue = IssuePOJO.issueGenerator();
        Issue returnedIssue = issueService.save(generatedIssue);
        System.out.println(returnedIssue.getId());
        assertEquals(returnedIssue.getAssigneeName(),generatedIssue.getAssigneeName());
        assertEquals(returnedIssue.getDescription(),generatedIssue.getDescription());
        assertEquals(returnedIssue.getTitle(),generatedIssue.getTitle());
        assertEquals(returnedIssue.getStatus(),generatedIssue.getStatus());

        int idToDelete = returnedIssue.getId();
        issueService.deleteById(idToDelete);
        List<Issue> returnedIssueAfterDeletion= jdbc.query("SELECT * from issue where id="+idToDelete,new IssueRowMapper());

        assertEquals(0,returnedIssueAfterDeletion.size());
    }

    @Test
    public void testingFindingIssueById() {
        Issue generatedIssue = IssuePOJO.issueGenerator();
        Issue returnedIssue = issueService.save(generatedIssue);
        returnedIssue = issueService.findById(returnedIssue.getId());
        assertEquals(returnedIssue.getAssigneeName(),generatedIssue.getAssigneeName());
        assertEquals(returnedIssue.getDescription(),generatedIssue.getDescription());
        assertEquals(returnedIssue.getTitle(),generatedIssue.getTitle());
        assertEquals(returnedIssue.getStatus(),generatedIssue.getStatus());
        LOGGER.debug(returnedIssue.getId());

        issueService.deleteById(returnedIssue.getId());
        System.out.println(returnedIssue.getId());
        List<Issue> returnedIssueAfterDeletion= jdbc.query("SELECT * from issue where id="+returnedIssue.getId(),new IssueRowMapper());
        assertEquals(0,returnedIssueAfterDeletion.size());
    }


    
    @AfterEach
    public void setupAfterTransaction() {
        //jdbc.execute(sqlDeleteIssue);

    }
}
