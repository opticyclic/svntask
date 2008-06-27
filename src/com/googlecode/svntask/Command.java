package com.googlecode.svntask;

import org.apache.tools.ant.ProjectComponent;


/**
 * Base class for a Command which represents an svn command 
 * such as svn info.
 * 
 * @author jonstevens
 */
public abstract class Command extends ProjectComponent
{
	private SvnTask task = null;

	public abstract void execute() throws Exception;

	protected abstract void validateAttributes() throws Exception;

	/**
	 * 
	 * @throws Exception
	 */
	public void executeCommand() throws Exception
	{
		try
		{
			this.validateAttributes();
			this.execute();
		}
		catch (Exception e)
		{
			if (this.task.isFailonerror())
			{
				throw new Exception(e);
			}
			else
			{
				this.task.log(e.getMessage());
			}
		}
	}
	
	/**
	 * Sets the SvnTask
	 */
	public void setTask(SvnTask task)
	{
		this.task = task;
	}
	
	/**
	 * Gets the SvnTask
	 */
	public SvnTask getTask()
	{
		return this.task;
	}
}
