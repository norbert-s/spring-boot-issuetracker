package com.issuetracker.tests.exceptions;

import java.util.function.Supplier;

public class ThrowsWhenIssue {
    public static Supplier<RuntimeException> isNotPresent = () -> {
        throw new RuntimeException("issue is not present");
    };

    public static Supplier<RuntimeException> listOfBooksNotPresent = () -> {
        throw new RuntimeException("list of issues not present");
    };
}

