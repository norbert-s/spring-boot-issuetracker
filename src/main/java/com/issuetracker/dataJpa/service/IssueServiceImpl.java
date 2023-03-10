package com.issuetracker.dataJpa.service;

import com.issuetracker.dataJpa.dao.IssueDao;
import com.issuetracker.dataJpa.entity.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IssueServiceImpl implements IssueService {

    private IssueDao issueDao;
    @Autowired
    public IssueServiceImpl(IssueDao issueDao) {
        this.issueDao = issueDao;
    }

    @Override
    public List<Issue> findAll() {
        return issueDao.findAll();
    }

    @Override
    public Issue findById(int theId) {
        Optional<Issue> result = issueDao.findById(theId);
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
    public Issue save(Issue theIssue) {
        return issueDao.save(theIssue);
    }

    @Override
    public Issue deleteById(int theId) {
        return issueDao.deleteById(theId);
    }
}
