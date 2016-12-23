package be.vdab.stories.git.endpoint;


import be.vdab.stories.git.domain.GitCommit;
import be.vdab.stories.git.domain.GitRelease;
import be.vdab.stories.git.domain.GitRequest;

import java.util.List;

public interface GitEndpoint {

    List<GitCommit> getCommits(GitRequest gitRequest, int page);
    List<GitRelease> getReleases(GitRequest gitRequest);
}
