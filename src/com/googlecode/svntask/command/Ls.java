package com.googlecode.svntask.command;

import java.util.Collection;

import org.apache.tools.ant.BuildException;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import com.googlecode.svntask.Command;

/**
 * Used for executing ls update. Defaults delimeter character to ','.
 *
 * Returns the concat of each name plus the delimeter char in the "svn.ls" property.
 *
 * @author scotthernandez
 */
public class Ls extends Command
{
	protected String path;
	protected SVNRevision revision = SVNRevision.HEAD;
	protected Character delimeter = ',';
	protected String repo;
	protected String user;
	protected String password;
	protected SVNNodeKind filterType = null;
	protected String propertyName = "svn.ls";
	private String filterContains;

	/** */
	@SuppressWarnings("unchecked")
	@Override
	public void execute() throws Exception
	{
		this.getTask().log("ls " + this.repo + ":" + this.path);
		try
		{
			SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIDecoded(this.repo));

			if (this.user != null)
			{
				ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(this.user,
						this.password);
				repository.setAuthenticationManager(authManager);
			}

			Collection<SVNDirEntry> lsResults = repository.getDir(this.path, this.revision.getNumber() , null, (Collection<?>) null);

			StringBuilder sb = new StringBuilder();

			boolean firstTime = true;
			for (SVNDirEntry dirEntry : lsResults)
			{
				if ((this.filterContains != null && !dirEntry.getName().contains(this.filterContains)))
					continue;

				if ((this.filterType != null && !this.filterType.equals(dirEntry.getKind())))
					continue;

				if (firstTime)
					firstTime = !firstTime;
				else
					sb.append(this.delimeter);

				sb.append(dirEntry.getName());
			}

			this.getProject().setProperty(this.propertyName, sb.toString());
		}
		catch (SVNException e)
		{
			throw new BuildException(e);
		}
	}

	/** */
	@Override
	protected void validateAttributes() throws Exception
	{
		if (this.repo == null)
			throw new Exception("repo (repository) cannot be null");
		if (this.path == null)
			throw new Exception("path cannot be null");
	}

	/** */
	public void setFilterType(String type)
	{
		if ("file".equals(type.toLowerCase()))
			this.filterType = SVNNodeKind.FILE;
		else if ("dir".equals(type.toLowerCase()))
			this.filterType = SVNNodeKind.DIR;
		else if ("none".equals(type.toLowerCase()))
			this.filterType = SVNNodeKind.NONE;
	}

	/** */
	public void setFilterContains(String contains)
	{
		this.filterContains = contains;
	}

	/** */
	public void setPassword(String pass)
	{
		this.password = pass;
	}

	/** */
	public void setUsername(String user)
	{
		this.user = user;
	}

	/** */
	public void setRepository(String repo)
	{
		this.repo = repo;
	}

	/** */
	public void setPath(String path)
	{
		//strip the first slash, caused error: org.tmatesoft.svn.core.SVNException: svn: '/svn/!svn/bc/0/
		if (path.startsWith("/"))
			path = path.substring(1);
		this.path = path;
	}

	/** */
	public void setDelimeter(Character del)
	{
		this.delimeter = del;
	}

	/** */
	public void setRevision(long revision)
	{
		this.revision = SVNRevision.create(revision);
	}

	/** */
	public void setProperty(String propName)
	{
		this.propertyName = propName;
	}
}