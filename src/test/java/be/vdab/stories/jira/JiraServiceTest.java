package be.vdab.stories.jira;

import be.vdab.stories.jira.domain.JiraIssue;
import be.vdab.stories.jira.JiraService;
import be.vdab.stories.github.GithubService;
import be.vdab.stories.jira.endpoint.JiraWrapper;
import com.google.common.collect.Lists;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static be.vdab.stories.jira.domain.JiraIssue.aJiraIssue;
import static be.vdab.stories.github.domain.GithubCommit.Commit.aCommit;
import static be.vdab.stories.github.domain.GithubCommit.aGithubCommit;
import static com.google.common.collect.Lists.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class JiraServiceTest {

    @Mock
    private JiraWrapper jiraWrapper;

    @Mock
    private GithubService githubService;

    @InjectMocks
    private JiraService jiraService;

    @Before
    public void init() {
        ReflectionTestUtils.setField(jiraService, "projectLabel", "LAB");
        ReflectionTestUtils.setField(jiraService, "pattern", Pattern.compile(String.format("(?s)(.*)(LAB-\\d+)(.*)")));
        Mockito.when(jiraWrapper.getIssue(Matchers.anyString())).then(call -> aJiraIssue((String) call.getArguments()[0]));
    }

    @Test
    public void getJiraIssuesSinceLastCommit() {
        Mockito.when(githubService.getCommitsSinceLastReleaseUntilLatestCommit()).thenReturn(
                Lists.newArrayList(
                        aGithubCommit().withCommit(aCommit().withMessage("[Tim] Hallo")),
                        aGithubCommit().withCommit(aCommit().withMessage("[Tim] LAB-123 hallo")),
                        aGithubCommit().withCommit(aCommit().withMessage("LAB-245 bla")),
                        aGithubCommit().withCommit(aCommit().withMessage("Test")),
                        aGithubCommit().withCommit(aCommit().withMessage("[Tim] LAB-123 bye")),
                        aGithubCommit().withCommit(aCommit().withMessage("Bye"))
                )
        );

        Set<JiraIssue> jiraIssuesSinceLastCommit = jiraService.getJiraIssuesSinceLastCommit();

        Assertions.assertThat(jiraIssuesSinceLastCommit)
                .hasSize(2)
                .containsOnly(aJiraIssue("LAB-123"), aJiraIssue("LAB-245"));
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