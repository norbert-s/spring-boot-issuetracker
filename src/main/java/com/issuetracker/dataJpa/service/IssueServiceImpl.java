package com.issuetracker.dataJpa.service;

import com.issuetracker.dataJpa.dao.IssueRepository;
import com.issuetracker.dataJpa.entity.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IssueServiceImpl implements IssueService {

    private IssueRepository issueRepository;
    @Autowired
    public IssueServiceImpl(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    @Override
    public List<Issue> findAll() {
        return issueRepository.findAll();
    }

    @Override
    public Issue findById(int theId) {
        Optional<Issue> result = issueRepository.findById(theId);
        Issue theIssue = null;

        if(result.isPresent()){
            theIssue = result.get();
        }
        else{
            throw new RuntimeException("Did not find issue with given id "+theId);
        }
        return theIssue;
    }

    @Override
    public void save(Issue theIssue) {
        issueRepository.save(theIssue);
    }

    @Override
    public void deleteById(int theId) {
        issueRepository.deleteById(theId);
    }
}
