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

@Component
public class DatabaseQueries {
    protected static final Logger LOGGER = LogManager.getLogger(DatabaseQueries.class);
    @Autowired
    private JdbcTemplate jdbc;

    public void deleteFromDbAndAssertDeletionSuccessful(int deletedId) {
        jdbc.execute(" delete from issue where id=" + deletedId);
        selectAllFromDbByIdAndAssertThatItIsEmpty(deletedId);
    }

    public void selectAllFromDbByIdAndAssertThatItIsEmpty(int deletedId) {
        Optional<List<Issue>> returnedIssueAfterDeletion = Optional.ofNullable(jdbc.query("SELECT * from issue where id=" + deletedId, new IssueRowMapper()));
        if(returnedIssueAfterDeletion.isPresent()){
            assertEquals(0, returnedIssueAfterDeletion.get().size(), "an issue has been found by above id " + deletedId + " , when it was supposed to have been already deleted");
            LOGGER.info("issue has been successfully deleted -> id "+deletedId);
        }else{
            throw new RuntimeException("issue was not deleted from the db"+deletedId);
        }
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
