package com.issuetracker;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.issuetracker.dataJpa.dao.IssueDao;
import com.issuetracker.dataJpa.entity.Issue;
import com.issuetracker.dataJpa.service.IssueService;
import com.issuetracker.database_integration.DatabaseQueries;
import com.issuetracker.issue_object_generator.IssuePOJO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource("/dev.properties")
@AutoConfigureMockMvc
@SpringBootTest
@Tag("mocking-controllers")
@Tag("sanity")
//@WebMvcTest(IssueRestControllerTest.class)
public class MockingRestControllersTest {
    protected static final Logger LOGGER = LogManager.getLogger(MockingRestControllersTest.class);

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    IssueService issueService;

    @Autowired
    private JdbcTemplate jdbc;

    @MockBean
    private IssueDao issueDao;


    @Autowired
    ObjectMapper objectMapper;


    public static final MediaType APPLICATION_JSON_UTF8 = MediaType.APPLICATION_JSON;

    @Autowired
    private DatabaseQueries dbQueries;

    private Issue testIssue;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        testIssue = IssuePOJO.issueGenerator();
    }

    @Test
    public void findById() throws Exception {
        when(issueDao.findById(3))
            .thenReturn(testIssue);
        MvcResult resutl = mockMvc.perform(MockMvcRequestBuilders.get("/api/issues/{id}" ,3))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.title", is(testIssue.getTitle())))
                .andExpect(jsonPath("$.title", is(testIssue.getTitle())))
                .andExpect(jsonPath("$.description", is(testIssue.getDescription())))
                .andExpect(jsonPath("$.assigneeName", is(testIssue.getAssigneeName())))
                .andExpect(jsonPath("$.status", is(testIssue.getStatus()))).andReturn();
        LOGGER.info(resutl.getResponse().getContentAsString());
    }

    @Test
    public void findAll() throws Exception {
        int expectedSize = 5;
        List<Issue> issues = new ArrayList<>();
        IntStream.range(1,expectedSize+1).forEach(s->{
            Issue issue = IssuePOJO.issueGenerator();
            issue.setId(s);
            issues.add(issue);
        });
        when(issueDao.findAll())
                .thenReturn(issues);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/issues"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                        .andReturn();

        LOGGER.info(result.getResponse().getContentAsString());
        String responseJson = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Issue> list = objectMapper.readValue(responseJson, new TypeReference<List<Issue>>(){});
        assertThat(list, hasSize(expectedSize));
    }

    @AfterEach
    public void tearDown() {
        testIssue = null;
    }
}