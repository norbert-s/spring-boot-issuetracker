package com.issuetracker.database_integration;

import com.issuetracker.dataJpa.entity.Issue;
import com.issuetracker.dataJpa.service.IssueService;
import com.issuetracker.issue_object_generator.IssuePOJO;
import com.issuetracker.row_mapper.IssueRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UtilityMethods {

    @Autowired
    IssueService issueService;

    @Autowired
    JdbcTemplate jdbc;

    public Issue createIssueInDb() {
        Issue generatedIssue = IssuePOJO.issueGenerator();
        return issueService.save(generatedIssue);
    }

    public List<Issue> deleteById(int idToDelete) {
        return jdbc.query("SELECT * from issue where id=" + idToDelete, new IssueRowMapper());
    }

    public Issue createIssue() {
        Issue issue = IssuePOJO.issueGenerator();
        String sql = "insert into issue(title,description,assignee_name,status) values(?,?,?,'open')";
        int count = jdbc.update(sql, issue.getTitle(), issue.getDescription(), issue.getAssigneeName());
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
