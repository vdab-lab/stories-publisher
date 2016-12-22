package be.vdab.stories.jira;


import be.vdab.stories.github.GithubService;
import be.vdab.stories.jira.domain.JiraIssue;
import be.vdab.stories.jira.endpoint.JiraWrapper;

import javax.inject.Named;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toSet;

@Named
public class JiraService {

    private Pattern pattern;
    private JiraWrapper endpoint;
    private GithubService githubService;
    private String projectLabel;

    public JiraService(String projectLabel, JiraWrapper endpoint, GithubService githubService){
        this.projectLabel = projectLabel;
        this.endpoint = endpoint;
        this.githubService = githubService;
        this.pattern = Pattern.compile(String.format("(?s)(.*)(%s-\\d+)(.*)", projectLabel));
    }

    public Set<JiraIssue> getJiraIssuesSinceLastCommit() {
        return githubService.getCommitsSinceLastReleaseUntilLatestCommit()
                .stream()
                .map(githubCommit -> githubCommit.commit.message)
                .filter(message -> message.contains(projectLabel))
                .map(message -> {
                    Matcher matcher = pattern.matcher(message);
                    matcher.matches();
                    return matcher.group(2);
                })
                .map(issue -> endpoint.getIssue(issue))
                .collect(toSet())
        ;
    }
}
