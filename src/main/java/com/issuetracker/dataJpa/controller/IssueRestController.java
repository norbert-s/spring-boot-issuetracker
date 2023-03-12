package com.issuetracker.dataJpa.controller;
import com.issuetracker.dataJpa.entity.Issue;

import com.issuetracker.dataJpa.service.IssueService;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class IssueRestController {

    protected static final Logger LOGGER = LogManager.getLogger(IssueRestController.class);
    private IssueService issueService;

    @Autowired
    public IssueRestController(IssueService issueService) {
        this.issueService = issueService;
    }

    @GetMapping("/issues")
    public List<Issue> findAll() {
        return issueService.findAll();
    }

    @GetMapping("/issues/{issueId}")
    public Issue getIssue(@PathVariable int issueId) {
        return issueService.findById(issueId);
    }

    @PostMapping("/issues")
    public Issue addIssue(@RequestBody Issue theIssue) {
        return issueService.save(theIssue);
    }
    @PutMapping("/issues/{issueId}")
    public Issue updateIssueById(@RequestBody Issue theIssue) {
        return issueService.save(theIssue);
    }
    @DeleteMapping("/issues/{issueId}")
    public void deleteIssue(@PathVariable int issueId) {
        issueService.deleteById(issueId);
    }
}
