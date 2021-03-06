package de.wellnerbou.gitchangelog.jgit;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import de.wellnerbou.gitchangelog.model.CommitDataModel;
import org.eclipse.jgit.revwalk.RevCommit;

public class CommitDataModelMapper {

	public Iterable<CommitDataModel> map(final Iterable<RevCommit> jGitLogBetween) {
		return FluentIterable.from(jGitLogBetween).transform(new Function<RevCommit, CommitDataModel>() {
			@Override
			public CommitDataModel apply(final RevCommit input) {
				return new CommitDataModel(input.getCommitTime(), input.getName(), input.getFullMessage());
			}
		});
	}
}
