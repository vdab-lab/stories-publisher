package be.vdab.stories.jira.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import static com.google.common.collect.Lists.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraIssue {
    public String key;
    public String self;
    public Fields fields;

    private JiraIssue(){

    }

    public static JiraIssue aJiraIssue(String issueName){
        return new JiraIssue().withIssueName(issueName);
    }

    public JiraIssue withIssueName(String issueName){
        this.key = issueName;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JiraIssue jiraIssue = (JiraIssue) o;

        return key.equals(jiraIssue.key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    public String toString(){
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Fields{
        public String summary;
        public Assignee assignee;
        public Status status;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Assignee{
            public String name;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Status{
            public String description;
        }
    }
}
