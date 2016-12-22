package be.vdab.stories.github.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import static be.vdab.stories.github.domain.GithubRelease.Commit.aCommit;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubRelease {
    public String name;
    public Commit commit;

    private GithubRelease(){

    }

    public static GithubRelease aGithubRelease(){
        return new GithubRelease().withCommit(aCommit());
    }

    public GithubRelease withName(String name) {
        this.name = name;
        return this;
    }

    public GithubRelease withCommit(Commit commit) {
        this.commit = commit;
        return this;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Commit{
        public String sha;

        private Commit(){

        }

        public static Commit aCommit(){
            return new Commit();
        }

        public Commit withSha(String sha) {
            this.sha = sha;
            return this;
        }
    }
}
