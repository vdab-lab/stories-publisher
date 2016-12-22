package be.vdab.stories.jira.endpoint;

import be.vdab.stories.jira.domain.JiraIssue;
import org.glassfish.jersey.client.proxy.WebResourceFactory;

import javax.inject.Named;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

@Named
public class JiraWrapper {
    private JiraEndpoint endpoint;
    private final String authorizationToken;

    public JiraWrapper(String backendUrl, String authorizationToken) {
        this.endpoint = WebResourceFactory.newResource(JiraEndpoint.class, restClient(backendUrl));
        this.authorizationToken = authorizationToken;
    }

    public JiraIssue getIssue(String issueName) {
        return endpoint.getIssue(issueName, String.format("Basic %s", authorizationToken));
    }


    private WebTarget restClient(String backendUrl) {
        return ClientBuilder.newClient()
                .target(backendUrl);
    }
}
