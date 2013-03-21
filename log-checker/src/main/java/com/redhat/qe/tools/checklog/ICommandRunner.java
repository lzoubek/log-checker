package com.redhat.qe.tools.checklog;

import java.io.IOException;

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
	/**
	 * gets a file from source and puts it to destination
	 * @param source file name
	 * @param destination file name - must be ABSOLUTE
	 */
	void getFile(String source, String destination) throws IOException;
}
