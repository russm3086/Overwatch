/**
 * 
 */
package com.ansys.cluster.monitor.settings;

import com.russ.util.settings.AbstractArguments;

/**
 * @author rmartine
 *
 */
public abstract class ArgsSettings extends AbstractArguments {

	/**
	 * @param args
	 */
	public ArgsSettings(String[] args) {
		super(args);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.russ.util.settings.Arguments#skipMainProgram()
	 */
	@Override
	public boolean skipMainProgram() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.russ.util.settings.Arguments#hasPropFiles()
	 */
	@Override
	public boolean hasPropFiles() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.russ.util.settings.Arguments#getHelpMessage()
	 */
	@Override
	protected String getHelpMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPropFiles() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Verify if the help argument has been given.
	 * 
	 * @return {@code true} if the help argument has been given.
	 */
	public boolean hasHelp(String helpProperties) {

		return exist(helpProperties);

	}

}
