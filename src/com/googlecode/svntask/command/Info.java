package com.googlecode.svntask.command;

import java.io.File;

import org.tmatesoft.svn.core.wc.SVNInfo;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCClient;

import com.googlecode.svntask.Command;


/**
 * Used for executing svn info
 * 
 * Right now, it just gets the revision information
 * for a working copy and puts it into the property
 * named revisionProperty or defaults to svn.info.revision
 * as the property name.
 * 
 * @author jonstevens
 */
public class Info extends Command
{
	private String path;
	private String revisionProperty;

	@Override
	public void execute() throws Exception
	{
		File filePath = new File(path);

		this.getTask().log("info " + filePath.getCanonicalPath());

		// Set the default property in ant in case we have an exception below.
		this.getProject().setProperty(revisionProperty, "-1");

		// Get the WC Client
		SVNWCClient client = this.getTask().getSvnClient().getWCClient();

		// Execute svn info
		SVNInfo info = client.doInfo(filePath, SVNRevision.WORKING);

		// Get the revision #
		SVNRevision revision = info.getRevision();
		String revisionNumber = new Long(revision.getNumber()).toString();

		// Set the computed property in ant
		this.getProject().setProperty(revisionProperty, revisionNumber);
	}

	@Override
	protected void validateAttributes() throws Exception
	{
		if (revisionProperty == null)
			revisionProperty = "svn.info.revision";

		if (path == null)
			throw new Exception("path cannot be null");
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	/**
	 * @param revisionProperty The revisionProperty to set. defaults to svn.info.revision
	 */
	public void setRevisionProperty(String revisionProperty)
	{
		this.revisionProperty = revisionProperty;
	}
}
