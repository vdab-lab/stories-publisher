package be.vdab.stories.github;

import be.vdab.stories.github.domain.GithubCommit;
import be.vdab.stories.github.domain.GithubRequest;
import be.vdab.stories.github.endpoint.GithubWrapper;
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

import static be.vdab.stories.github.domain.GithubCommit.aGithubCommit;
import static be.vdab.stories.github.domain.GithubRelease.Commit.aCommit;
import static be.vdab.stories.github.domain.GithubRelease.aGithubRelease;

@RunWith(MockitoJUnitRunner.class)
public class GithubServiceTest {

    @Mock
    private GithubWrapper githubWrapper;

    @InjectMocks
    private GithubService githubService;

    @Before
    public void setup() {
        Mockito.when(githubWrapper.getReleases(Matchers.any(GithubRequest.class), Matchers.eq(1)))
                .thenReturn(
                        Lists.newArrayList(
                                aGithubRelease().withCommit(aCommit().withSha("sha3")),
                                aGithubRelease().withCommit(aCommit().withSha("sha4"))
                        ));
        Mockito.when(githubWrapper.getCommits(Matchers.any(GithubRequest.class), Matchers.eq(1)))
                .thenReturn(
                        Lists.newArrayList(
                                aGithubCommit().withSha("sha1"),
                                aGithubCommit().withSha("sha2"),
                                aGithubCommit().withSha("sha3"),
                                aGithubCommit().withSha("sha4")
                        ));
    }

    @Test
    public void getAllReleasesSinceHash() {
        ReflectionTestUtils.setField(githubService, "latestCommit", "sha2");
        List<GithubCommit> commitsSinceLastRelease = githubService.getCommitsSinceLastReleaseUntilLatestCommit();

        Assertions.assertThat(commitsSinceLastRelease)
                .hasSize(2)
                .contains(
                        aGithubCommit().withSha("sha2"),
                        aGithubCommit().withSha("sha3")
                );
    }
}