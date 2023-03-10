package com.issuetracker.issue_object_generator;

import com.github.javafaker.Faker;
import com.issuetracker.dataJpa.entity.Issue;

public class IssuePOJO {
    static Faker faker = new Faker();

    public static Issue issueGenerator() {
        int id = faker.number().numberBetween(1000, 2000);
        String description = faker.lorem().sentences(5).get(0);
        String title = faker.lorem().sentence();
        String assigneeName = faker.name().fullName();
        return new Issue(id, title, description, assigneeName, "open");
    }
}
