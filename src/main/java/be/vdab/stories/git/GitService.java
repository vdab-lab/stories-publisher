package be.vdab.stories.git;

import be.vdab.stories.git.domain.GitCommit;
import be.vdab.stories.git.domain.GitRelease;
import be.vdab.stories.git.domain.GitRequest;
import be.vdab.stories.git.endpoint.GitEndpoint;
import com.google.common.collect.Lists;

import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static be.vdab.stories.git.domain.GitRequest.GitRequestBuilder.aGitRequest;

public class GitService {
    private final String repositoryName;
    private final String latestCommit;
    private final String authorizationToken;
    private GitEndpoint endpoint;

    public GitService(String repositoryName, String latestCommit, String authorizationToken, GitEndpoint endpoint) {
        this.repositoryName = repositoryName;
        this.latestCommit = latestCommit;
        this.authorizationToken = authorizationToken;
        this.endpoint = endpoint;
    }

    public List<GitCommit> getCommitsSinceLastReleaseUntilLatestCommit(){
        return getCommitsFromReleaseUntilLatestCommit(getAllReleases().get(0));
    }

    private List<GitRelease> getAllReleases() {
        GitRequest gitRequest = aGitRequest()
                .withAuthorization(authorizationToken)
                .withRepositoryName(repositoryName)
                .build();

        return endpoint.getReleases(gitRequest);
    }

    public List<GitCommit> getAllCommits(Date since) {
        GitRequest gitRequest = aGitRequest()
                .withAuthorization(authorizationToken)
                .withRepositoryName(repositoryName)
                .withSince(since)
                .build();

        return fetchPagesUntil(
                page -> endpoint.getCommits(gitRequest, page),
                List::isEmpty
        );
    }

    private List<GitCommit> getCommitsFromReleaseUntilLatestCommit(GitRelease release) {
        GitRequest gitRequest = aGitRequest()
                .withAuthorization(authorizationToken)
                .withRepositoryName(repositoryName)
                .build();

        List<GitCommit> githubCommits = fetchPagesUntil(
                page -> endpoint.getCommits(gitRequest, page),
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
