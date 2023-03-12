package com.issuetracker.dataJpa.service;

import com.issuetracker.dataJpa.dao.IssueDao;
import com.issuetracker.dataJpa.entity.Issue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IssueServiceImpl implements IssueService {
    protected static final Logger LOGGER = LogManager.getLogger(IssueServiceImpl.class);
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
            throw new RuntimeException("Did not find issue with given id "+theId);
        }
    }

    @Override
    public Issue save(Issue theIssue) {
        theIssue.setId(0);
        Optional<Issue> createdIssue = Optional.ofNullable(issueDao.save(theIssue));
        if(createdIssue.isPresent()){
            return createdIssue.get();
        }else{
            throw new RuntimeException("issue could not be created in database " +  theIssue);
        }
    }

    @Override
    public void deleteById(int theId) {
        Optional<Issue> found = Optional.ofNullable(findById(theId));
        if(found.isPresent()){
            try{
                LOGGER.info("issue is being deleted "+found.get());
                issueDao.deleteById(theId);
            }catch (Exception e){
                LOGGER.info("Issue with given id was found but could not delete it "+found.get());
            }
        }else{
            LOGGER.info("Could not delete issue with given id "+theId);
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
