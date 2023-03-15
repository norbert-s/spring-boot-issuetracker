package com.issuetracker.dataJpa.service;

import com.issuetracker.dataJpa.dao.IssueDao;
import com.issuetracker.dataJpa.entity.Issue;
import com.issuetracker.dataJpa.exceptionhandling.exceptions.IssueDeleteException;
import com.issuetracker.dataJpa.exceptionhandling.exceptions.IssueNotFoundException;
import com.issuetracker.dataJpa.exceptionhandling.exceptions.IssueSaveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IssueServiceImpl implements IssueService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IssueServiceImpl.class);

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
        LOGGER.info("result "+result);
        if(result.isPresent()){
            LOGGER.info("issue has been found with given id "+theId +" -> "+result.get());
            return result.get();
        }
        else{
            throw new IssueNotFoundException( "Did not find issue with given id "+theId);
        }
    }

    @Override
    public Issue save(Issue theIssue) {

        theIssue.setId(0);
        Issue createdIssue = issueDao.save(theIssue);
        if(createdIssue!=null){
            LOGGER.info("issue created "+theIssue);
            return createdIssue;
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
                LOGGER.info("issue has been deleted "+found.get());
            }catch (Exception e){
                throw new IssueDeleteException("Issue with given id was found but could not delete it "+found.get());
            }
        }
    }

//    @Override
//    public Issue updateById(int id, Issue newIssue) {
//        Optional<Issue> foundIssue = Optional.ofNullable(findById(id));
//        if(foundIssue.isPresent()){
//            newIssue.setId(foundIssue.get().getId());
//            return issueDao.updateById(id,newIssue);
//        }else{
//            throw new RuntimeException("issue was not in the database");
//        }
//    }
}
