//package com.issuetracker;
//
//import ch.qos.logback.classic.Logger;
//import ch.qos.logback.classic.spi.ILoggingEvent;
//import com.issuetracker.appender.MemoryAppender;
//import com.issuetracker.dataJpa.dao.IssueDao;
//import com.issuetracker.dataJpa.entity.Issue;
//import com.issuetracker.dataJpa.service.IssueService;
//import com.issuetracker.database_integration.DatabaseQueries;
//import com.issuetracker.issue_object_generator.IssuePOJO;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Tag;
//import org.junit.jupiter.api.Test;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.test.context.TestPropertySource;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//
//@TestPropertySource("/dev.properties")
//@SpringBootTest
//@Tag("db_integration_tests")
//@Tag("sanity")
//public class DatabaseIntegrationTest_withCheckingLogging {
//
//    //protected static final Logger LOGGER = LogManager.getLogger(DatabaseIntegrationTest.class);
//    @Autowired
//    private JdbcTemplate jdbc;
//
//    @Autowired
//    private IssueDao issueDao;
//
//    @Autowired
//    private IssueService issueService;
//
//    @Autowired
//    private DatabaseQueries dbQueries;
//
//    private Issue testIssue;
//
//    private MemoryAppender customAppender;
//    private Logger LOGGER;
//
//    @BeforeEach
//    public void setup() {
//        LOGGER = (Logger) LoggerFactory.getLogger(DatabaseIntegrationTest_withCheckingLogging.class);
//        customAppender = new MemoryAppender();
//        customAppender.start();
//        LOGGER.addAppender(customAppender);
//        testIssue = IssuePOJO.issueGenerator();
//    }
//
//    @Test
//    public void testSaveIssue() {
//        //testing saving issue service
//        Optional<Issue> createdDbEntry = Optional.ofNullable(issueService.save(testIssue));
//        LOGGER.info(String.valueOf(createdDbEntry.get()));
//        assertThat(createdDbEntry.get().equalsWithoutCheckingId(testIssue));
//
//
//        //deleting issue by sql
////        int deletedId = createdDbEntry.get().getId();
////        dbQueries.deleteFromDbAndAssertDeletionSuccessful(deletedId);
//    }
//
//    @Test
//    public void testFindIssueById() {
//        //saving by sql
//        Optional<Issue> createdDbEntry = Optional.ofNullable(dbQueries.saveIssue());
//
//        //testing the service here
//        Optional<Issue> foundIssue = Optional.ofNullable(issueService.findById(createdDbEntry.get().getId()));
//        LOGGER.info(String.valueOf(foundIssue.get()));
//        assertThat(testIssue.equalsWithoutCheckingId(foundIssue.get()));
//
//        //deleting issue by sql
//        int deletedId = createdDbEntry.get().getId();
//        dbQueries.deleteFromDbAndAssertDeletionSuccessful(deletedId);
//    }
//
//    @Test
//    public void testFindIssueByIdByDao() {
//        //saving by sql
//        Optional<Issue> createdDbEntry = Optional.ofNullable(dbQueries.saveIssue());
//
//        //testing the service here
//        Optional<Issue> foundIssue = Optional.ofNullable(issueDao.findById(createdDbEntry.get().getId()));
//        assertThat(testIssue.equalsWithoutCheckingId(foundIssue.get()));
//
//        //deleting issue by sql
//        int deletedId = createdDbEntry.get().getId();
//        dbQueries.deleteFromDbAndAssertDeletionSuccessful(deletedId);
//    }
//
//    @Test
//    public void testDeleteIssue() {
//        //saving by sql
//        Optional<Issue> createdDbEntry = Optional.ofNullable(dbQueries.saveIssue());
//        LOGGER.info(createdDbEntry.isPresent()?  createdDbEntry.get().toString() :" warning - not found");
//        //testing the service here
//        issueService.deleteById(createdDbEntry.get().getId());
//
//        //assert deletion was successfull by sql
//        int deletedId = createdDbEntry.get().getId();
//        dbQueries.assertNotFoundInDb(deletedId);
//        List<ILoggingEvent> logEvents = customAppender.getLogEvents();
//        LOGGER.debug(String.valueOf(logEvents.size()));
//        assertTrue(logEvents.stream().anyMatch(event -> event.getFormattedMessage().contains("issue has been deleted")));
//    }
//    @AfterEach
//    public void tearDown(){
//        testIssue = null;
//        LOGGER.detachAppender(customAppender);
//    }
//}
