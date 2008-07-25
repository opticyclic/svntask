package com.googlecode.svntask.command;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;

import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNStatus;
import org.tmatesoft.svn.core.wc.SVNStatusClient;
import org.tmatesoft.svn.core.wc.SVNStatusType;

import com.googlecode.svntask.Command;


/**
 * Used for executing svn status
 * 
 * Available properties to set are:
 * 	revisionProperty = svn.info.revision
 *  urlProperty = svn.info.url
 *  repositoryRootUrlProperty = svn.info.repositoryRootUrl
 *  authorProperty = svn.info.author
 *  committedDateProperty = svn.info.committedDate
 *
 * @author jonstevens
 */
public class Status extends Command
{
	private String path;
	private boolean remote = true;
	private boolean ignoreExternals = false;

	private String committedRevisionProperty;
	private String remoteRevisionProperty;
	private String revisionProperty;
	private String copyFromRevisionProperty;

	private String remotePropertiesStatusProperty;
	private String remoteContentsStatusProperty;
	private String propertiesStatusProperty;
	private String contentsStatusProperty;
	
	private String remoteDateProperty;
	private String committedDateProperty;
	private String workingContentsDateProperty;
	private String workingPropertiesDateProperty;

	private String authorProperty;
	private String remoteAuthorProperty;

	/** */
	@Override
	public void execute() throws Exception
	{
		File filePath = new File(path);

		this.getTask().log("status " + filePath.getCanonicalPath());

		// Set the default property in ant in case we have an exception below.
		this.getProject().setProperty(revisionProperty, "-1");

		// Get the WC Client
		SVNStatusClient client = this.getTask().getSvnClient().getStatusClient();

		// Execute svn status
		SVNStatus status = client.doStatus(filePath, this.remote, this.ignoreExternals);

		// Get the interesting status data
		SVNRevision committedRevision = status.getCommittedRevision();
		SVNRevision remoteRevision = status.getRemoteRevision();
		SVNRevision revision = status.getRevision();
		SVNRevision copyFromRevision = status.getCopyFromRevision();

		SVNStatusType remotePropertiesStatus = status.getRemotePropertiesStatus();
		SVNStatusType remoteContentsStatus = status.getRemoteContentsStatus();
		SVNStatusType propertiesStatus = status.getPropertiesStatus();
		SVNStatusType contentsStatus = status.getContentsStatus();
		
		Date remoteDate = status.getRemoteDate();
		Date committedDate = status.getCommittedDate();
		Date workingContentsDate = status.getWorkingContentsDate();
		Date workingPropertiesDate = status.getWorkingPropertiesDate();
		
		String author = status.getAuthor();
		String remoteAuthor = status.getRemoteAuthor();
		
		// Set the computed properties in ant
		this.getProject().setProperty(committedRevisionProperty, new Long(committedRevision.getNumber()).toString());
		this.getProject().setProperty(remoteRevisionProperty, new Long(remoteRevision.getNumber()).toString());
		this.getProject().setProperty(revisionProperty, new Long(revision.getNumber()).toString());
		this.getProject().setProperty(copyFromRevisionProperty, new Long(copyFromRevision.getNumber()).toString());

		this.getProject().setProperty(remotePropertiesStatusProperty, remotePropertiesStatus.toString());
		this.getProject().setProperty(remoteContentsStatusProperty, remoteContentsStatus.toString());
		this.getProject().setProperty(propertiesStatusProperty, propertiesStatus.toString());
		this.getProject().setProperty(contentsStatusProperty, contentsStatus.toString());
		
		this.getProject().setProperty(remoteDateProperty, DateFormat.getInstance().format(remoteDate));
		this.getProject().setProperty(committedDateProperty, DateFormat.getInstance().format(committedDate));
		this.getProject().setProperty(workingContentsDateProperty, DateFormat.getInstance().format(workingContentsDate));
		this.getProject().setProperty(workingPropertiesDateProperty, DateFormat.getInstance().format(workingPropertiesDate));

		this.getProject().setProperty(authorProperty, author);
		this.getProject().setProperty(remoteAuthorProperty, remoteAuthor);
	}

	@Override
	protected void validateAttributes() throws Exception
	{
		if (path == null)
			throw new Exception("path cannot be null");

		if (committedRevisionProperty == null)
			committedRevisionProperty = "svn.status.committedRevision";
		if (remoteRevisionProperty == null)
			remoteRevisionProperty = "svn.status.remoteRevision";
		if (revisionProperty == null)
			revisionProperty = "svn.status.revision";
		if (copyFromRevisionProperty == null)
			copyFromRevisionProperty = "svn.status.copyFromRevision";

		if (remotePropertiesStatusProperty == null)
			remotePropertiesStatusProperty = "svn.status.remotePropertiesStatus";
		if (remoteContentsStatusProperty == null)
			remoteContentsStatusProperty = "svn.status.remoteContentsStatus";
		if (propertiesStatusProperty == null)
			propertiesStatusProperty = "svn.status.propertiesStatus";
		if (contentsStatusProperty == null)
			contentsStatusProperty = "svn.status.contentsStatus";

		if (remoteDateProperty == null)
			remoteDateProperty = "svn.status.remoteDate";
		if (committedDateProperty == null)
			committedDateProperty = "svn.status.committedDate";
		if (workingContentsDateProperty == null)
			workingContentsDateProperty = "svn.status.workingContentsDate";
		if (workingPropertiesDateProperty == null)
			workingPropertiesDateProperty = "svn.status.workingPropertiesDate";
		
		if (authorProperty == null)
			authorProperty = "svn.status.author";
		if (remoteAuthorProperty == null)
			remoteAuthorProperty = "svn.status.remoteAuthor";
	}

	/**
	 * path to the file or directory
	 */
	public void setPath(String path)
	{
		this.path = path;
	}

	/**
	 * to check up the status of the item in the repository, 
	 * that will tell if the local item is out-of-date (like '-u' 
	 * option in the SVN client's 'svn status' command)
	 * default true.
	 */
	public void setRemote(boolean remote)
	{
		this.remote = remote;
	}
	
	/** 
	 * ignore externals definitions
	 * default false.
	 */
	public void setIgnoreExternals(boolean ignoreExternals)
	{
		this.ignoreExternals = ignoreExternals;
	}
	
	/**
	 * @param revisionProperty The revisionProperty to set. defaults to svn.info.revision
	 */
	public void setRevisionProperty(String revisionProperty)
	{
		this.revisionProperty = revisionProperty;
	}
	
//	/**
//	 * @param urlProperty The urlProperty to set. defaults to svn.info.url
//	 */
//	public void setUrlProperty(String urlProperty)
//	{
//		this.urlProperty = urlProperty;
//	}
//
//	/**
//	 * @param repositoryRootUrlProperty The repositoryRootUrlProperty to set. defaults to svn.info.repositoryRootUrl
//	 */
//	public void setRepositoryRootUrlProperty(String repositoryRootUrlProperty)
//	{
//		this.repositoryRootUrlProperty = repositoryRootUrlProperty;
//	}

	/**
	 * @param authorProperty The authorProperty to set. defaults to svn.info.author
	 */
	public void setAuthorProperty(String authorProperty)
	{
		this.authorProperty = authorProperty;
	}

	/**
	 * @param committedDateProperty The committedDateProperty to set. defaults to svn.info.committedDate
	 */
	public void setCommittedDateProperty(String committedDateProperty)
	{
		this.committedDateProperty = committedDateProperty;
	}
}
