package com.issuetracker.helpers.notyet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.issuetracker.dataJpa.dao.IssueDao;
import com.issuetracker.dataJpa.entity.Issue;
import com.issuetracker.dataJpa.service.IssueService;
import com.issuetracker.helpers.issue_object_generator.IssuePOJO;
import com.issuetracker.helpers.sql_queries.DatabaseQueries;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@TestPropertySource("/dev.properties")
@AutoConfigureMockMvc
@SpringBootTest
public class Mocking {
    private static MockHttpServletRequest request;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    IssueService issueService;

    @Autowired
    private JdbcTemplate jdbc;

    @MockBean
    private IssueDao issueDao;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;


    public static final MediaType APPLICATION_JSON_UTF8 = MediaType.APPLICATION_JSON;

    @Autowired
    private DatabaseQueries dbQueries;

    private Issue testIssue;

    @BeforeEach
    public void setup() {
        testIssue = IssuePOJO.issueGenerator();
    }
    @Test
    public void mockSavingIssue() throws Exception {
        testIssue.setId(9999);
        when(issueDao.save(testIssue)).thenReturn(testIssue);
        assertTrue(issueService.save(testIssue).equalsWithoutCheckingId(testIssue));
    }

    @Test
    public void mockFindingById() throws Exception {
        testIssue.setId(9999);
        when(issueDao.findById(testIssue.getId())).thenReturn(testIssue);
        assertTrue(issueService.findById(testIssue.getId()).equalsWithoutCheckingId(testIssue));
    }

    @Test
    public void createUsers() throws Exception {
       for(int i=0;i<5;i++){
           Optional<Issue> issue = Optional.ofNullable(dbQueries.saveIssue());
           assertTrue(issue.isPresent());
       }

    }

//    @Test
//    public void deleteIssues(){
//        for(int i=0;i<10;i++){
//            if(dbQueries.IsIssueInDb(i)){
//                dbQueries.deleteFromDbAndAssertDeletionSuccessful(i);
//            }
//        }
//    }

}
