package com.issuetracker.dataJpa.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.Objects;


@Entity
@Table(name = "issue")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Slf4j
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title")
    String title;

    @Column(name = "description")
    String description;

    @Column(name = "assignee_name")
    @JsonProperty("assignee_name")
    String assigneeName;

    @Column(name = "status")
    String status;


    public Issue(String title, String description, String assignee_name, String status) {
        this.title = title;
        this.description = description;
        this.assigneeName = assignee_name;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Issue)) return false;
        Issue issue = (Issue) o;
        return getId() == issue.getId() && Objects.equals(getTitle(), issue.getTitle()) && Objects.equals(getDescription(), issue.getDescription()) && Objects.equals(getAssigneeName(), issue.getAssigneeName()) && Objects.equals(getStatus(), issue.getStatus());
    }

    public boolean equalsWithoutCheckingId(Object o) {
        if (this == o) return true;
        if (!(o instanceof Issue)) return false;
        Issue issue = (Issue) o;
        log.info("first object is "+this);
        log.info("second object is "+issue);
        return Objects.equals(getTitle(), issue.getTitle()) && Objects.equals(getDescription(), issue.getDescription()) && Objects.equals(getAssigneeName(), issue.getAssigneeName()) && Objects.equals(getStatus(), issue.getStatus());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getDescription(), getAssigneeName(), getStatus());
    }


}
