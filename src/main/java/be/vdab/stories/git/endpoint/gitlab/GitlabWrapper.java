package be.vdab.stories.git.endpoint.gitlab;

import be.vdab.stories.git.domain.GitCommit;
import be.vdab.stories.git.domain.GitRelease;
import be.vdab.stories.git.domain.GitRequest;
import be.vdab.stories.git.endpoint.GitEndpoint;
import org.glassfish.jersey.client.proxy.WebResourceFactory;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.util.List;
import java.util.stream.Collectors;

import static be.vdab.stories.git.domain.GitCommit.aGitCommit;
import static be.vdab.stories.git.domain.GitRelease.aGitRelease;

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
                gitlabRequest.getAuthorization(),
                gitlabRequest.getRepositoryName(),
                page - 1,
                gitlabRequest.getRecordsPerPage())
                .stream()
                .filter(commit -> gitlabRequest.getSince() == null || gitlabRequest.getSince().before(commit.created_at))
                .map(commit -> aGitCommit(commit.id).withMessage(commit.message))
                .collect(Collectors.toList());
    }

    public List<GitRelease> getReleases(GitRequest gitlabRequest) {
        return endpoint.getTags(
                gitlabRequest.getAuthorization(),
                gitlabRequest.getRepositoryName(),
                gitlabRequest.getRecordsPerPage())
                .stream()
                .map(tag -> aGitRelease(tag.name, aGitCommit(tag.commit.id).withMessage(tag.commit.message)))
                .collect(Collectors.toList());
    }
}
