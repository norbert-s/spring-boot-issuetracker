//package com.issuetracker.tests.mocking;
//
//import com.issuetracker.dataJpa.dao.IssueDao;
//import com.issuetracker.dataJpa.entity.Issue;
//import com.issuetracker.dataJpa.exceptionhandling.exceptions.IssueNotFoundException;
//import com.issuetracker.dataJpa.exceptionhandling.exceptions.IssueSaveException;
//import com.issuetracker.dataJpa.service.IssueService;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Tag;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.when;
//@TestPropertySource("/dev.properties")
//@AutoConfigureMockMvc
//
//@SpringBootTest
//@Tag("mocking-controllers")
//@Tag("sanity")
//@ExtendWith(SpringExtension.class)
//@Slf4j
//public class MockingExceptionsThrownTest {
//
//
//
//        @MockBean
//        private IssueDao issueDao;
//
//        @Autowired
//        private IssueService issueService;
//
//        @Test
//        public void saveIssue_throwsException_whenDaoReturnsNull() {
//            // Given
//            Issue issueToSave = new Issue();
//            // Given
//
//            when(issueDao.save(issueToSave)).thenThrow(IssueNotFoundException.class);
//
//            // When-Then
//            assertThrows(IssueSaveException.class, () -> issueService.save(issueToSave));
//        }
//}
