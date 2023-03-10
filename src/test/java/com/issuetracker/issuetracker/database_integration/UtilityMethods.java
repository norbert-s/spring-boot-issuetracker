package com.issuetracker.issuetracker.database_integration;

import com.issuetracker.dataJpa.entity.Issue;
import com.issuetracker.dataJpa.service.IssueService;
import com.issuetracker.issuetracker.issue_object_generator.IssuePOJO;
import org.springframework.beans.factory.annotation.Autowired;

public class UtilityMethods {

    @Autowired
    IssueService issueService;
    public Issue createIssueInDb(){
        Issue generatedIssue = IssuePOJO.issueGenerator();
        return issueService.save(generatedIssue);
    }
}
