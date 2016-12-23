package be.vdab.stories.git.endpoint.github;

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

public class GithubWrapper implements GitEndpoint {
    private GithubEndpoint endpoint;

    public GithubWrapper(String backendUrl) {
        endpoint = WebResourceFactory.newResource(GithubEndpoint.class, restClient(backendUrl));
    }

    public List<GitCommit> getCommits(GitRequest request, int page) {
        String[] split = request.getRepositoryName().split("/");
        return endpoint.getCommits(
                request.getUserAgent(),
                String.format("token %s", request.getAuthorization()),
                split[0],
                split[1],
                page,
                request.getRecordsPerPage(),
                request.getSince())
                .stream()
                .map(commit -> aGitCommit(commit.sha).withMessage(commit.commit.message))
                .collect(Collectors.toList());
    }

    public List<GitRelease> getReleases(GitRequest request) {
        String[] split = request.getRepositoryName().split("/");
        return endpoint.getReleases(
                request.getUserAgent(),
                String.format("token %s", request.getAuthorization()),
                split[0],
                split[1],
                request.getRecordsPerPage(),
                request.getSince())
                .stream()
                .map(release -> aGitRelease(release.name, aGitCommit(release.commit.sha)))
                .collect(Collectors.toList());
    }

    private WebTarget restClient(String backendUrl) {
        return ClientBuilder.newClient()
                .target(backendUrl);
    }
}
