package com.issuetracker.database_integration;

import com.issuetracker.dataJpa.entity.Issue;
import com.issuetracker.issue_object_generator.IssuePOJO;
import com.issuetracker.row_mapper.IssueRowMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Component
public class DatabaseQueries {
    protected static final Logger LOGGER = LogManager.getLogger(DatabaseQueries.class);
    @Autowired
    private JdbcTemplate jdbc;

    public void deleteFromDatabaseById(int idToDelete) {
        jdbc.execute(" delete from issue where id=" + idToDelete);
    }

    public void deleteFromDbAndCheckIfPasses(int deletedId) {
        deleteFromDatabaseById(deletedId);
        List<Issue> returnedIssueAfterDeletion = selectAllFromDbById(deletedId);
        assertEquals(0, returnedIssueAfterDeletion.size(), "an issue has been found by above id " + deletedId + " , when it was supposed to have been already deleted");
    }

    public List<Issue> selectAllFromDbById(int id) {
        return jdbc.query("SELECT * from issue where id=" + id, new IssueRowMapper());
    }

    public void selectAllFromDbByIdAndAssertThatItIsEmpty(int id) {
        Optional<List<Issue>> result = Optional.of(jdbc.query("SELECT * from issue where id=" + id, new IssueRowMapper()));
        assertTrue(result.get().size() == 0);
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
