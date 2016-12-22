package be.vdab.stories.git.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class GitRelease {
    private String name;
    private GitCommit gitCommit;

    private GitRelease(String name, GitCommit gitCommit) {
        this.name = name;
        this.gitCommit = gitCommit;
    }

    public static GitRelease aGitRelease(String name, GitCommit gitCommit){
        return new GitRelease(name, gitCommit);
    }


    public String getName() {
        return name;
    }

    public GitCommit getGitCommit() {
        return gitCommit;
    }

    @Override
    public String toString(){
        return ToStringBuilder.reflectionToString(this);
    }
}
