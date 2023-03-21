package com.issuetracker.helpers.issue_object_generator;

import com.github.javafaker.Faker;
import com.issuetracker.dataJpa.entity.Issue;

public class IssuePOJO {
    static Faker faker = new Faker();

    synchronized public static Issue issueGenerator() {
        int id = 0;
        String description = faker.lorem().sentences(5).get(0);
        String title = faker.lorem().sentence();
        String assigneeName = faker.name().fullName();
        return new Issue(id, title, description, assigneeName, "open");
    }
}
