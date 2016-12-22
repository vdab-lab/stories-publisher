package be.vdab.stories.git;

import be.vdab.stories.git.GitService;
import be.vdab.stories.git.domain.GitCommit;
import be.vdab.stories.git.domain.GitRequest;
import be.vdab.stories.git.endpoint.github.GithubWrapper;
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

import java.util.List;

import static be.vdab.stories.git.domain.GitCommit.aGitCommit;
import static be.vdab.stories.git.domain.GitRelease.aGitRelease;

@RunWith(MockitoJUnitRunner.class)
public class GitServiceTest {

    @Mock
    private GithubWrapper githubWrapper;

    @InjectMocks
    private GitService gitService;

    @Before
    public void setup() {
        Mockito.when(githubWrapper.getReleases(Matchers.any(GitRequest.class), Matchers.eq(1)))
                .thenReturn(
                        Lists.newArrayList(
                                aGitRelease("", aGitCommit("sha3")),
                                aGitRelease("", aGitCommit("sha3"))
                        ));
        Mockito.when(githubWrapper.getCommits(Matchers.any(GitRequest.class), Matchers.eq(1)))
                .thenReturn(
                        Lists.newArrayList(
                                aGitCommit("sha1"),
                                aGitCommit("sha2"),
                                aGitCommit("sha3"),
                                aGitCommit("sha4")
                        ));
    }

    @Test
    public void getAllReleasesSinceHash() {
        ReflectionTestUtils.setField(gitService, "latestCommit", "sha2");
        List<GitCommit> commitsSinceLastRelease = gitService.getCommitsSinceLastReleaseUntilLatestCommit();

        Assertions.assertThat(commitsSinceLastRelease)
                .hasSize(2)
                .contains(
                        aGitCommit("sha2"),
                        aGitCommit("sha3")
                );
    }
}