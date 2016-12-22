package be.vdab.stories.jira.endpoint;

import be.vdab.stories.jira.domain.JiraIssue;
import org.glassfish.jersey.client.proxy.WebResourceFactory;

import javax.inject.Named;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

@Named
public class JiraWrapper {
    private JiraEndpoint endpoint;

    public JiraWrapper(String backendUrl) {
        endpoint = WebResourceFactory.newResource(JiraEndpoint.class, restClient(backendUrl));
    }

    public JiraIssue getIssue(String issueName) {
        return endpoint.getIssue(issueName, "Basic VFZFUkNSVVk6VGlWZTI2MDIyMDE1");
    }


    private WebTarget restClient(String backendUrl) {
        return ClientBuilder.newClient()
                .target(backendUrl);
    }
}
