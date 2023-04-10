package com.issuetracker.tests.exceptions;

import com.issuetracker.dataJpa.controller.ErrorController;
import com.issuetracker.helpers.issue_object_generator.IssuePOJO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource("/dev.properties")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Tag("current")
@ExtendWith(SpringExtension.class)
@Slf4j
public class ErrorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    @Test
    public void testHandleError() throws Exception {
        MvcResult resutl = mockMvc.perform(MockMvcRequestBuilders.get("/api/issues4"))
                .andExpect(status().isNotFound())
                // .andExpect(jsonPath("$.status").value(404))
                // .andExpect(jsonPath("$.message").value("Requested page not found"))
                // .andExpect(jsonPath("$.timestamp").isNumber())
                .andReturn();
            log.info(resutl.getResponse().getContentAsString());
    }
}