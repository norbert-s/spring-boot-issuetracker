package com.issuetracker.helpers.sql_queries;

import com.issuetracker.dataJpa.entity.Issue;
import com.issuetracker.helpers.issue_object_generator.IssuePOJO;
import com.issuetracker.helpers.row_mapper.IssueRowMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Component
public class DatabaseQueries {
    protected static final Logger LOGGER = LogManager.getLogger(DatabaseQueries.class);
    @Autowired
    private JdbcTemplate jdbc;

    public void deleteFromDbAndAssertDeletionSuccessful(int deletedId) {
        jdbc.execute(" delete from issue where id=" + deletedId);
        assertNotFoundInDb(deletedId);
    }

    public void assertNotFoundInDb(int id) {
        Optional<List<Issue>> returnedIssueAfterDeletion = Optional.ofNullable(jdbc.query("SELECT * from issue where id=" + id, new IssueRowMapper()));
        if(returnedIssueAfterDeletion.isPresent()){
            assertTrue(returnedIssueAfterDeletion.get().isEmpty(),"an issue has been found by above id " + id + " , when it was supposed to have been already deleted");
            LOGGER.info("issue has been correctly not found "+id);
        }
    }

    public Issue findIssueById(int id) {
        Optional<List<Issue>> foundIssue = Optional.ofNullable(jdbc.query("SELECT * from issue where id=" + id, new IssueRowMapper()));
        if(foundIssue.isPresent()){
            LOGGER.info("issue has been found "+id);
            return foundIssue.get().get(0);
        }else{
            LOGGER.info("issue has been not found in the db "+id);
        }
        return null;
    }

    public Issue saveIssue() {
        Issue issue = IssuePOJO.issueGenerator();
        String sql = "insert into issue(id,title,description,assignee_name,status) values(?,?,?,?,?)";
        int count = jdbc.update(sql, 0, issue.getTitle(), issue.getDescription(), issue.getAssigneeName(), "open");
        if (count > 0) {
            Integer generatedId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
            issue.setId(generatedId);
            return jdbc.queryForObject("SELECT * FROM issue WHERE id = ?", new Object[]{generatedId}, new IssueRowMapper());
        } else {
            return null;
        }
        //return  jdbc.queryForObject(" insert into issue(id,title,description,assignee_name,status) values(1000,"+issue.getTitle()+","+issue.getDescription()+","+issue.getAssigneeName()+","+"'open')",new IssueRowMapper());
    }
}
