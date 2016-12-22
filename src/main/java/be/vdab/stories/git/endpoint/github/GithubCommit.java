package be.vdab.stories.git.endpoint.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubCommit {
    public String sha;
    public Commit commit;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Commit {
        public String message;
    }
}
