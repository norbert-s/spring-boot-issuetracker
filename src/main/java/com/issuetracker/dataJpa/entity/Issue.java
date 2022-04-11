package com.issuetracker.dataJpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;


@Entity
@Table(name="issue")
public class Issue {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="title")
    String title;

    @Column(name="description")
    String description;

    @Column(name="assignee_name")
    String assigneeName;

    @Column(name="status")
    String status;


    public Issue( String title, String description, String assignee_name, String status) {
        this.title = title;
        this.description = description;
        this.assigneeName = assignee_name;
        this.status = status;
    }

    public Issue() {

    }



    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAssigneeName() {
        return assigneeName;
    }

    public String getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAssigneeName(String assignee_name) {
        this.assigneeName = assignee_name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Issue{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", assignee_name='" + assigneeName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Issue)) return false;
        Issue issue = (Issue) o;
        return getId() == issue.getId() && Objects.equals(getTitle(), issue.getTitle()) && Objects.equals(getDescription(), issue.getDescription()) && Objects.equals(getAssigneeName(), issue.getAssigneeName()) && Objects.equals(getStatus(), issue.getStatus());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getDescription(), getAssigneeName(), getStatus());
    }
}
