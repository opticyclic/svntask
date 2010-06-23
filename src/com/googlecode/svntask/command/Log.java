package com.googlecode.svntask.command;

import java.io.File;
import java.util.Iterator;

import org.tmatesoft.svn.core.ISVNLogEntryHandler;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.SVNLogClient;
import org.tmatesoft.svn.core.wc.SVNRevision;

import com.googlecode.svntask.Command;

/**
 * Used for executing svn log. Output is similar to command line client except
 * that it doesn't (yet) include the number of lines that have changed in the
 * revision and the date format is different.
 *
 * Includes these optional params:
 * url: use a svn url rather than a path to a working copy
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

    private String url;
    private String path;
    private String logProperty;
    private String startRevision;
    private String endRevision;
    private long limit;
    private boolean stopOnCopy;
    private boolean discoverChangedPaths;
    private boolean includeMergedRevisions;

    /** */
    @Override
    public void execute() throws Exception
    {
        SVNLogClient client = this.getTask().getSvnClient().getLogClient();
        SVNRevision start = SVNRevision.parse(this.startRevision);
        SVNRevision end = SVNRevision.parse(this.endRevision);

        final StringBuilder logBuffer = new StringBuilder(1024);
        if (this.url == null)
        {
            File filePath = new File(this.path);
            this.getTask().log("log " + filePath.getCanonicalPath() + " " + start.toString() + ":" + end.toString());
            client.doLog(new File[] { filePath }, end, start, end, this.stopOnCopy, this.discoverChangedPaths, this.includeMergedRevisions, this.limit, new String[] { }, this.getLogEntryHandler(logBuffer));
        }
        else
        {
            String path = (this.path == null || "".equals(this.path)) ? "/" : this.path;
            this.getTask().log("log " + this.url + (("/".equals(path)) ? "" : path) + " " + start.toString() + ":" + end.toString());
            client.doLog(SVNURL.parseURIDecoded(this.url), new String[] { path }, end, start, end, this.stopOnCopy, this.discoverChangedPaths, this.includeMergedRevisions, this.limit, new String[] { }, this.getLogEntryHandler(logBuffer));
        }

        logBuffer.append(ITEM_SEPARATOR + LINE_SEPARATOR);

        this.getProject().setProperty(this.logProperty, logBuffer.toString());
    }

    /** */
    private ISVNLogEntryHandler getLogEntryHandler(final StringBuilder logBuffer)
    {
        return new ISVNLogEntryHandler()
        {
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
        };
    }

    /** */
    @Override
    protected void validateAttributes() throws Exception
    {
        if (this.url == null && this.path == null)
            throw new NullPointerException("must specify path or url");

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
    * the svn url to use (as opposed to the path of a working copy)
    */
    public void setUrl(String url)
    {
        this.url = url;
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
    * if true logs will include changes merged in from elsewhere
    */
    public void setIncludeMergedRevisions(boolean includeMergedRevisions)
    {
        this.includeMergedRevisions = includeMergedRevisions;
    }

    /**
    * the log output goes here
    */
    public void setLogProperty(String logProperty)
    {
        this.logProperty = logProperty;
    }
}
