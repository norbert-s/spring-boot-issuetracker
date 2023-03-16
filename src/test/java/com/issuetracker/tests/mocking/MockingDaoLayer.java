package com.issuetracker.tests.mocking;

import com.issuetracker.dataJpa.dao.IssueDao;
import com.issuetracker.dataJpa.entity.Issue;
import com.issuetracker.dataJpa.service.IssueService;
import com.issuetracker.issue_object_generator.IssuePOJO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@TestPropertySource("/dev.properties")
@AutoConfigureMockMvc
@SpringBootTest
@Tag("mocking-controllers")
@Tag("sanity")
public class MockingDaoLayer {
    private static final Logger LOGGER = LogManager.getLogger(MockingDaoLayer.class);

    @Autowired
    private IssueService issueService;

    @MockBean
    private IssueDao issueDao;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private Issue testIssue;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        testIssue = IssuePOJO.issueGenerator();
    }

    @Test
    public void testFindById() {
        testIssue.setId(1);
        when(issueDao.findById(1)).thenReturn(testIssue);

        // Test the issueService.findById method
        Issue foundIssue = issueService.findById(1);
        assertTrue(foundIssue.equals(testIssue));

        verify(issueDao, times(1)).findById(1);
    }

    @Test
    public void testSavingIssue() {
        testIssue.setId(1);
        when(issueDao.save(testIssue)).thenReturn(testIssue);

        // Test the issueService.findById method
        Issue foundIssue = issueService.save(testIssue);
        assertTrue(foundIssue.equals(testIssue));

        verify(issueDao, times(1)).save(testIssue);
    }

    @Test
    public void testUpdatingIssue() {
        testIssue.setId(1);
        when(issueDao.save(testIssue)).thenReturn(testIssue);
        when(issueDao.findById(1)).thenReturn(testIssue);

        // Test the issueService.findById method
        Issue foundIssue = issueService.updateById(1, testIssue);
        assertTrue(foundIssue.equals(testIssue));

        verify(issueDao, times(1)).findById(1);
        verify(issueDao, times(1)).save(testIssue);
    }
}
