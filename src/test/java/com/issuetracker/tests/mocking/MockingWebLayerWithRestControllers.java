package com.issuetracker.tests.mocking;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.issuetracker.dataJpa.entity.Issue;
import com.issuetracker.dataJpa.service.IssueService;
import com.issuetracker.issue_object_generator.IssuePOJO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource("/dev.properties")
@AutoConfigureMockMvc

@SpringBootTest
@Tag("mocking-controllers")
@Tag("sanity")
@ExtendWith(SpringExtension.class)
public class MockingWebLayerWithRestControllers {
    private static final Logger LOGGER = LogManager.getLogger(MockingWebLayerWithRestControllers.class);

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    IssueService issueService;

    @Autowired
    ObjectMapper objectMapper;

    private static final MediaType APPLICATION_JSON_UTF8 = MediaType.APPLICATION_JSON;

    private Issue testIssue;



    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        testIssue = IssuePOJO.issueGenerator();
    }

    @Test
    public void findById() throws Exception {
        when(issueService.findById(3))
            .thenReturn(testIssue);
        MvcResult resutl = mockMvc.perform(MockMvcRequestBuilders.get("/api/issues/{id}" ,3))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.title", is(testIssue.getTitle())))
                .andExpect(jsonPath("$.description", is(testIssue.getDescription())))
                .andExpect(jsonPath("$.assignee_name", is(testIssue.getAssigneeName())))
                .andExpect(jsonPath("$.status", is(testIssue.getStatus()))).andReturn();
        LOGGER.info(resutl.getResponse().getContentAsString());
        verify(issueService, times(1)).findById(3);
    }
//
    @Test
    public void findAllIssues() throws Exception {
        int expectedSize = 5;
        List<Issue> issues = new ArrayList<>();
        IntStream.range(1,expectedSize+1).forEach(s->{
            Issue issue = IssuePOJO.issueGenerator();
            issue.setId(s);
            issues.add(issue);
        });
        when(issueService.findAll())
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
        verify(issueService, times(1)).findAll();
    }

    @Test
    public void saveIssue() throws Exception {
        testIssue.setId(1);
        when(issueService.save(testIssue))
                .thenReturn(testIssue);

        String payload = objectMapper.writeValueAsString(testIssue);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/issues")
                        .content(payload)
                        .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn();

        LOGGER.info(result.getResponse().getContentAsString());
        String responseJson = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        Issue returnedIssue = objectMapper.readValue(responseJson, Issue.class);
        testIssue.equals(returnedIssue);
        verify(issueService, times(1)).save(testIssue);
    }

    @Test
    public void testDeleteIssue() throws Exception {
        int testIssueId = testIssue.getId();
        doNothing().when(issueService).deleteById(testIssueId);

        mockMvc.perform(delete("/api/issues/{id}", testIssueId))
                .andExpect(status().isOk());

        verify(issueService, times(1)).deleteById(testIssueId);
    }

    @Test
    public void testUpdateIssue() throws Exception {
        testIssue.setId(1);
        when(issueService.updateById(testIssue.getId(),testIssue))
                .thenReturn(testIssue);

        String payload = objectMapper.writeValueAsString(testIssue);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/issues/{issueId}",1)
                        .content(payload)
                        .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn();

        LOGGER.info(result.getResponse().getContentAsString());
        String responseJson = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        Issue returnedIssue = objectMapper.readValue(responseJson, Issue.class);
        assertTrue(testIssue.equals(returnedIssue));
        verify(issueService, times(1)).updateById(testIssue.getId(), testIssue);
    }

    @AfterEach
    public void tearDown() {
        testIssue = null;
    }
}