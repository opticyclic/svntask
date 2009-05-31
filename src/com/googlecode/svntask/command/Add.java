package com.googlecode.svntask.command;

import java.io.File;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.wc.SVNWCClient;

import com.googlecode.svntask.Command;

/**
 * Implements the "add" command. Requires a "path" argument indicating the path
 * to add. Has optional boolean arguments "force", "mkdir",
 * "climbUnversionedParents", "includeIgnored" and "makeParents". Example: {@code <svn><add
 * path="." force="false" /></svn>}
 *
 * @author darren (darren@bizo.com)
 * @author Jon Stevens
 */
public class Add extends Command
{
	private String path;
	private boolean force = true;
	private boolean mkdir = false;
	private boolean climbUnversionedParents = true;
	private SVNDepth depth = SVNDepth.INFINITY;
	private boolean includeIgnored = true;
	private boolean makeParents = true;

	/** */
	@Override
	public void execute() throws Exception
	{
		File filePath = new File(this.path);

		this.getTask().log("add " + filePath.getCanonicalPath());

		SVNWCClient wcClient = this.getTask().getSvnClient().getWCClient();
		wcClient.doAdd(filePath, this.force, this.mkdir, this.climbUnversionedParents, this.depth, this.includeIgnored,
				this.makeParents);
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
	public void setForce(boolean force)
	{
		this.force = force;
	}

	/** */
	public void setMkdir(boolean mkdir)
	{
		this.mkdir = mkdir;
	}

	/** */
	public void setClimbUnversionedParents(boolean climbUnversionedParents)
	{
		this.climbUnversionedParents = climbUnversionedParents;
	}

	/** */
	public void setDepth(String depth)
	{
		this.depth = SVNDepth.fromString(depth);
	}

	/** */
	public void setIncludeIgnored(boolean includeIgnored)
	{
		this.includeIgnored = includeIgnored;
	}

	/** */
	public void setMakeParents(boolean makeParents)
	{
		this.makeParents = makeParents;
	}
}
