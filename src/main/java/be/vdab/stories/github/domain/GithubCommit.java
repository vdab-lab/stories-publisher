package be.vdab.stories.github.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.ToStringBuilder;

import static be.vdab.stories.github.domain.GithubCommit.Commit.aCommit;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubCommit {
    public String sha;
    public Commit commit;

    private GithubCommit(){

    }

    public static GithubCommit aGithubCommit(){
        return new GithubCommit().withCommit(aCommit());
    }

    public GithubCommit withSha(String sha) {
        this.sha = sha;
        return this;
    }

    public GithubCommit withCommit(Commit commit) {
        this.commit = commit;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GithubCommit that = (GithubCommit) o;

        return sha.equals(that.sha);
    }

    @Override
    public int hashCode() {
        return sha.hashCode();
    }

    @Override
    public String toString(){
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Commit {
        public String message;

        private Commit(){

        }

        public static Commit aCommit(){
            return new Commit();
        }

        public Commit withMessage(String message) {
            this.message = message;
            return this;
        }
    }
}
