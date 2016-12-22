package be.vdab.stories.git.endpoint.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubRelease {
    public String name;
    public Commit commit;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Commit {
        public String sha;
    }
}
