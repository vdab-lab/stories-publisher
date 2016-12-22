package be.vdab.stories.resource;

import be.vdab.stories.jira.domain.JiraIssue;
import be.vdab.stories.jira.JiraService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Set;

@Named
@Path("/issue")
public class IssueResource {

    @Inject
    private JiraService jiraService;

    @GET
    @Path("/sinceLastRelease")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<JiraIssue> getAllSinceLastRelease(){
        return jiraService.getJiraIssuesSinceLastRelease();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Set<JiraIssue> getAllIssues(){
        return jiraService.getAllJiraIssues();
    }

}
