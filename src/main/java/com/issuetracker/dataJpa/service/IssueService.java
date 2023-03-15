package com.issuetracker.dataJpa.service;

import com.issuetracker.dataJpa.entity.Issue;

import java.util.List;

public interface IssueService {
    public List<Issue> findAll();

    public Issue findById(int theId);

    public Issue save(Issue theIssue);

    public void deleteById(int theId);


}