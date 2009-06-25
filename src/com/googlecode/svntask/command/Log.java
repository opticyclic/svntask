package com.googlecode.svntask.command;

import java.io.File;
import java.util.Iterator;

import org.tmatesoft.svn.core.ISVNLogEntryHandler;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.wc.SVNLogClient;
import org.tmatesoft.svn.core.wc.SVNRevision;

import com.googlecode.svntask.Command;

/**
 * Used for executing svn log. Output is similar to command line client except
 * that it doesn't (yet) include the number of lines that have changed in the
 * revision and the date format is different.
 *
 * Includes these optional params:
 * startRevision: revision number or "HEAD" or "BASE" (defaults to "HEAD")
 * endRevision: revision number or "HEAD" or "BASE" (defaults to "BASE")
 * limit: max number of log entries to get, 0 for all (defaults to 0)
 * stopOnCopy: do we stop on copy? (defaults to false)
 * discoverChangedPaths: do we report of all changed paths for every revision being processed? (defaults to false)
 *
 * @author rayvanderborght
 */
public class Log extends Command
{
	/** */
	public static final String SVN_LOG = "svn.log";

	private static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n");
	private static final String ITEM_SEPARATOR = "----------------------------------------------------------------------------";

	private String path;
	private String logProperty;
	private String startRevision;
	private String endRevision;
	private long limit;
	private boolean stopOnCopy;
	private boolean discoverChangedPaths;

	/** */
	@Override
	public void execute() throws Exception
	{
		File filePath = new File(this.path);

		SVNLogClient client = this.getTask().getSvnClient().getLogClient();
		SVNRevision start = SVNRevision.parse(this.startRevision);
		SVNRevision end = SVNRevision.parse(this.endRevision);

		this.getTask().log("log " + filePath.getCanonicalPath() + " " + start.toString() + ":" + end.toString());

		final StringBuilder logBuffer = new StringBuilder(1024);
		client.doLog(new File[] { filePath }, start, end, this.stopOnCopy, this.discoverChangedPaths, this.limit, new ISVNLogEntryHandler()
		{
			@Override
			public void handleLogEntry(SVNLogEntry entry) throws SVNException
			{
				logBuffer.append(ITEM_SEPARATOR).append(LINE_SEPARATOR)
					.append('r').append(entry.getRevision()).append(" | ")
					.append(entry.getAuthor()).append(" | ")
					.append(entry.getDate()).append(LINE_SEPARATOR)
					.append(entry.getMessage()).append(LINE_SEPARATOR);

				if (Log.this.discoverChangedPaths)
				{
					logBuffer.append("Changed paths:").append(LINE_SEPARATOR);
		            for (Iterator<?> paths = entry.getChangedPaths().values().iterator(); paths.hasNext(); )
		            {
		                SVNLogEntryPath path = (SVNLogEntryPath) paths.next();
		                logBuffer.append(path.toString()).append(LINE_SEPARATOR);
		            }
				}
			}
		});

		logBuffer.append(ITEM_SEPARATOR + LINE_SEPARATOR);

		this.getProject().setProperty(this.logProperty, logBuffer.toString());
	}

	/** */
	@Override
	protected void validateAttributes() throws Exception
	{
		if (this.path == null)
			throw new NullPointerException("path cannot be null");

		if (this.logProperty == null)
			this.logProperty = SVN_LOG;

		if (this.startRevision == null)
			this.startRevision = "HEAD";

		if (this.endRevision == null)
			this.endRevision = "BASE";

		if (this.limit < 0)
			this.limit = 0;
	}

	/**
	 * the path to the file
	 */
	public void setPath(String path)
	{
		this.path = path;
	}

	/**
	 * the starting revision
	 */
	public void setStartRevision(String startRevision)
	{
		this.startRevision = startRevision;
	}

	/**
	 * the ending revision
	 */
	public void setEndRevision(String endRevision)
	{
		this.endRevision = endRevision;
	}

	/**
	 * the max number of log messages to return
	 * must be positive.  use 0 for no limit
	 */
	public void setLimit(long limit)
	{
		this.limit = limit;
	}

	/**
	 * should svn logging stop on copy
	 */
	public void setStopOnCopy(boolean stopOnCopy)
	{
		this.stopOnCopy = stopOnCopy;
	}

	/**
	 * set to true to report of all changed paths for every revision being processed
	 */
	public void setDiscoverChangedPaths(boolean discoverChangedPaths)
	{
		this.discoverChangedPaths = discoverChangedPaths;
	}

	/**
	 * the log output goes here
	 */
	public void setLogProperty(String logProperty)
	{
		this.logProperty = logProperty;
	}
}
