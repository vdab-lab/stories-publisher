package be.vdab.stories.git.domain;


public class GitRequest {
    public String userAgent = "vdab-mentor";
    public String authorization;
    public String repositoryName;
    public int recordsPerPage = 100;

    private GitRequest() {

    }

    public static GitRequest aGithubRequest() {
        return new GitRequest();
    }

    public GitRequest withUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public GitRequest withAuthorization(String authorization) {
        this.authorization = authorization;
        return this;
    }

    public GitRequest withRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
        return this;
    }

    public GitRequest withRecordsPerPage(int recordsPerPage) {
        this.recordsPerPage = recordsPerPage;
        return this;
    }
}
