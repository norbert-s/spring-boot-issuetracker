package com.issuetracker.issuetracker;

import com.issuetracker.dataJpa.dao.IssueDao;
import com.issuetracker.dataJpa.entity.Issue;
import com.issuetracker.dataJpa.service.IssueService;
import com.issuetracker.issuetracker.issue_object_generator.IssuePOJO;
import com.issuetracker.issuetracker.row_mapper.IssueRowMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.List;


import static org.junit.jupiter.api.Assertions.*;


@TestPropertySource("/application-test.properties")
@SpringBootTest
public class IssuetrackerDatabaseIntegrationTests {

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


    @BeforeEach
    public void setupDatabase() {


    }

    @Test
    public void testingCreatingIssue() {
        Issue generatedIssue = IssuePOJO.issueGenerator();
        Issue returnedIssue = issueService.save(generatedIssue);
        assertEquals(returnedIssue.getAssigneeName(),generatedIssue.getAssigneeName());
        assertEquals(returnedIssue.getDescription(),generatedIssue.getDescription());
        assertEquals(returnedIssue.getTitle(),generatedIssue.getTitle());
        assertEquals(returnedIssue.getStatus(),generatedIssue.getStatus());
        int idToDelete = returnedIssue.getId();
        jdbc.execute(" delete from issue where id="+idToDelete);
        //jdbc.execute(" insert into issue(id,title,description,assignee_name,status) values(2,'problem','problem with table','norebrt','open')");
    }
    @Test
    public void testingDeletingIssue() {
        Issue generatedIssue = IssuePOJO.issueGenerator();
        Issue returnedIssue = issueService.save(generatedIssue);
        assertEquals(returnedIssue.getAssigneeName(),generatedIssue.getAssigneeName());
        assertEquals(returnedIssue.getDescription(),generatedIssue.getDescription());
        assertEquals(returnedIssue.getTitle(),generatedIssue.getTitle());
        assertEquals(returnedIssue.getStatus(),generatedIssue.getStatus());

        int idToDelete = returnedIssue.getId();

        Issue deletedIssue = issueService.deleteById(idToDelete);

        //jdbc.execute("SELECT * from issue where id="+idToDelete);
        List<Issue> returnedIssueAfterDeletion= jdbc.query("SELECT * from issue where id="+idToDelete,new IssueRowMapper.EmployeeRowMapper());
        assertEquals(0,returnedIssueAfterDeletion.size());
    }
    
    @AfterEach
    public void setupAfterTransaction() {
        //jdbc.execute(sqlDeleteIssue);

    }
}
