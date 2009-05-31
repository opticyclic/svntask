package com.googlecode.svntask;

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.wc.SVNClientManager;

import com.googlecode.svntask.command.Info;
import com.googlecode.svntask.command.Status;
import com.googlecode.svntask.command.Switch;
import com.googlecode.svntask.command.Update;

/**
 *
 * @author jonstevens
 */
public class SvnTask extends Task
{
	/** */
	private List<Command> commands = new ArrayList<Command>();

	/** */
	private boolean failonerror = false;

	/** */
	private SVNClientManager manager = null;

	/** */
	public void addInfo(Info info)
	{
		this.addCommand(info);
	}

	/** */
	public void addUpdate(Update update)
	{
		this.addCommand(update);
	}

	/** */
	public void addStatus(Status status)
	{
		this.addCommand(status);
	}

	/** */
	public void addSwitch(Switch switchTask)
	{
		this.addCommand(switchTask);
	}

	/** */
	private void addCommand(Command command)
	{
		command.setTask(this);
		this.commands.add(command);
	}

	/** */
	public boolean isFailonerror()
	{
		return this.failonerror;
	}

	/** */
	public void setFailonerror(boolean failonerror)
	{
		this.failonerror = failonerror;
	}

	/** */
	@Override
	public void init() throws BuildException
	{
		super.init();

		try
		{
			this.setupSvnKit();
		}
		catch (Exception e)
		{
			if (this.isFailonerror())
			{
				throw new BuildException(e);
			}
			else
			{
				this.log(e.getMessage());
			}
		}
	}

	/** */
	@Override
	public void execute() throws BuildException
	{
		try
		{
			for (Command command : this.commands)
			{
				command.executeCommand();
			}
		}
		catch (Exception e)
		{
			throw new BuildException(e);
		}
	}

	/** */
	public SVNClientManager getSvnClient()
	{
		return this.manager;
	}

	/**
	 * Initializes the library to work with a repository via different
	 * protocols.
	 */
	private void setupSvnKit()
	{
		/*
		 * For using over http:// and https://
		 */
		DAVRepositoryFactory.setup();
		/*
		 * For using over svn:// and svn+xxx://
		 */
		SVNRepositoryFactoryImpl.setup();

		/*
		 * For using over file:///
		 */
		FSRepositoryFactory.setup();

		/*
		 * Create the client manager with defaults
		 */
		this.manager = SVNClientManager.newInstance();
	}
}
