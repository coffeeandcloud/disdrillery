package com.martinsteinhauer.disdrillery.git.repository;

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.merge.MergeStrategy;
import org.eclipse.jgit.transport.FetchResult;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class GitRepository {

    private Git git;
    List<Pair<String, String>> refNames;

    private Logger log = Logger.getLogger(getClass().getName());

    public GitRepository(Git git) {
        this.git = git;
    }

    public void checkoutAllBranches() {
        try {
            refNames = git.branchList().setListMode(ListBranchCommand.ListMode.REMOTE).call()
                    .stream()
                    .map(ref -> Pair.of(ref.getName().replace("refs/remotes/", ""),ref.getName()))
                    .filter(ref -> !ref.getValue().contains("HEAD") && !ref.getValue().contains("tags"))
                    .collect(Collectors.toList());
            refNames.stream()
                    .forEach(ref -> {
                        try {
                            git.branchCreate()
                                    .setName(ref.getKey())
                                    .setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK)
                                    .setStartPoint(ref.getValue())
                                    .setForce(true)
                                    .call();
                            log.info("Tracking branch <" + ref.getValue() + ">");
                        } catch (GitAPIException e) {
                            e.printStackTrace();
                        }
            });

            try {
                FetchResult fetchResult = git.fetch().setRemote("origin").call();
                log.info(fetchResult.getMessages());
            } catch (GitAPIException e) {
                e.printStackTrace();
            }

            refNames.stream().forEach(ref -> {
                /*
                try {
                    //PullResult pullResult = git.pull().setStrategy(MergeStrategy.THEIRS).setRemoteBranchName(ref.getKey().replace("origin/", "")).setRemote("origin").call();
                    //log.info(pullResult.toString());
                } catch (GitAPIException e) {
                    e.printStackTrace();
                }

                 */
            });
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }
}
