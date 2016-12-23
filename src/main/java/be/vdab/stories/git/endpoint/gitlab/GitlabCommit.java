package be.vdab.stories.git.endpoint.gitlab;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GitlabCommit {
    public String id;
    public String message;
    public Date created_at;
}
