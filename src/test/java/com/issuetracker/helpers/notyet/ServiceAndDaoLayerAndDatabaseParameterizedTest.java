//package com.issuetracker.notyet;
//
//import com.issuetracker.tests.integration.ServiceLayerDaoIntegrationTest;
//import com.issuetracker.dataJpa.dao.IssueDao;
//import com.issuetracker.dataJpa.entity.Issue;
//import com.issuetracker.dataJpa.service.IssueService;
//import com.issuetracker.database_integration.DatabaseQueries;
//import com.issuetracker.issue_object_generator.IssuePOJO;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Tag;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.Arguments;
//import org.junit.jupiter.params.provider.MethodSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.test.context.TestPropertySource;
//
//import java.util.Optional;
//import java.util.function.Consumer;
//import java.util.function.Function;
//import java.util.stream.Stream;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//
//@TestPropertySource("/dev.properties")
//@SpringBootTest
//
//public class ServiceAndDaoLayerAndDatabaseParameterizedTest {
//    private static final Logger LOGGER = LogManager.getLogger(ServiceLayerDaoIntegrationTest.class);
//
//    @Autowired
//    private JdbcTemplate jdbc;
//
//    @Autowired
//    private static IssueService issueService;
//
//    @Autowired
//    private static IssueDao issueDao;
//
//    @Autowired
//    private DatabaseQueries dbQueries;
//
//    private Issue testIssue;
//
//
//    @BeforeEach
//    public void setup() {
//        testIssue = IssuePOJO.issueGenerator();
//    }
//
//
//    private static Stream<Arguments> deleteIssueMethods() {
//        return Stream.of(
//                Arguments.of((Consumer<Integer>) issueDao::deleteById),
//                Arguments.of((Consumer<Integer>) issueService::deleteById)
//        );
//    }
//
//    @ParameterizedTest
//    @MethodSource("deleteIssueMethods")
//    public void testDeleteIssue(Consumer<Integer> deleteMethod) {
//        // Saving by SQL
//        Optional<Issue> createdDbEntry = Optional.ofNullable(dbQueries.saveIssue());
//        LOGGER.info(createdDbEntry.isPresent() ? createdDbEntry.get().toString() : "warning - not found");
//
//        // Testing the service or DAO here
//        deleteMethod.accept(createdDbEntry.get().getId());
//
//        // Assert deletion was successful by SQL
//        int deletedId = createdDbEntry.get().getId();
//        dbQueries.assertNotFoundInDb(deletedId);
//    }
//
//
//    private static Stream<Arguments> saveIssueMethods() {
//        return Stream.of(
//                Arguments.of("Service", (Function<Issue, Issue>) issue -> issueService.save(issue)),
//                Arguments.of("DAO", (Function<Issue, Issue>) issue -> issueDao.save(issue))
//        );
//    }
//
//    @ParameterizedTest
//    @MethodSource("saveIssueMethods")
//    public void testSaveIssue(String sourceName, Function<Issue, Issue> saveMethod) {
//        // Testing saving issue service or DAO
//        Optional<Issue> createdDbEntry = Optional.ofNullable(saveMethod.apply(testIssue));
//        LOGGER.info(String.valueOf(createdDbEntry.get()));
//        assertThat(createdDbEntry.get().equalsWithoutCheckingId(testIssue));
//
//        // Deleting issue by SQL
//        int deletedId = createdDbEntry.get().getId();
//        dbQueries.deleteFromDbAndAssertDeletionSuccessful(deletedId);
//    }
//
//    private static Stream<Arguments> findIssueByIdMethods() {
//        return Stream.of(
//                Arguments.of("Service", (Function<Integer, Issue>) id -> issueService.findById(id)),
//                Arguments.of("DAO", (Function<Integer, Issue>) id -> issueDao.findById(id).get())
//        );
//    }
//
//    @ParameterizedTest
//    @MethodSource("findIssueByIdMethods")
//    public void testFindIssueById(String sourceName, Function<Integer, Issue> findByIdMethod) {
//        // Saving by SQL
//        Optional<Issue> createdDbEntry = Optional.ofNullable(dbQueries.saveIssue());
//
//        // Testing the service or DAO here
//        Optional<Issue> foundIssue = Optional.ofNullable(findByIdMethod.apply(createdDbEntry.get().getId()));
//        LOGGER.info(foundIssue.get().toString());
//        assertThat(testIssue.equalsWithoutCheckingId(foundIssue.get()));
//
//        // Deleting issue by SQL
//        int deletedId = createdDbEntry.get().getId();
//        dbQueries.deleteFromDbAndAssertDeletionSuccessful(deletedId);
//    }
//
//    @AfterEach
//    public void tearDown() {
//        testIssue = null;
//    }
//}