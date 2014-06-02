package com.googlecode.svntask.command;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Collection;

import com.googlecode.svntask.Command;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNDiffClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

/**
 * svn diff.
 * Diff between working copy and head
 *
 * @author opticyclic
 */
public class Diff extends Command {

  private File workingCopy;
  private String user;
  private String password;
  private String outFileName = "svn.patch";

  @Override
  public void execute() throws Exception {
    SVNClientManager cm = getTask().getSvnClient();

    if(user != null && password != null) {
      ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(user, password);
      cm.setAuthenticationManager(authManager);
    }

    // Get the Diff Client
    SVNDiffClient diffClient = cm.getDiffClient();

    //Save output at root
    File workingCopyRoot = SVNWCUtil.getWorkingCopyRoot(workingCopy, false);
    File svnDiffFile = new File(workingCopyRoot, outFileName);
    svnDiffFile.createNewFile();
    OutputStream outputStream = new FileOutputStream(svnDiffFile);

    boolean useAncestry = false;
    Collection changelist = null;

    //Use the pegged version of doDiff() which runs diff on different revisions of the same tree (or file).
    //The 2nd parameter (SVNRevision.UNDEFINED) denotes that we do not provide any special peg revision. This will automatically default to the HEAD revision.
    //Then we provide SVNRevision.WORKING and SVNRevision.HEAD as we want to diff working tree with the one located in the repository at HEAD revision.
    //SVNDepth.INFINITY says that want to diff the entire tree recursively.
    //useAncestry flag - controls whether or not items being diffed will be checked for relatedness first.
    // Unrelated items are typically transmitted to the editor as a deletion of one thing and the addition of another, but if this flag is true, unrelated items will be diffed as if they were related.
    //outputStream - this is the diff output receiver stream.
    //diff on the entire tree, not just on members of some changelist (in case we had one) so pass null.
    diffClient.doDiff(workingCopy, SVNRevision.UNDEFINED, SVNRevision.WORKING, SVNRevision.HEAD, SVNDepth.INFINITY, useAncestry, outputStream, changelist);

    getTask().log("Diff file is at: " + svnDiffFile.getAbsolutePath(), Project.MSG_INFO);
  }

  @Override
  protected void validateAttributes() throws Exception {
    //ToDo:Validation!
  }

  public void setOutFileName(String outFileName) {
    this.outFileName = outFileName;
  }

  public void setWorkingCopy(File workingCopy) {
    this.workingCopy = workingCopy;
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
