package com.issuetracker.dataJpa.controller;

import com.issuetracker.dataJpa.entity.Issue;
import com.issuetracker.dataJpa.service.IssueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class IssueRestController {

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
