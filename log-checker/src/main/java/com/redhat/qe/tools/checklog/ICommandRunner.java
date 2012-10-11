package com.redhat.qe.tools.checklog;

import com.redhat.qe.tools.SSHCommandResult;

/**
 * This intefrace defines Command Runner interface. 
 * @author lzoubek@redhat.com
 *
 */
public interface ICommandRunner {

	/**
	 * runs a command
	 * @param command
	 * @return returns command result
	 */
	SSHCommandResult runAndWait(String command);
	/**
	 * disconnects (useful for remote runner implementations)
	 */
	void disconnect();
	/**
	 * connects (useful for remote runner implementations)
	 */
	void connect();
}
