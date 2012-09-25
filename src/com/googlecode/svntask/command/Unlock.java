package com.googlecode.svntask.command;

import java.io.File;
import java.util.Vector;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.types.FileSet;
import org.tmatesoft.svn.core.wc.SVNWCClient;

import com.googlecode.svntask.Command;

/**
 * From <a href="https://github.com/chripo/svntask/">https://github.com/chripo/svntask/</a>
 *
 * @author christoph polcin (christoph-polcin.com)
 */
public class Unlock extends Command {
	private boolean breakLock = true;
	private boolean includeDirs = true;
	private Vector<FileSet> filesets = new Vector<FileSet>();

	@Override
	public void execute() throws Exception {
		SVNWCClient wcl = getTask().getSvnClient().getWCClient();
		this.getTask().log("Unlock");

		DirectoryScanner ds;
		File dir;
		String[] filesInSet;
		for (FileSet fileset : filesets) {
			ds = fileset.getDirectoryScanner(getProject());
			dir = ds.getBasedir();
			filesInSet = ds.getIncludedFiles();
			if(includeDirs){
				String[] dirs = ds.getIncludedDirectories();
				String[] temp = new String[filesInSet.length + dirs.length];
				System.arraycopy(filesInSet, 0, temp, 0, filesInSet.length);
				System.arraycopy(dirs, 0, temp, filesInSet.length, dirs.length);
				filesInSet = temp;
			}

			if(filesInSet.length > 0){
				int pos = 0;
				File[] paths = new File[filesInSet.length];
				for (String filename : filesInSet)
					paths[pos++] = new File(dir, filename).getCanonicalFile();

				wcl.doUnlock(paths, breakLock);
			}
		}
	}

	@Override
	protected void validateAttributes() throws Exception {
		if(this.filesets.size() == 0)
			throw new BuildException("add some filesets to unlock");
	}

	public void setBreakLock(boolean breakLock) {
		this.breakLock = breakLock;
	}

	/** */
	public void addFileSet(FileSet fileset){
		if(!filesets.contains(fileset))
			filesets.add(fileset);
	}

	/** */
	public void setIncludeDirs(boolean includeDirs) {
		this.includeDirs = includeDirs;
	}
}
