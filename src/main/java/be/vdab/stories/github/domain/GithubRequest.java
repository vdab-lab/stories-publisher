package be.vdab.stories.github.domain;


public class GithubRequest {
    public String userAgent = "vdab-mentor";
    public String authorization;
    public String user;
    public String repositoryName;
    public int recordsPerPage = 100;

    private GithubRequest() {

    }

    public static GithubRequest aGithubRequest() {
        return new GithubRequest();
    }

    public GithubRequest withUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public GithubRequest withAuthorization(String authorization) {
        this.authorization = String.format("token %s", authorization);
        return this;
    }

    public GithubRequest withUser(String user) {
        this.user = user;
        return this;
    }

    public GithubRequest withRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
        return this;
    }

    public GithubRequest withRecordsPerPage(int recordsPerPage) {
        this.recordsPerPage = recordsPerPage;
        return this;
    }
}
