package com.issuetracker.dataJpa.service;

import com.issuetracker.dataJpa.dao.IssueDao;
import com.issuetracker.dataJpa.entity.Issue;
import com.issuetracker.dataJpa.exceptionhandling.exceptions.IssueDeleteException;
import com.issuetracker.dataJpa.exceptionhandling.exceptions.IssueNotFoundException;
import com.issuetracker.dataJpa.exceptionhandling.exceptions.IssueSaveException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
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
        Optional<Issue> result = Optional.ofNullable(issueDao.findById(theId));
        log.info("result "+result);
        if(result.isPresent()){
            log.info("issue has been found with given id "+theId +" -> "+result.get());
            return result.get();
        }
        else{
            throw new IssueNotFoundException( "Did not find issue with given id "+theId);
        }
    }

    @Override
    public Issue save(Issue theIssue) {
        theIssue.setId(0);
        Optional<Issue> createdIssue = Optional.of(issueDao.save(theIssue));
        if(createdIssue.isPresent()){
            log.info("issue has been saved "+theIssue);
            return createdIssue.get();
        }else{
            throw new IssueSaveException("issue could not be saved in the database " +  theIssue);
        }
    }

    @Override
    public void deleteById(int theId) {
        Optional<Issue> found = Optional.ofNullable(findById(theId));
        if(found.isPresent()){
            try{
                issueDao.deleteById(theId);
                log.info("issue has been deleted "+found.get());
            }catch (Exception e){
                throw new IssueDeleteException("Issue with given id was found but could not delete it "+found.get());
            }
        }
    }

    @Override
    public Issue updateById(int id, Issue newIssue) {
        Optional<Issue> foundIssue = Optional.ofNullable(issueDao.findById(id));
        if(foundIssue.isPresent()){
            newIssue.setId(id);
            log.info("issue has been found with id "+ id);
            log.info("old issue "+foundIssue);
            log.info("new issue "+newIssue);
            return issueDao.save(newIssue);
        }
        else{
            throw new IssueNotFoundException("issue is not in the database");
        }
    }
}
