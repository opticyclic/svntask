package com.googlecode.svntask.command;

import java.io.File;
import java.util.Vector;

import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.types.FileSet;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.wc.SVNWCClient;

import com.googlecode.svntask.Command;

/**
 * Implements the "add" command. Requires a "path" argument indicating the path
 * to add. Has optional boolean arguments "force", "mkdir",
 * "climbUnversionedParents", "includeIgnored" and "makeParents". Example: {@code <svn><add
 * path="." force="false" /></svn>}
 *
 * @author darren (darren@bizo.com)
 * @author Jon Stevens
 * @author christoph polcin (christoph-polcin.com)
 */
public class Add extends Command
{
	private String path;
	private boolean force = true;
	private boolean mkdir;
	private boolean climbUnversionedParents = true;
	private SVNDepth depth = SVNDepth.INFINITY;
	private boolean includeIgnored;
	private boolean makeParents = true;
	private Vector<FileSet> filesets = new Vector<FileSet>();


	/** */
	@Override
	public void execute() throws Exception
	{
		File file;
		SVNWCClient wcClient = this.getTask().getSvnClient().getWCClient();

		if(path != null){
			file = new File(path).getCanonicalFile();

			this.getTask().log("add " + file.getPath());
			wcClient.doAdd(file, this.force, this.mkdir, this.climbUnversionedParents, this.depth, this.includeIgnored, this.makeParents);
		} else {
			DirectoryScanner ds;
			File dir;
			String[] filesInSet;
			for (FileSet fileset : filesets) {
				ds = fileset.getDirectoryScanner(getProject());
				dir = ds.getBasedir();
				filesInSet = ds.getIncludedFiles();
				for (String filename : filesInSet) {
					file = new File(dir,filename).getCanonicalFile();
					this.getTask().log("add " + file.getCanonicalPath());
					wcClient.doAdd(file, this.force, this.mkdir, this.climbUnversionedParents, this.depth, this.includeIgnored, this.makeParents);
				}
			}
		}
	}

	/** */
	@Override
	protected void validateAttributes() throws Exception
	{
		if (this.filesets.size() == 0 && (this.path == null || this.path.length() == 0) )
			throw new NullPointerException("add task: need a path or fileset");
	}

	/** */
	public void setPath(String path)
	{
		this.path = path;
	}

	/** */
	public void setForce(boolean force)
	{
		this.force = force;
	}

	/** */
	public void setMkdir(boolean mkdir)
	{
		this.mkdir = mkdir;
	}

	/** */
	public void setClimbUnversionedParents(boolean climbUnversionedParents)
	{
		this.climbUnversionedParents = climbUnversionedParents;
	}

	/** */
	public void setDepth(String depth)
	{
		this.depth = SVNDepth.fromString(depth);
	}

	/** */
	public void setIncludeIgnored(boolean includeIgnored)
	{
		this.includeIgnored = includeIgnored;
	}

	/** */
	public void setMakeParents(boolean makeParents)
	{
		this.makeParents = makeParents;
	}

	public void addFileSet(FileSet fileset){
		if(!filesets.contains(fileset))
			filesets.add(fileset);
	}
}
