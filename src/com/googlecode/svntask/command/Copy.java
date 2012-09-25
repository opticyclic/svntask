package com.googlecode.svntask.command;

import java.io.File;

import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNErrorMessage;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.SVNCopyClient;
import org.tmatesoft.svn.core.wc.SVNCopySource;
import org.tmatesoft.svn.core.wc.SVNRevision;

import com.googlecode.svntask.Command;

/**
 * From <a href="https://github.com/chripo/svntask/">https://github.com/chripo/svntask/</a>
 *
 * @author christoph polcin (christoph-polcin.com)
 */
public class Copy extends Command {
	private String src;
	private String dst;
	private boolean isMove;
	private boolean failOnDstExists = true;
	private String commitMessage = "copy by svntask";

	@Override
	public void execute() throws Exception {
		this.getTask().log("copy "  + src + " to " + dst );

		// Get the copy client
		SVNCopyClient copyC = this.getTask().getSvnClient().getCopyClient();

		SVNRevision srcRev;
		SVNURL srcUrl;
		if(src.startsWith("http") || src.startsWith("svn")){
			srcRev = SVNRevision.HEAD; //TODO make setable
			srcUrl = SVNURL.parseURIEncoded(src);
		} else {
			srcRev = SVNRevision.WORKING;
			srcUrl = SVNURL.fromFile(new File(src).getCanonicalFile());
		}

		SVNCopySource source = new SVNCopySource(SVNRevision.UNDEFINED, srcRev, srcUrl );

		SVNURL dstPath = dst.startsWith("http") || dst.startsWith("svn") ?  SVNURL.parseURIEncoded(dst) : SVNURL.fromFile(new File(dst).getCanonicalFile());

		SVNCommitInfo nfo = copyC.doCopy(new SVNCopySource[]{source}, dstPath, isMove, true, failOnDstExists, commitMessage, null);

		SVNErrorMessage err = nfo.getErrorMessage();
		if(err != null)
			this.getTask().log("error on copy resources: " + err.getMessage() );
	}

	@Override
	protected void validateAttributes() throws Exception {
		if(src == null)
			throw new NullPointerException("set source path");
		if(dst == null)
			throw new NullPointerException("set a destination url");
		if(commitMessage == null)
			throw new NullPointerException("set a commit message");
	}


	public void setDst(String path) {
		this.dst = path;
	}

	public void setMove(boolean isMove) {
		this.isMove = isMove;
	}

	public void setFailOnDstExists(boolean failOnDstExists) {
		this.failOnDstExists = failOnDstExists;
	}

	public void setCommitMessage(String commitMessage) {
		this.commitMessage = commitMessage;
	}

	public void setSrc(String path) {
		this.src = path;
	}
}
