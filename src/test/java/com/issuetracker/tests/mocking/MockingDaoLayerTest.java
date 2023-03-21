package com.issuetracker.tests.mocking;

import com.issuetracker.dataJpa.dao.IssueDao;
import com.issuetracker.dataJpa.entity.Issue;
import com.issuetracker.dataJpa.service.IssueService;
import com.issuetracker.helpers.issue_object_generator.IssuePOJO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@TestPropertySource("/dev.properties")
@SpringBootTest
@Tag("mocking-controllers")
@Tag("sanity")
@Slf4j
public class MockingDaoLayerTest {

    @Autowired
    private IssueService issueService;

    @MockBean
    private IssueDao issueDao;

    private Issue testIssue;

    @BeforeEach
    public void setup() {
        testIssue = IssuePOJO.issueGenerator();
    }

    @Test
    public void testFindById() {
        testIssue.setId(1);
        when(issueDao.findById(1)).thenReturn(testIssue);

        // Test the issueService.findById method
        Issue foundIssue = issueService.findById(1);
        assertEquals(foundIssue, testIssue);

        verify(issueDao, times(1)).findById(1);
    }

    @Test
    public void testSavingIssue() {
        testIssue.setId(1);
        when(issueDao.save(testIssue)).thenReturn(testIssue);

        // Test the issueService.findById method
        Issue foundIssue = issueService.save(testIssue);
        assertEquals(foundIssue, testIssue);

        verify(issueDao, times(1)).save(testIssue);
    }

    @Test
    public void testUpdatingIssue() {
        testIssue.setId(1);
        when(issueDao.save(testIssue)).thenReturn(testIssue);
        when(issueDao.findById(1)).thenReturn(testIssue);

        // Test the issueService.findById method
        Issue foundIssue = issueService.updateById(1, testIssue);
        assertEquals(foundIssue, testIssue);

        verify(issueDao, times(1)).findById(1);
        verify(issueDao, times(1)).save(testIssue);
    }
}
