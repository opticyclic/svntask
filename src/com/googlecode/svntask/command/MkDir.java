package com.googlecode.svntask.command;

import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.SVNCommitClient;

import com.googlecode.svntask.Command;

/**
 * From <a href="https://github.com/chripo/svntask/">https://github.com/chripo/svntask/</a>
 *
 * @author christoph polcin (christoph-polcin.com)
 */
public class MkDir extends Command {
	private String path;
	private boolean makeParents = true;
	private String commitMessage = "mkdir by svntask";

	@Override
	public void execute() throws Exception {

		this.getTask().log("mkdir  " + path);

		// Get the commit client
		SVNCommitClient commitClient = this.getTask().getSvnClient().getCommitClient();

		SVNURL[] urlAr = { SVNURL.parseURIEncoded(path) } ;

		// Execute SVN commit
		SVNCommitInfo info = commitClient.doMkDir(urlAr, commitMessage, null, makeParents);

		long newRevision = info.getNewRevision();
		if (newRevision >= 0)
			this.getTask().log("commit successful: new revision = " + newRevision);
		else
			this.getTask().log("no commits performed (commit operation returned new revision < 0)");

	}

	@Override
	protected void validateAttributes() throws Exception {
		if (this.path == null)
			throw new NullPointerException("path cannot be null");

		if (this.commitMessage == null)
			throw new NullPointerException("commitMessage cannot be null");
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setMakeParents(boolean makeParents) {
		this.makeParents = makeParents;
	}

	public void setCommitMessage(String commitMessage) {
		this.commitMessage = commitMessage;
	}
}
