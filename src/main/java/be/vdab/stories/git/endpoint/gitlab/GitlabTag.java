package be.vdab.stories.git.endpoint.gitlab;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GitlabTag {
    public String name;
    public GitlabCommit commit;
}
