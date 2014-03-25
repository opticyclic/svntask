package com.googlecode.svntask.command;

import java.io.File;

import com.googlecode.svntask.Command;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

/**
 * svn export.
 * Exports a clean directory tree from the remote url by default or
 * Exports a clean directory tree from the working copy
 *
 * @author opticyclic
 */
public class Export extends Command {
  /**
   * the source url
   */
  private SVNURL url = null;
  private File workingCopy;
  private File exportPath;
  private SVNRevision pegRevision = SVNRevision.WORKING;
  private SVNRevision revision = SVNRevision.WORKING;
  private SVNDepth depth = SVNDepth.INFINITY;
  private boolean force = false;
  private String user;
  private String password;

  @Override
  public void execute() throws Exception {
    SVNClientManager cm = getTask().getSvnClient();

    if(user != null && password != null) {
      ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(user, password);
      cm.setAuthenticationManager(authManager);
    }

    // Get the Update Client
    SVNUpdateClient updateClient = cm.getUpdateClient();
    long rev = 0;
    if(url != null) {
      rev = updateClient.doExport(url, exportPath, pegRevision, revision, null, force, depth);
    } else {
      rev = updateClient.doExport(workingCopy, exportPath, pegRevision, revision, null, force, depth);
    }
    getTask().log("Exported revision " + rev + " to " + exportPath, Project.MSG_INFO);
  }

  @Override
  protected void validateAttributes() throws Exception {
    if(url != null && workingCopy != null) {
      getTask().log("Ignoring working copy and exporting from repo", Project.MSG_WARN);
    }

    if(exportPath == null) {
      throw new BuildException("Destintaion path must be provided to export into");
    }
  }

  public void setUrl(String url) {
    try {
      this.url = SVNURL.parseURIDecoded(url);
    } catch(SVNException e) {
      throw new BuildException(e);
    }
  }

  public void setWorkingCopy(File workingCopy) {
    this.workingCopy = workingCopy;
  }

  public void setExportPath(File exportPath) {
    this.exportPath = exportPath;
  }

  public void setPegRevision(long pegRevision) {
    this.pegRevision = SVNRevision.create(pegRevision);
  }

  public void setRevision(long revision) {
    this.revision = SVNRevision.create(revision);
  }

  public void setDepth(String depth) {
    this.depth = SVNDepth.fromString(depth);
  }

  /**
   * @param force the force to set
   */
  public void setForce(boolean force) {
    this.force = force;
  }

  /** */
  public void setUsername(String user) {
    this.user = user;
  }

  /** */
  public void setPassword(String pass) {
    this.password = pass;
  }

}
