package com.googlecode.svntask.command;

import java.io.File;
import java.text.DateFormat;
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
 * 
 * @author jonstevens
 */
public class Info extends Command
{
	public static final String SVN_INFO_URL = "svn.info.url";
	public static final String SVN_INFO_REPOSITORY_ROOT_URL = "svn.info.repositoryRootUrl";
	public static final String SVN_INFO_REVISION = "svn.info.revision";
	public static final String SVN_AUTHOR = "svn.info.author";
	public static final String SVN_COMMITTED_DATE = "svn.info.committedDate";

	private String path;
	private String revisionProperty;
	private String urlProperty;
	private String repositoryRootUrlProperty;
	private String authorProperty;
	private String committedDateProperty;
	
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

		// Get the interesting info data
		SVNRevision revision = info.getRevision();
		SVNURL url = info.getURL();
		SVNURL repositoryRootUrl = info.getRepositoryRootURL();
		String author = info.getAuthor();
		Date committedDate = info.getCommittedDate();
		
		// Set the computed properties in ant
		this.getProject().setProperty(revisionProperty, new Long(revision.getNumber()).toString());
		this.getProject().setProperty(urlProperty, url.toDecodedString());
		this.getProject().setProperty(repositoryRootUrlProperty, repositoryRootUrl.toDecodedString());
		this.getProject().setProperty(authorProperty, author);
		this.getProject().setProperty(committedDateProperty, DateFormat.getInstance().format(committedDate));
	}

	@Override
	protected void validateAttributes() throws Exception
	{
		if (revisionProperty == null)
			revisionProperty = SVN_INFO_REVISION;

		if (urlProperty == null)
			urlProperty = SVN_INFO_URL;
		
		if (repositoryRootUrlProperty == null)
			repositoryRootUrlProperty = SVN_INFO_REPOSITORY_ROOT_URL;

		if (authorProperty == null)
			authorProperty = SVN_AUTHOR;

		if (committedDateProperty == null)
			committedDateProperty = SVN_COMMITTED_DATE;

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
}
