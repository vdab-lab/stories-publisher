package be.vdab.stories.git.domain;


import java.util.Date;

public class GitRequest {
    private String userAgent;
    private String authorization;
    private String repositoryName;
    private int recordsPerPage;
    private Date since;

    private GitRequest() {

    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getAuthorization() {
        return authorization;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public int getRecordsPerPage() {
        return recordsPerPage;
    }

    public Date getSince() {
        return since;
    }

    public static class GitRequestBuilder{
        private int recordsPerPage = 100;
        private String repositoryName;
        private String userAgent = "stories-publisher";
        private String authorization;
        private Date since;

        private GitRequestBuilder(){

        }
        public GitRequest build(){
            GitRequest gitRequest = new GitRequest();
            gitRequest.since = since;
            gitRequest.repositoryName = repositoryName;
            gitRequest.authorization = authorization;
            gitRequest.userAgent = userAgent;
            gitRequest.recordsPerPage = recordsPerPage;
            return gitRequest;
        }

        public static GitRequestBuilder aGitRequest(){
            return new GitRequestBuilder();
        }

        public GitRequestBuilder withUserAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        public GitRequestBuilder withAuthorization(String authorization) {
            this.authorization = authorization;
            return this;
        }

        public GitRequestBuilder withRepositoryName(String repositoryName) {
            this.repositoryName = repositoryName;
            return this;
        }

        public GitRequestBuilder withRecordsPerPage(int recordsPerPage) {
            this.recordsPerPage = recordsPerPage;
            return this;
        }

        public GitRequestBuilder withSince(Date since){
            this.since = since;
            return this;
        }


    }
}
