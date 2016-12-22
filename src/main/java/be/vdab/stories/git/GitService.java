package be.vdab.stories.git;

import be.vdab.stories.git.domain.GitCommit;
import be.vdab.stories.git.domain.GitRelease;
import be.vdab.stories.git.endpoint.GitEndpoint;
import be.vdab.stories.git.endpoint.github.GithubWrapper;
import be.vdab.stories.git.endpoint.github.GithubCommit;
import be.vdab.stories.git.endpoint.github.GithubRelease;
import be.vdab.stories.git.domain.GitRequest;
import com.google.common.collect.Lists;

import javax.inject.Named;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Named
public class GitService {
    private final String latestCommit;
    private GitRequest request;
    private GitEndpoint endpoint;

    public GitService(String repositoryName, String latestCommit, String authorizationToken, GitEndpoint endpoint) {
        this.latestCommit = latestCommit;
        this.request = GitRequest.aGithubRequest()
                .withRepositoryName(repositoryName)
                .withAuthorization(authorizationToken);
        this.endpoint = endpoint;
    }

    public List<GitCommit> getCommitsSinceLastReleaseUntilLatestCommit(){
        return getCommitsFromReleaseUntilLatestCommit(getAllReleases().get(0));
    }

    private List<GitRelease> getAllReleases() {
        return fetchPagesUntil(
                page -> endpoint.getReleases(request, page),
                List::isEmpty
        );
    }

    public List<GitCommit> getAllCommits() {
        return fetchPagesUntil(
                page -> endpoint.getCommits(request, page),
                List::isEmpty
        );
    }

    private List<GitCommit> getCommitsFromReleaseUntilLatestCommit(GitRelease release) {
        List<GitCommit> githubCommits = fetchPagesUntil(
                page -> endpoint.getCommits(request, page),
                commits -> {
                    List<String> hashes = commits.stream().map(GitCommit::getId).collect(Collectors.toList());
                    return hashes.contains(release.getGitCommit().getId());
                }
        );
        int latestCommitIndex = getIndexOfCommit(this.latestCommit, githubCommits, 0);
        int tagIndex = getIndexOfCommit(release.getGitCommit().getId(), githubCommits, githubCommits.size());

        return githubCommits.subList(latestCommitIndex, tagIndex+1);
    }

    private int getIndexOfCommit(String hash, List<GitCommit> githubCommits, int orElse) {
        return IntStream.range(0, githubCommits.size())
                    .filter(index -> githubCommits.get(index).getId().equals(hash))
                    .findFirst()
                    .orElse(orElse);
    }

    private <T> List<T> fetchPagesUntil(Function<Integer, List<T>> supply, Predicate<List<T>> until) {
        List<T> records;
        List<T> result = Lists.newArrayList();
        int page = 1;
        do {
            records = supply.apply(page++);
            result.addAll(records);
        } while (!until.test(records));
        return result;
    }
}
