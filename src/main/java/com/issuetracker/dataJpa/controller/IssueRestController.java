package com.issuetracker.dataJpa.controller;
import com.issuetracker.dataJpa.entity.Issue;

import com.issuetracker.dataJpa.service.IssueService;


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
        Issue theIssue = issueService.findById(issueId);

        if (theIssue == null) {
            throw new RuntimeException("issue id not found - " +  issueId);
        }
        return theIssue;
    }

    @PostMapping("/issues")
    public Issue addIssue(@RequestBody Issue theIssue) {
        theIssue.setId(0);
        issueService.save(theIssue);
        return theIssue;
    }
    @PutMapping("/issues")
    public Issue updateIssue(@RequestBody Issue theIssue) {
        issueService.save(theIssue);
        return theIssue;
    }
    @DeleteMapping("/issues/{issueId}")
    public Issue deleteIssue(@PathVariable int issueId) {

        Issue tempIssue = issueService.findById(issueId);

        // throw exception if null

        if (tempIssue == null) {
            throw new RuntimeException("issue id not found - " + issueId);
        }

        issueService.deleteById(issueId);

        return tempIssue;
    }
//    @DeleteMapping("/issues/{issueAssigneeName}")
//    public Issue deleteIssueByName(@PathVariable String name) {
//
//        Issue tempIssue = issueService.findByName(name);
//
//        // throw exception if null
//
//        if (tempIssue == null) {
//            throw new RuntimeException("issue id not found - " + name);
//        }
//
//        issueService.deleteByName(name);
//
//        return tempIssue;
//    }
}
