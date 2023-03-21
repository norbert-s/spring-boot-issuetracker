package com.issuetracker.helpers.notyet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.issuetracker.dataJpa.entity.Issue;
import com.issuetracker.helpers.issue_object_generator.IssuePOJO;
import com.issuetracker.helpers.sql_queries.DatabaseQueries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource("/dev.properties")
@AutoConfigureMockMvc
@SpringBootTest

public class MockControllerWithRealDatabaseTest {
    private static final Logger LOGGER = LogManager.getLogger(MockControllerWithRealDatabaseTest.class);

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private DatabaseQueries dbQueries;

    private Issue testIssue;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        testIssue = IssuePOJO.issueGenerator();
    }
    @Test
    public void testCreateIssue() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String issueJson = objectMapper.writeValueAsString(testIssue);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/issues")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(issueJson))
                .andExpect(status().isOk())
                .andReturn();
        String responseContent = result.getResponse().getContentAsString();
        LOGGER.info(responseContent);
        Issue issue = objectMapper.readValue(responseContent, Issue.class);
        assertTrue(testIssue.equalsWithoutCheckingId(issue));
    }
    @Test
    public void testFindingIssueById() throws Exception {

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/issues"))
                .andExpect(status().isOk())
                .andReturn();
        String responseContent = result.getResponse().getContentAsString();
        LOGGER.info(responseContent);
        /*Issue issue = objectMapper.readValue(responseContent, Issue.class);
        assertTrue(testIssue.equalsWithoutCheckingId(issue));*/
    }

}
