package com.issuetracker.assertion_helpers;

import com.issuetracker.dataJpa.entity.Issue;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AssertionHelper {
    public static void compareIssues(Issue original, Issue returned) {
        assertEquals(original.getId(), returned.getId());
    }
}
