package com.googlecode.svntask.command;

import java.io.File;
import java.util.Vector;

import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.types.FileSet;
import org.tmatesoft.svn.core.wc.SVNWCClient;

import com.googlecode.svntask.Command;

/**
 * From <a href="https://github.com/chripo/svntask/">https://github.com/chripo/svntask/</a>
 *
 * @author christoph polcin (christoph-polcin.com)
 */
public class Delete extends Command {
	private String path;
	private boolean force = true;
	private boolean deleteFiles;
	private boolean includeDirs = true;
	private boolean dryRun;
	private Vector<FileSet> filesets = new Vector<FileSet>();

	@Override
	public void execute() throws Exception {
		File file;
		SVNWCClient wcClient = this.getTask().getSvnClient().getWCClient();

		if(path != null){
			file = new File(path).getCanonicalFile();
			this.getTask().log("delete " + file.getPath());
			wcClient.doDelete(file, this.force, this.deleteFiles, this.dryRun);
		} else {
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
				for (String filename : filesInSet) {
					file = new File(dir, filename).getCanonicalFile();
					this.getTask().log("delete " + file.getPath());
					wcClient.doDelete(file, this.force, this.deleteFiles, this.dryRun);
				}
			}
		}
	}

	@Override
	protected void validateAttributes() throws Exception {
		if(this.filesets.size() == 0 && (this.path == null || this.path.length() == 0))
			throw new NullPointerException("delete task: path cannot be null");
	}

	/** */
	public void setPath(String path){
		this.path = path;
	}

	/** */
	public void setForce(boolean force){
		this.force = force;
	}

	/** */
	public void setDeleteFiles(boolean deleteFiles){
		this.deleteFiles = deleteFiles;
	}

	/** */
	public void setDryRun(boolean dryRun){
		this.dryRun = dryRun;
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
