package com.issuetracker.issuetracker;


import com.issuetracker.dataJpa.controller.IssueRestController;
import com.issuetracker.dataJpa.entity.Issue;
import com.issuetracker.dataJpa.service.IssueService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class IssuetrackerApplicationTests {
	@InjectMocks
	IssueRestController issueRestController;

	@Mock
	IssueService issueService;

	@Test
	void contextLoads() {
		Issue issue = new Issue("blas","bla","val","val");
		List<Issue> result = issueRestController.findAll();
		for(Issue i:result){
			System.out.println(i.getAssigneeName());
		}
		Assert.isTrue(result!=null);
	}

}
