package be.vdab.stories.git.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class GitCommit {
    private String id;
    private String message;

    private GitCommit(String id) {
        this.id = id;
    }

    public static GitCommit aGitCommit(String id){
        return new GitCommit(id);
    }


    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public GitCommit withMessage(String message){
        this.message = message;
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GitCommit gitCommit = (GitCommit) o;

        return id.equals(gitCommit.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString(){
        return ToStringBuilder.reflectionToString(this);
    }
}
