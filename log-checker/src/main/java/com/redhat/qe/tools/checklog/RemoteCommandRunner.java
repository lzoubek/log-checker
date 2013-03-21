package com.redhat.qe.tools.checklog;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

import com.redhat.qe.tools.SSHCommandResult;
import com.redhat.qe.tools.SSHCommandRunner;
import com.trilead.ssh2.Connection;
import com.trilead.ssh2.SCPClient;
/**
 * This class is a remote command runner that uses SSH as a transport protocol for executing remote commands 
 * @author lzoubek@redhat.com
 *
 */
public class RemoteCommandRunner implements ICommandRunner {
	protected SSHCommandRunner sshCommandRunner = null;
	protected SCPClient scpClient = null;
	protected Connection connection = null;
	private final String user;
	private final String host;
	private final String pass;
	private final String key;
	protected static final Logger log = Logger.getLogger(RemoteCommandRunner.class.getName());

	public RemoteCommandRunner(String user, String host, String pass, String key) {		
		log.fine("Creating SSHClient that will connect to [" +user+"@"+host+"]");
		this.user = user;
		this.host  = host;
		this.pass = pass;
		this.key = key;
	}
	public String getHost() {
		return host;
	}
	public String getUser() {
		return user;
	}
	public String getPass() {
		return pass;
	}
	/**
	 * connects to SSH server. This method is a good choice if you wish to check your connection settings
	 */
	public void connect(){
		disconnect();
        connection = new Connection(host, 22);
		try {
			connection.connect();
			if (this.key==null || "".equals(this.key)) {				
				connection.authenticateWithPassword(user, pass);
			}
			else {
				connection.authenticateWithPublicKey(user, new File(key), pass);
			}
			sshCommandRunner = new SSHCommandRunner(connection, null);
			scpClient = new SCPClient(connection);
		} catch (IOException e) {
			connection = null;
			throw new RuntimeException("Cannot connect to "+user+"@"+host+" via SSH",e);
		}
	}
	public boolean isConnected() {
		return connection!=null;
	}

	/**
	 * runs given command and waits for return value on SSH server, if client is not connected it will connect automatically, 
	 * don't forget to disconnect ;)
	 * @param command
	 */
	public SSHCommandResult runAndWait(String command) {
		if (!isConnected()) {
			connect();
		}
		log.fine(command);
		return sshCommandRunner.runCommandAndWait(command);
	}
	/**
	 * disconnects from SSH server
	 */
	public void disconnect() {
		if (isConnected()) {
			connection.close();
			connection = null;
		}
	}
	@Override
	public void getFile(String source, String destination) throws IOException {
	    File d = new File(destination);
	    if (!d.isAbsolute()) {
		throw new IOException("Destination file is not absolute");
	    }
	    d.getParentFile().mkdirs();	    
	    if (!isConnected()) {
		connect();
	    }
	    FileOutputStream fos = new FileOutputStream(d);
	    scpClient.get(source, fos);
	    fos.flush();
	    fos.close();	    
	}
	
}
