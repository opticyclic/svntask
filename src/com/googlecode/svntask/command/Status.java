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
 *  committedRevisionProperty = svn.status.committedRevision
 *  remoteRevisionProperty = svn.status.remoteRevision
 *  revisionProperty = svn.status.revision
 *  copyFromRevisionProperty = svn.status.copyFromRevision
 *  remotePropertiesStatusProperty = svn.status.remotePropertiesStatus
 *  remoteContentsStatusProperty = svn.status.remoteContentsStatus
 *  propertiesStatusProperty = svn.status.propertiesStatus
 *  contentsStatusProperty = svn.status.contentsStatus
 *  remoteDateProperty = svn.status.remoteDate
 *  committedDateProperty = svn.status.committedDate
 *  workingContentsDateProperty = svn.status.workingContentsDate
 *  workingPropertiesDateProperty = svn.status.workingPropertiesDate
 *  authorProperty = svn.status.author
 *  remoteAuthorProperty = svn.status.remoteAuthor
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
		File filePath = new File(this.path);

		this.getTask().log("status " + filePath.getCanonicalPath());

		// Set the default property in ant in case we have an exception below.
		this.getProject().setProperty(this.revisionProperty, "-1");

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
		this.getProject().setProperty(this.committedRevisionProperty, new Long(committedRevision.getNumber()).toString());

		if (remoteRevision != null)
			this.getProject().setProperty(this.remoteRevisionProperty, new Long(remoteRevision.getNumber()).toString());

		this.getProject().setProperty(this.revisionProperty, new Long(revision.getNumber()).toString());
		this.getProject().setProperty(this.copyFromRevisionProperty, new Long(copyFromRevision.getNumber()).toString());

		this.getProject().setProperty(this.remotePropertiesStatusProperty, remotePropertiesStatus.toString());
		this.getProject().setProperty(this.remoteContentsStatusProperty, remoteContentsStatus.toString());
		this.getProject().setProperty(this.propertiesStatusProperty, propertiesStatus.toString());
		this.getProject().setProperty(this.contentsStatusProperty, contentsStatus.toString());

		if (remoteDate != null)
			this.getProject().setProperty(this.remoteDateProperty, DateFormat.getInstance().format(remoteDate));

		this.getProject().setProperty(this.committedDateProperty, DateFormat.getInstance().format(committedDate));
		this.getProject().setProperty(this.workingContentsDateProperty, DateFormat.getInstance().format(workingContentsDate));
		this.getProject().setProperty(this.workingPropertiesDateProperty, DateFormat.getInstance().format(workingPropertiesDate));

		this.getProject().setProperty(this.authorProperty, author);

		if (remoteAuthor != null)
			this.getProject().setProperty(this.remoteAuthorProperty, remoteAuthor);
	}

	@Override
	protected void validateAttributes() throws Exception
	{
		if (this.path == null)
			throw new Exception("path cannot be null");

		if (this.committedRevisionProperty == null)
			this.committedRevisionProperty = "svn.status.committedRevision";
		if (this.remoteRevisionProperty == null)
			this.remoteRevisionProperty = "svn.status.remoteRevision";
		if (this.revisionProperty == null)
			this.revisionProperty = "svn.status.revision";
		if (this.copyFromRevisionProperty == null)
			this.copyFromRevisionProperty = "svn.status.copyFromRevision";

		if (this.remotePropertiesStatusProperty == null)
			this.remotePropertiesStatusProperty = "svn.status.remotePropertiesStatus";
		if (this.remoteContentsStatusProperty == null)
			this.remoteContentsStatusProperty = "svn.status.remoteContentsStatus";
		if (this.propertiesStatusProperty == null)
			this.propertiesStatusProperty = "svn.status.propertiesStatus";
		if (this.contentsStatusProperty == null)
			this.contentsStatusProperty = "svn.status.contentsStatus";

		if (this.remoteDateProperty == null)
			this.remoteDateProperty = "svn.status.remoteDate";
		if (this.committedDateProperty == null)
			this.committedDateProperty = "svn.status.committedDate";
		if (this.workingContentsDateProperty == null)
			this.workingContentsDateProperty = "svn.status.workingContentsDate";
		if (this.workingPropertiesDateProperty == null)
			this.workingPropertiesDateProperty = "svn.status.workingPropertiesDate";

		if (this.authorProperty == null)
			this.authorProperty = "svn.status.author";
		if (this.remoteAuthorProperty == null)
			this.remoteAuthorProperty = "svn.status.remoteAuthor";
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
	 * @param committedRevisionProperty The committedRevisionProperty; to set. defaults to svn.status.committedRevision
	 */
	public void setCommittedRevisionProperty(String committedRevisionProperty)
	{
		this.committedRevisionProperty = committedRevisionProperty;
	}

	/**
	 * @param remoteRevisionProperty The remoteRevisionProperty; to set. defaults to svn.status.remoteRevision
	 */
	public void setRemoteRevisionProperty(String remoteRevisionProperty)
	{
		this.remoteRevisionProperty = remoteRevisionProperty;
	}

	/**
	 * @param revisionProperty The revisionProperty to set. defaults to svn.status.revision
	 */
	public void setRevisionProperty(String revisionProperty)
	{
		this.revisionProperty = revisionProperty;
	}

	/**
	 * @param copyFromRevisionProperty The copyFromRevisionProperty to set. defaults to svn.status.copyFromRevision
	 */
	public void setCopyFromRevisionProperty(String copyFromRevisionProperty)
	{
		this.copyFromRevisionProperty = copyFromRevisionProperty;
	}

	/**
	 * @param remotePropertiesStatusProperty The remotePropertiesStatusProperty to set. defaults to svn.status.remotePropertiesStatus
	 */
	public void setRemotePropertiesStatusProperty(String remotePropertiesStatusProperty)
	{
		this.remotePropertiesStatusProperty = remotePropertiesStatusProperty;
	}

	/**
	 * @param remoteContentsStatusProperty The remoteContentsStatusProperty to set. defaults to svn.status.remoteContentsStatus
	 */
	public void setRemoteContentsStatusProperty(String remoteContentsStatusProperty)
	{
		this.remoteContentsStatusProperty = remoteContentsStatusProperty;
	}

	/**
	 * @param propertiesStatusProperty The propertiesStatusProperty to set. defaults to svn.status.propertiesStatus
	 */
	public void setPropertiesStatusProperty(String propertiesStatusProperty)
	{
		this.propertiesStatusProperty = propertiesStatusProperty;
	}

	/**
	 * @param contentsStatusProperty The contentsStatusProperty to set. defaults to svn.status.contentsStatus
	 */
	public void setContentsStatusProperty(String contentsStatusProperty)
	{
		this.contentsStatusProperty = contentsStatusProperty;
	}

	/**
	 * @param remoteDateProperty The remoteDateProperty to set. defaults to svn.status.remoteDate
	 */
	public void setRemoteDateProperty(String remoteDateProperty)
	{
		this.remoteDateProperty = remoteDateProperty;
	}

	/**
	 * @param committedDateProperty The committedDateProperty to set. defaults to svn.status.committedDate
	 */
	public void setCommittedDateProperty(String committedDateProperty)
	{
		this.committedDateProperty = committedDateProperty;
	}

	/**
	 * @param workingContentsDateProperty The workingContentsDateProperty to set. defaults to svn.status.workingContentsDate
	 */
	public void setWorkingContentsDateProperty(String workingContentsDateProperty)
	{
		this.workingContentsDateProperty = workingContentsDateProperty;
	}

	/**
	 * @param workingPropertiesDateProperty The workingPropertiesDateProperty to set. defaults to svn.status.workingPropertiesDate
	 */
	public void setWorkingPropertiesDateProperty(String workingPropertiesDateProperty)
	{
		this.workingPropertiesDateProperty = workingPropertiesDateProperty;
	}

	/**
	 * @param authorProperty The authorProperty to set. defaults to svn.status.author
	 */
	public void setAuthorProperty(String authorProperty)
	{
		this.authorProperty = authorProperty;
	}

	/**
	 * @param remoteAuthorProperty The remoteAuthorProperty to set. defaults to svn.status.remoteAuthor
	 */
	public void setRemoteAuthorProperty(String remoteAuthorProperty)
	{
		this.remoteAuthorProperty = remoteAuthorProperty;
	}
}
