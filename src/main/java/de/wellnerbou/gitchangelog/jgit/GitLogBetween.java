package de.wellnerbou.gitchangelog.jgit;

import de.wellnerbou.gitchangelog.model.CommitDataModel;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.IOException;

public class GitLogBetween {

	private final Repository repo;
	private final CommitDataModelMapper commitDataModelMapper;

	public GitLogBetween(final Repository repository, final CommitDataModelMapper commitDataModelMapper) {
		this.repo = repository;
		this.commitDataModelMapper = commitDataModelMapper;
	}

	public Iterable<CommitDataModel> getGitLogBetween(final String rev1, final String rev2) throws RuntimeException {
		try {
			return commitDataModelMapper.map(getJGitLogBetween(rev1, rev2));
		} catch (IOException | GitAPIException e) {
			throw new RuntimeException(e);
		}
	}

	private Iterable<RevCommit> getJGitLogBetween(final String rev1, final String rev2) throws IOException, GitAPIException {
		final Ref refFrom = repo.getRef(rev1);
		if(refFrom == null) {
			throw new RuntimeException("Ref "+rev1+" not found.");
		}
		final Ref refTo = repo.getRef(rev2);
		if(refTo == null) {
			throw new RuntimeException("Ref "+rev2+" not found.");
		}
		return new Git(repo).log().addRange(getActualRefObjectId(refFrom), getActualRefObjectId(refTo)).call();
	}

	private ObjectId getActualRefObjectId(Ref ref) {
		final Ref repoPeeled = repo.peel(ref);
		if(repoPeeled.getPeeledObjectId() != null) {
			return repoPeeled.getPeeledObjectId();
		}
		return ref.getObjectId();
	}
}