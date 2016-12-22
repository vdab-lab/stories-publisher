package be.vdab.stories.git.endpoint.gitlab;

import be.vdab.stories.git.domain.GitCommit;
import be.vdab.stories.git.domain.GitRelease;
import be.vdab.stories.git.domain.GitRequest;
import be.vdab.stories.git.endpoint.GitEndpoint;
import org.glassfish.jersey.client.proxy.WebResourceFactory;

import javax.inject.Named;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.util.List;
import java.util.stream.Collectors;

import static be.vdab.stories.git.domain.GitCommit.aGitCommit;
import static be.vdab.stories.git.domain.GitRelease.aGitRelease;

@Named
public class GitlabWrapper implements GitEndpoint{
    private GitlabEndpoint endpoint;

    public GitlabWrapper(String backendUrl) {
        endpoint = WebResourceFactory.newResource(GitlabEndpoint.class, restClient(backendUrl));
    }

    private WebTarget restClient(String backendUrl) {
        return ClientBuilder.newClient()
                .target(backendUrl);
    }

    public List<GitCommit> getCommits(GitRequest gitlabRequest, int page) {
        return endpoint.getCommits(
                gitlabRequest.authorization,
                gitlabRequest.repositoryName,
                page,
                gitlabRequest.recordsPerPage)
                .stream()
                .map(commit -> aGitCommit(commit.id).withMessage(commit.message))
                .collect(Collectors.toList());
    }

    public List<GitRelease> getReleases(GitRequest gitlabRequest, int page) {
        return endpoint.getTags(
                gitlabRequest.authorization,
                gitlabRequest.repositoryName,
                page,
                gitlabRequest.recordsPerPage)
                .stream()
                .map(tag -> aGitRelease(tag.name, aGitCommit(tag.commit.id).withMessage(tag.commit.message)))
                .collect(Collectors.toList());
    }
}
