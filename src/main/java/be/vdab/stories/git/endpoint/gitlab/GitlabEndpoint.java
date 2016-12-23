package be.vdab.stories.git.endpoint.gitlab;

import javax.ws.rs.*;
import java.util.List;

@Path("/projects")
public interface GitlabEndpoint {

    @GET
    @Path("/{repoName}/repository/commits")
    @Produces("application/json")
    List<GitlabCommit> getCommits(
            @HeaderParam("PRIVATE-TOKEN") String authorization,
            @PathParam("repoName") String repoName,
            @QueryParam("page") int page,
            @QueryParam("per_page") int perPage
    );

    @GET
    @Path("/{repoName}/repository/tags")
    @Produces("application/json")
    List<GitlabTag> getTags(
            @HeaderParam("PRIVATE-TOKEN") String authorization,
            @PathParam("repoName") String repoName,
            @QueryParam("per_page") int perPage
    );
}
