package be.vdab.stories;

import be.vdab.stories.git.GitService;
import be.vdab.stories.git.endpoint.github.GithubWrapper;
import be.vdab.stories.git.endpoint.gitlab.GitlabWrapper;
import be.vdab.stories.jira.JiraService;
import be.vdab.stories.jira.endpoint.JiraWrapper;
import org.glassfish.jersey.servlet.ServletContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StoriesConfig {

    @Bean
    public ServletRegistrationBean publicJersey() {
        ServletRegistrationBean publicJersey = new ServletRegistrationBean(new ServletContainer(new JerseyStoriesConfig()));
        publicJersey.addUrlMappings("/stories/*");
        publicJersey.setName("JerseyStories");
        publicJersey.setLoadOnStartup(0);
        return publicJersey;
    }

    @Bean
    public GitService githubService(@Value("${git.repository:''}") String githubRepo,
                                    @Value("${git.latest.commit:''}") String githubLatestCommit,
                                    @Value("${git.authorization.token:''}") String authorizationToken,
                                    @Value("${git.isGithub:true}") boolean isGithub,
                                    GithubWrapper githubWrapper, GitlabWrapper gitlabWrapper) {
        if(isGithub){
            return new GitService(githubRepo, githubLatestCommit, authorizationToken, githubWrapper);
        }
        return new GitService(githubRepo, githubLatestCommit, authorizationToken, gitlabWrapper);
    }

    @Bean
    public GithubWrapper githubWrapper(@Value("${git.url:''}") String githubUrl) {
        return new GithubWrapper(githubUrl);
    }

    @Bean
    public GitlabWrapper gitlabWrapper(@Value("${git.url:''}") String githubUrl) {
        return new GitlabWrapper(githubUrl);
    }

    @Bean
    public JiraWrapper jiraWrapper(@Value("${jira.url:''}") String jiraUrl,
                                   @Value("${jira.authorization.token:''}") String authorizationToken) {
        return new JiraWrapper(jiraUrl, authorizationToken);
    }

    @Bean
    public JiraService jiraService(JiraWrapper jiraWrapper, GitService gitService) {
        return new JiraService(jiraWrapper, gitService);
    }
}
