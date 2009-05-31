package com.googlecode.svntask.command;

import java.io.File;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;

import com.googlecode.svntask.Command;


/**
 * Used for executing svn switch. Defaults to recursive == true. Requires an svn URL and optionally a
 * revision. HEAD is the default peg/revision if a revision is not specified. depth is INFINITY.
 * allowUnversionedObstructions is true.
 *
 * @author Raman Gupta
 * @author Jon Stevens
 */
public class Switch extends Command
{
	private String path;
	private SVNURL svnUrl;
	private SVNRevision pegRevision = SVNRevision.HEAD;
	private SVNRevision revision = SVNRevision.HEAD;
	private SVNDepth depth = SVNDepth.INFINITY;
	private boolean recursive = true;
	private boolean allowUnversionedObstructions = true;

	/** */
	@Override
	public void execute() throws Exception
	{
		File filePath = new File(this.path);

		this.getTask().log("switch " + filePath.getCanonicalPath() + " to URL " + this.svnUrl + " at revision " + this.revision);

		// Get the Update Client
		SVNUpdateClient client = this.getTask().getSvnClient().getUpdateClient();

		// Execute svn switch
		client.doSwitch(filePath, this.svnUrl, this.pegRevision, this.revision, this.depth, this.allowUnversionedObstructions, this.recursive);
	}

	/** */
	@Override
	protected void validateAttributes() throws Exception
	{
		if (this.path == null)
			throw new Exception("path cannot be null");

		if (this.svnUrl == null)
			throw new Exception("svnUrl cannot be null");
	}

	/** */
	public void setPath(String path)
	{
		this.path = path;
	}

	/** */
	public void setSvnUrl(String svnUrl) throws SVNException
	{
		this.svnUrl = SVNURL.parseURIDecoded(svnUrl);
	}

	/** */
	public void setPegRevision(long pegRevision)
	{
		this.pegRevision = SVNRevision.create(pegRevision);
	}

	/** */
	public void setRevision(long revision)
	{
		this.revision = SVNRevision.create(revision);
	}

	/** */
	public void setDepth(String depth)
	{
		this.depth = SVNDepth.fromString(depth);
	}

	/** */
	public void setAllowUnversionedObstructions(boolean allowUnversionedObstructions)
	{
		this.allowUnversionedObstructions = allowUnversionedObstructions;
	}

	/** */
	public void setRecursive(boolean recursive)
	{
		this.recursive = recursive;
	}
}
