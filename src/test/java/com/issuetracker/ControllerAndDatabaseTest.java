package com.issuetracker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.issuetracker.dataJpa.entity.Issue;
import com.issuetracker.database_integration.DatabaseQueries;
import com.issuetracker.issue_object_generator.IssuePOJO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Tag("controller-db")
@Tag("sanity")
public class ControllerAndDatabaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerAndDatabaseTest.class);

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

        // Perform the POST request
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
    //    @Test
//    public void save() throws Exception {
//        ObjectMapper objectMapper = new ObjectMapper();
//        String issueString = objectMapper.writeValueAsString(testIssue);
//        System.out.println(issueString);
//
//        MvcResult result =mockMvc.perform(MockMvcRequestBuilders.post("/api/issues")
//                .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                .content(issueString))
//                .andExpect(status().isOk())
//                .andReturn();
//        System.out.println("Response status: " + result.getResponse().getStatus());
//        System.out.println("Response content: " + result.getResponse().getContentAsString());
//        System.out.println(result.getResponse().getContentAsString());

//        when(issueService.save(any(Issue.class))).thenReturn(testIssue);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String issueString = objectMapper.writeValueAsString(testIssue);
//        System.out.println(issueString);
//
//        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/issues")
//                        .contentType(MediaType.APPLICATION_JSON_UTF8)
//                        .accept(MediaType.APPLICATION_JSON_UTF8)
//                        .content(issueString))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        System.out.println("Response status: " + result.getResponse().getStatus());
//        System.out.println("Response content: " + result.getResponse().getContentAsString());
//        System.out.println(result.getResponse().getContentAsString());
//    }
}
