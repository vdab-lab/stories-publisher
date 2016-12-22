package be.vdab.stories.jira;


import be.vdab.stories.git.GitService;
import be.vdab.stories.git.domain.GitCommit;
import be.vdab.stories.jira.domain.JiraIssue;
import be.vdab.stories.jira.endpoint.JiraWrapper;

import javax.inject.Named;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

@Named
public class JiraService {

    private Pattern pattern;
    private JiraWrapper endpoint;
    private GitService gitService;
    private String projectLabel;

    public JiraService(String projectLabel, JiraWrapper endpoint, GitService gitService){
        this.projectLabel = projectLabel;
        this.endpoint = endpoint;
        this.gitService = gitService;
        this.pattern = Pattern.compile(String.format("(?s)(.*)(%s-\\d+)(.*)", projectLabel));
    }

    public Set<JiraIssue> getJiraIssuesSinceLastRelease() {
        return filterAllIssues(gitService.getCommitsSinceLastReleaseUntilLatestCommit());
    }

    public Set<JiraIssue> getAllJiraIssues(){
        return filterAllIssues(gitService.getAllCommits());
    }

    private Set<JiraIssue> filterAllIssues(List<GitCommit> commits){
        return commits
                .stream()
                .map(GitCommit::getMessage)
                .filter(message -> message.contains(projectLabel))
                .map(message -> {
                    Matcher matcher = pattern.matcher(message);
                    matcher.matches();
                    return matcher.group(2);
                })
                .map(issue -> endpoint.getIssue(issue))
                .collect(toSet());
    }
}
