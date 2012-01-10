package com.googlecode.svntask.command;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.SVNInfo;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCClient;

import com.googlecode.svntask.Command;


/**
 * Used for executing svn info
 *
 * Available properties to set are:
 * 	revisionProperty = svn.info.revision
 *  urlProperty = svn.info.url
 *  repositoryRootUrlProperty = svn.info.repositoryRootUrl
 *  authorProperty = svn.info.author
 *  committedDateProperty = svn.info.committedDate
 *  committedRevision = svn.info.committedRevision
 *  committedDateFormatPattern
 *
 * @author jonstevens
 */
public class Info extends Command
{
	public static final String SVN_INFO_URL = "svn.info.url";
	public static final String SVN_INFO_REPOSITORY_ROOT_URL = "svn.info.repositoryRootUrl";
	public static final String SVN_INFO_REVISION = "svn.info.revision";
	public static final String SVN_INFO_AUTHOR = "svn.info.author";
	public static final String SVN_INFO_COMMITTED_DATE = "svn.info.committedDate";
	public static final String SVN_INFO_COMMITTED_REVISION = "svn.info.committedRevision";

	private String path;
	private String committedDateFormatPattern;
	private String revisionProperty;
	private String urlProperty;
	private String repositoryRootUrlProperty;
	private String authorProperty;
	private String committedDateProperty;
	private String committedRevisionProperty;

	/** */
	@Override
	public void execute() throws Exception
	{
		File filePath = new File(this.path);
		DateFormat format = null;
		if (committedDateFormatPattern != null) {
			format = new SimpleDateFormat(committedDateFormatPattern);
		} else {
			format = DateFormat.getInstance();
		}
		this.getTask().log("info " + filePath.getCanonicalPath());

		// Set the default property in ant in case we have an exception below.
		this.getProject().setProperty(this.revisionProperty, "-1");

		// Get the WC Client
		SVNWCClient client = this.getTask().getSvnClient().getWCClient();

		// Execute svn info
		SVNInfo info = client.doInfo(filePath, SVNRevision.WORKING);

		// Get the interesting info data
		SVNRevision revision = info.getRevision();
		SVNRevision committedRevision = info.getCommittedRevision();
		SVNURL url = info.getURL();
		SVNURL repositoryRootUrl = info.getRepositoryRootURL();
		String author = info.getAuthor();
		Date committedDate = info.getCommittedDate();

		// Set the computed properties in ant
		this.getProject().setProperty(this.revisionProperty, new Long(revision.getNumber()).toString());
		this.getProject().setProperty(this.urlProperty, url.toDecodedString());
		this.getProject().setProperty(this.repositoryRootUrlProperty, repositoryRootUrl.toDecodedString());
		this.getProject().setProperty(this.authorProperty, author);
		this.getProject().setProperty(this.committedDateProperty, format.format(committedDate));
		this.getProject().setProperty(this.committedRevisionProperty, new Long(committedRevision.getNumber()).toString());
	}

	/** */
	@Override
	protected void validateAttributes() throws Exception
	{
		if (this.path == null)
			throw new Exception("path cannot be null");

		if (this.revisionProperty == null)
			this.revisionProperty = SVN_INFO_REVISION;

		if (this.urlProperty == null)
			this.urlProperty = SVN_INFO_URL;

		if (this.repositoryRootUrlProperty == null)
			this.repositoryRootUrlProperty = SVN_INFO_REPOSITORY_ROOT_URL;

		if (this.authorProperty == null)
			this.authorProperty = SVN_INFO_AUTHOR;

		if (this.committedDateProperty == null)
			this.committedDateProperty = SVN_INFO_COMMITTED_DATE;

		if (this.committedRevisionProperty == null)
			this.committedRevisionProperty = SVN_INFO_COMMITTED_REVISION;
	}

	/**
	 * the date format for the committed date
	 */
	public void setCommittedDateFormatPattern(String dateFormat)
	{
		this.committedDateFormatPattern = dateFormat;
	}
	
	/**
	 * the path to the file
	 */
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

	/**
	 * @param urlProperty The urlProperty to set. defaults to svn.info.url
	 */
	public void setUrlProperty(String urlProperty)
	{
		this.urlProperty = urlProperty;
	}

	/**
	 * @param repositoryRootUrlProperty The repositoryRootUrlProperty to set. defaults to svn.info.repositoryRootUrl
	 */
	public void setRepositoryRootUrlProperty(String repositoryRootUrlProperty)
	{
		this.repositoryRootUrlProperty = repositoryRootUrlProperty;
	}

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

	/**
	 * @param committedRevisionProperty The committedRevisionProperty; to set. defaults to svn.info.committedRevision
	 */
	public void setCommittedRevisionProperty(String committedRevisionProperty)
	{
		this.committedRevisionProperty = committedRevisionProperty;
	}

}
