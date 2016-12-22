package be.vdab.stories.git.endpoint.gitlab;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GitlabCommit {
    public String id;
    public String message;
}
