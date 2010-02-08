package com.googlecode.svntask.command;

import com.googlecode.svntask.Command;
import org.apache.tools.ant.BuildException;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;

import java.io.File;

/**
 */
public class Checkout extends Command
{
	public static final String SVN_CHECKOUT_REVISION = "svn.checkout.revision";

	private String url;
	private File path;
	private SVNRevision pegRevision = SVNRevision.HEAD;
	private SVNRevision revision = SVNRevision.HEAD;
	private SVNDepth depth = SVNDepth.INFINITY;
	private boolean force = false;

	private String revisionProperty;

	@Override
	public void execute() throws Exception
	{
		// Get the Update Client
		SVNUpdateClient client = this.getTask().getSvnClient().getUpdateClient();

		SVNURL svnurl = SVNURL.parseURIDecoded(url);

		this.getTask().log("checkout " + url + " " + path);

		long r = client.doCheckout(svnurl, path, pegRevision, revision, depth, force);
		this.getProject().setProperty(this.revisionProperty, new Long(r).toString());
	}
	@Override
	protected void validateAttributes() throws Exception {
		if (this.url == null)
			throw new BuildException("url must be provided");
		if (this.path == null)
			throw new BuildException("path must be provided");

		if (this.revisionProperty == null)
			this.revisionProperty = SVN_CHECKOUT_REVISION;
	}

	public void setUrl(String url) throws SVNException
	{
		this.url = url;
	}

	public void setPath(File path)
	{
		this.path = path;
	}

	public void setPegRevision(long pegRevision)
	{
		this.pegRevision = SVNRevision.create(pegRevision);
	}

	public void setRevision(long revision)
	{
		this.revision = SVNRevision.create(revision);
	}

	public void setDepth(String depth)
	{
		this.depth = SVNDepth.fromString(depth);
	}

	public void setForce(boolean force)
	{
		this.force = force;
	}

	public void setRevisionProperty(String revisionProperty)
	{
		this.revisionProperty = revisionProperty;
	}
}
