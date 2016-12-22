package be.vdab.stories.jira;

import be.vdab.stories.git.GitService;
import be.vdab.stories.jira.domain.JiraIssue;
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

import static be.vdab.stories.git.domain.GitCommit.aGitCommit;
import static be.vdab.stories.jira.domain.JiraIssue.aJiraIssue;


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
        ReflectionTestUtils.setField(jiraService, "projectLabel", "LAB");
        ReflectionTestUtils.setField(jiraService, "pattern", Pattern.compile(String.format("(?s)(.*)(LAB-\\d+)(.*)")));
        Mockito.when(jiraWrapper.getIssue(Matchers.anyString())).then(call -> aJiraIssue((String) call.getArguments()[0]));
    }

    @Test
    public void getJiraIssuesSinceLastCommit() {
        Mockito.when(gitService.getCommitsSinceLastReleaseUntilLatestCommit()).thenReturn(
                Lists.newArrayList(
                        aGitCommit("1").withMessage("[Tim] Hallo"),
                        aGitCommit("2").withMessage("[Tim] LAB-123 hallo"),
                        aGitCommit("3").withMessage("LAB-245 bla"),
                        aGitCommit("4").withMessage("Test"),
                        aGitCommit("5").withMessage("[Tim] LAB-123 bye"),
                        aGitCommit("6").withMessage("Bye")
                )
        );

        Set<JiraIssue> jiraIssuesSinceLastCommit = jiraService.getJiraIssuesSinceLastRelease();

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