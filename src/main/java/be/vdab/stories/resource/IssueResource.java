package be.vdab.stories.resource;

import be.vdab.stories.jira.JiraService;
import be.vdab.stories.jira.domain.JiraIssue;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Named
@Path("/issue")
public class IssueResource {

    private final String FORMAT = "yyyy-MM-dd";
    private final DateFormat DATE_FORMAT = new SimpleDateFormat(FORMAT);

    @Inject
    private JiraService jiraService;

    @GET
    @Path("/sinceLastRelease")
    @Produces(MediaType.APPLICATION_JSON)
    public List<JiraIssue> getAllSinceLastRelease(){
        return jiraService.getJiraIssuesSinceLastRelease();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<JiraIssue> getAllIssues(@QueryParam("since") String dateString){
        return jiraService.getAllJiraIssues(getDateFromString(dateString));
    }

    private Date getDateFromString(String dateString){
        if(dateString == null){
            return null;
        }
        try {
            return DATE_FORMAT.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException(String.format("The since date should be of format %s", FORMAT));
        }
    }

}
