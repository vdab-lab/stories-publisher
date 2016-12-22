package be.vdab.stories.github.endpoint;

import be.vdab.stories.github.domain.GithubCommit;
import be.vdab.stories.github.domain.GithubRelease;
import be.vdab.stories.github.domain.GithubRequest;
import org.glassfish.jersey.client.proxy.WebResourceFactory;

import javax.inject.Named;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.util.List;

@Named
public class GithubWrapper {
    private GithubEndpoint endpoint;

    public GithubWrapper(String backendUrl) {
        endpoint = WebResourceFactory.newResource(GithubEndpoint.class, restClient(backendUrl));
    }

    public List<GithubCommit> getCommits(GithubRequest request, int page) {
        return endpoint.getCommits(
                request.userAgent,
                request.authorization,
                request.user,
                request.repositoryName,
                page,
                request.recordsPerPage);
    }

    public List<GithubRelease> getReleases(GithubRequest request, int page) {
        return endpoint.getReleases(
                request.userAgent,
                request.authorization,
                request.user,
                request.repositoryName,
                page,
                request.recordsPerPage);
    }

    private WebTarget restClient(String backendUrl) {
        return ClientBuilder.newClient()
                .target(backendUrl);
    }
}
