package be.vdab.stories.jira;

import be.vdab.stories.git.GitService;
import be.vdab.stories.jira.domain.JiraIssue;
import be.vdab.stories.jira.endpoint.JiraWrapper;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static be.vdab.stories.git.domain.GitCommit.aGitCommit;
import static be.vdab.stories.jira.domain.JiraIssue.aJiraIssue;
import static be.vdab.stories.jira.domain.JiraQuery.aJiraQuery;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;


@RunWith(MockitoJUnitRunner.class)
public class JiraServiceTest {

    @Mock
    private JiraWrapper jiraWrapper;

    @Mock
    private GitService gitService;

    @InjectMocks
    private JiraService jiraService;

    @Before
    public void init() {
        Mockito.when(jiraWrapper.searchIssues(Matchers.anyString())).then(call -> {
            String query = (String) call.getArguments()[0];
            Pattern pattern = Pattern.compile("key in \\((.*)\\)");
            Matcher matcher = pattern.matcher(query);
            matcher.matches();
            List<JiraIssue> issues = newArrayList(matcher.group(1).split(", ")).stream().map(JiraIssue::aJiraIssue).collect(toList());

            return aJiraQuery().withIssues(issues);
        });
    }

    @Test
    public void getJiraIssuesSinceLastCommit() {
        Mockito.when(gitService.getCommitsSinceLastReleaseUntilLatestCommit()).thenReturn(
                newArrayList(
                        aGitCommit("1").withMessage("[Tim] Hallo"),
                        aGitCommit("2").withMessage("[Tim] LAB-123 hallo"),
                        aGitCommit("3").withMessage("LAB-245 bla"),
                        aGitCommit("4").withMessage("Test"),
                        aGitCommit("5").withMessage("[Tim] LAB-123 bye"),
                        aGitCommit("6").withMessage("[Tim] VSB-23 Vrijstellingen"),
                        aGitCommit("7").withMessage("Bye")
                )
        );

        List<JiraIssue> jiraIssuesSinceLastCommit = jiraService.getJiraIssuesSinceLastRelease();

        Assertions.assertThat(jiraIssuesSinceLastCommit)
                .hasSize(3)
                .containsOnly(
                        aJiraIssue("VSB-23"),
                        aJiraIssue("LAB-123"),
                        aJiraIssue("LAB-245"));
    }

    @Test
    public void regex() {
        Pattern compile = Pattern.compile("(?s)(.*)(LAB-\\d+)(.*)");
        Matcher matcher = compile.matcher("[Tim] LAB-253\n" +
                "- fixed unbound functions\n" +
                "- removed onDeviceReady call in constructor");
        matcher.matches();
        matcher.group(2);
    }
}