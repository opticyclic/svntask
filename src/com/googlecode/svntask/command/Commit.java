package com.googlecode.svntask.command;

import java.io.File;

import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNCommitClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import com.googlecode.svntask.Command;

/**
 * Implements the "commit" command. Requires a "path" String argument indicating
 * the path to commit. Has optional boolean argument "keepLocks", "force".
 * Also has an optional String argument "commitMessage" to specify
 * a commit message. Example: {@code <svn><commit path="."
 * commitMessage="changed stuff" /></svn>}
 *
 * @author darren (darren@bizo.com)
 * @author Jon Stevens
 */
public class Commit extends Command
{

	private String path;
	private boolean keepLocks = false;
	private String commitMessage = "commit by svntask";
	private boolean force = true;
	private SVNDepth depth = SVNDepth.INFINITY;
	private String user;
	private String password;

	/** */
	@Override
	public void execute() throws Exception
	{
		File[] filePaths = new File[1];
		filePaths[0] = new File(this.path);

		this.getTask().log("commit " + filePaths[0].getCanonicalPath());		

		SVNClientManager cm = this.getTask().getSvnClient();
		
		if (this.user != null && this.password != null) {
			ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(this.user, this.password);
			cm.setAuthenticationManager(authManager);
		} 			
				
		// Get the commit client
		SVNCommitClient commitClient = cm.getCommitClient();
		// Execute SVN commit
		SVNCommitInfo info = commitClient.doCommit(filePaths, this.keepLocks, this.commitMessage, null, null, true, this.force, this.depth);

		long newRevision = info.getNewRevision();
		if (newRevision >= 0)
			this.getTask().log("commit successful: new revision = " + newRevision);
		else
			this.getTask().log("no commits performed (commit operation returned new revision < 0)");
	}

	/** */
	@Override
	protected void validateAttributes() throws Exception
	{
		if (this.path == null)
			throw new NullPointerException("path cannot be null");
	}

	/** */
	public void setPath(String path)
	{
		this.path = path;
	}

	/** */
	public void setKeepLocks(boolean keepLocks)
	{
		this.keepLocks = keepLocks;
	}

	/** */
	public void setCommitMessage(String commitMessage)
	{
		this.commitMessage = commitMessage;
	}

	/** */
	public void setForce(boolean force)
	{
		this.force = force;
	}

	/** */
	public void setDepth(String depth)
	{
		this.depth = SVNDepth.fromString(depth);
	}
	
	/** */
	public void setUsername(String user)
	{
		this.user = user;
	}
	
	/** */
	public void setPassword(String pass)
	{
		this.password = pass;
	}

}
