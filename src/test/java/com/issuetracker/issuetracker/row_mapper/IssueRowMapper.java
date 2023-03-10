package com.issuetracker.issuetracker.row_mapper;

import com.issuetracker.dataJpa.entity.Issue;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IssueRowMapper implements RowMapper<Issue>{

        @Override
        public Issue mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Issue(
                    rs.getInt("id"),
                    rs.getString("description"),
                    rs.getString("title"),
                    rs.getString("status"),
                    rs.getString("assignee_name")
            );
        }

}
