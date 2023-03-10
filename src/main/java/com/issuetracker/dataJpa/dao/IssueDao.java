package com.issuetracker.dataJpa.dao;

import com.issuetracker.dataJpa.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueDao extends JpaRepository<Issue, Integer> {
    public Issue deleteById(int id);
}
