package be.vdab.stories.jira;


import be.vdab.stories.git.GitService;
import be.vdab.stories.git.domain.GitCommit;
import be.vdab.stories.jira.domain.JiraIssue;
import be.vdab.stories.jira.endpoint.JiraWrapper;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class JiraService {

    private Pattern pattern = Pattern.compile("(?s)(.*)([A-Z]{3}-\\d+)(.*)");
    private JiraWrapper endpoint;
    private GitService gitService;

    public JiraService(JiraWrapper endpoint, GitService gitService) {
        this.endpoint = endpoint;
        this.gitService = gitService;
    }

    public List<JiraIssue> getJiraIssuesSinceLastRelease() {
        return filterAllIssues(gitService.getCommitsSinceLastReleaseUntilLatestCommit());
    }

    public List<JiraIssue> getAllJiraIssues(Date since) {
        return filterAllIssues(gitService.getAllCommits(since));
    }

    private List<JiraIssue> filterAllIssues(List<GitCommit> commits) {
        String issues = commits
                .stream()
                .map(GitCommit::getMessage)
                .filter(pattern.asPredicate())
                .map(message -> {
                    Matcher matcher = pattern.matcher(message);
                    matcher.matches();
                    return matcher.group(2);
                })
                .distinct()
                .collect(Collectors.joining(", "));
        return endpoint.searchIssues(String.format("key in (%s)", issues)).issues;
    }
}
