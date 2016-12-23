package be.vdab.stories.jira.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraQuery {
    public List<JiraIssue> issues;

    private JiraQuery() {

    }

    public static JiraQuery aJiraQuery() {
        return new JiraQuery();
    }

    public JiraQuery withIssues(List<JiraIssue> issues) {
        this.issues = issues;
        return this;
    }
}
