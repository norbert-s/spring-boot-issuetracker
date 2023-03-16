package com.issuetracker.dataJpa.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.issuetracker.dataJpa.entity.Issue;
import com.issuetracker.dataJpa.exceptionhandling.exceptions.IssueNotFoundException;
import com.issuetracker.dataJpa.service.IssueService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class IssueRestController {

    protected static final Logger LOGGER = LogManager.getLogger(IssueRestController.class);
    private IssueService issueService;

    @Autowired
    public IssueRestController(IssueService issueService) {
        this.issueService = issueService;
    }

    @GetMapping("/api/issues")
    public List<Issue> findAll() {
        return issueService.findAll();
    }

    @GetMapping("/api/issues/{issueId}")
    public Issue getIssue(@PathVariable int issueId) {
        return issueService.findById(issueId);
    }

    @PostMapping("/api/issues")
    public Issue addIssue(@RequestBody Issue theIssue) {
        return issueService.save(theIssue);
    }
    @PutMapping("/api/issues/{issueId}")
    public Issue updateIssueById(@PathVariable int issueId,@RequestBody Issue theIssue) {
       return issueService.updateById(issueId,theIssue);
    }
    @DeleteMapping("/api/issues/{issueId}")
    public void deleteIssue(@PathVariable int issueId) {
        issueService.deleteById(issueId);
    }

}
