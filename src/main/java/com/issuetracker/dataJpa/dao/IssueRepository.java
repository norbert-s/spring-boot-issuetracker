package com.issuetracker.dataJpa.dao;

import com.issuetracker.dataJpa.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<Issue, Integer> {
}
