package be.vdab.stories.github;

import be.vdab.stories.github.domain.GithubCommit;
import be.vdab.stories.github.domain.GithubRelease;
import be.vdab.stories.github.domain.GithubRequest;
import be.vdab.stories.github.endpoint.GithubWrapper;
import com.google.common.collect.Lists;

import javax.inject.Named;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Named
public class GithubService {
    private final String latestCommit;
    private GithubRequest request;
    private GithubWrapper endpoint;

    public GithubService(String repoUser, String repositoryName, String latestCommit, String authorizationToken, GithubWrapper endpoint) {
        this.latestCommit = latestCommit;
        this.request = GithubRequest.aGithubRequest()
                .withUser(repoUser)
                .withRepositoryName(repositoryName)
                .withAuthorization(authorizationToken);
        this.endpoint = endpoint;
    }

    public List<GithubCommit> getCommitsSinceLastReleaseUntilLatestCommit(){
        return getCommitsFromReleaseUntilLatestCommit(getAllReleases().get(0));
    }

    private List<GithubCommit> getCommitsFromReleaseUntilLatestCommit(GithubRelease release) {
        List<GithubCommit> githubCommits = fetchPagesUntil(
                page -> endpoint.getCommits(request, page),
                commits -> {
                    List<String> hashes = commits.stream().map(commit -> commit.sha).collect(Collectors.toList());
                    return hashes.contains(release.commit.sha);
                }
        );
        int latestCommitIndex = getIndexOfCommit(this.latestCommit, githubCommits, 0);
        int tagIndex = getIndexOfCommit(release.commit.sha, githubCommits, githubCommits.size());

        return githubCommits.subList(latestCommitIndex, tagIndex+1);
    }

    private int getIndexOfCommit(String hash, List<GithubCommit> githubCommits, int orElse) {
        return IntStream.range(0, githubCommits.size())
                    .filter(index -> githubCommits.get(index).sha.equals(hash))
                    .findFirst()
                    .orElse(orElse);
    }

    private List<GithubRelease> getAllReleases() {
        return fetchPagesUntil(
                page -> endpoint.getReleases(request, page),
                List::isEmpty
        );
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
