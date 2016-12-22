package be.vdab.stories;

import be.vdab.stories.github.GithubService;
import be.vdab.stories.github.endpoint.GithubWrapper;
import be.vdab.stories.jira.JiraService;
import be.vdab.stories.jira.endpoint.JiraWrapper;
import org.glassfish.jersey.servlet.ServletContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
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
    public GithubService githubService(@Value("${github.user:''}") String githubUser,
                                       @Value("${github.repo:''}") String githubRepo,
                                       @Value("${github.latest.commit:''}") String githubLatestCommit,
                                       @Value("${github.authorization.token:''}") String authorizationToken,
                                       GithubWrapper githubWrapper) {
        return new GithubService(githubUser, githubRepo, githubLatestCommit, authorizationToken, githubWrapper);
    }

    @Bean
    public GithubWrapper githubWrapper(@Value("${github.url:''}") String githubUrl) {
        return new GithubWrapper(githubUrl);
    }

    @Bean
    public JiraWrapper jiraWrapper(@Value("${jira.url:''}") String jiraUrl,
                                   @Value("${jira.authorization.token}") String authorizationToken) {
        return new JiraWrapper(jiraUrl, authorizationToken);
    }

    @Bean
    public JiraService jiraService(
            @Value("${jira.project.label:''}") String projectLabel,
            JiraWrapper jiraWrapper,
            GithubService githubService) {
        return new JiraService(projectLabel, jiraWrapper, githubService);
    }
}
