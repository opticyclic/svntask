package com.googlecode.svntask.command;

import java.io.File;

import org.apache.tools.ant.BuildException;

import com.googlecode.svntask.Command;

/**
 * From <a href="https://github.com/chripo/svntask/">https://github.com/chripo/svntask/</a>
 *
 * @author christoph polcin (christoph-polcin.com)
 */
public class Cleanup extends Command {
	private String path;
	private boolean deleteWCProperties;

	@Override
	public void execute() throws Exception {
		this.getTask().log("cleanup");
		this.getTask().getSvnClient().getWCClient().doCleanup(new File(this.path).getCanonicalFile(), deleteWCProperties);
	}

	@Override
	protected void validateAttributes() throws Exception {
		if(this.path == null)
			throw new BuildException("set a starting path");
	}

	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @param deleteWCProperties boolean - if true, removes DAV specific "svn:wc:" properties from the working copy
	 */
	public void setDeleteWCProperties(boolean deleteWCProperties) {
		this.deleteWCProperties = deleteWCProperties;
	}
}
