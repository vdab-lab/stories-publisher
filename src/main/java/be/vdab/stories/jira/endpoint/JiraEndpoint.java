package be.vdab.stories.jira.endpoint;

import be.vdab.stories.jira.domain.JiraIssue;
import be.vdab.stories.jira.domain.JiraQuery;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("rest/api/2/")
public interface JiraEndpoint {

    @GET
    @Path("issue/{issueName}")
    @Produces(MediaType.APPLICATION_JSON)
    JiraIssue getIssue(@PathParam("issueName") String issueName,
                       @HeaderParam("authorization") String authorization);

    @GET
    @Path("search")
    @Produces(MediaType.APPLICATION_JSON)
    JiraQuery search(@QueryParam("jql") String query,
                     @HeaderParam("authorization")String format);
}
