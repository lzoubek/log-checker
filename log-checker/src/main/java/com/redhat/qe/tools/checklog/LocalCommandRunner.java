package com.redhat.qe.tools.checklog;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import com.redhat.qe.tools.SSHCommandResult;
/**
 * this class is a local command runner (currently works on linux only) that runs all commands locally
 * works by default in current user's home directory!
 * @author lzoubek@redhat.com
 *
 */
public class LocalCommandRunner implements ICommandRunner {

	protected static final Logger log = Logger
			.getLogger(LocalCommandRunner.class.getName());

	private final String workDir;
	public LocalCommandRunner() {
		this(System.getProperty("user.home"));
	}
	public LocalCommandRunner(String workDir) {
		this.workDir = workDir;
		log.fine("Creating local command runner");
	}

	@Override
	public SSHCommandResult runAndWait(String command) {
		log.fine(command);
		SSHCommandResult result = new SSHCommandResult(-1, "", "");
		try {
			String[] cmd = new String[] {"/bin/sh","-c",command};
			final Process p = Runtime.getRuntime().exec(cmd,null,new File(workDir));
			final StringBuilder output = new StringBuilder("");
			final StringBuilder error = new StringBuilder("");
			
			new Runnable() {

				@Override
				public void run() {
					String line;
					BufferedReader input = new BufferedReader(
							new InputStreamReader(p.getInputStream()));
					try {
						while ((line = input.readLine()) != null) {
							output.append(line+"\n");
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						input.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}.run();

			new Runnable() {

				@Override
				public void run() {
					String line;
					BufferedReader input = new BufferedReader(
							new InputStreamReader(p.getErrorStream()));
					try {
						while ((line = input.readLine()) != null) {
							error.append(line+"\n");
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						input.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}.run();
			p.waitFor();
			result = new SSHCommandResult(p.exitValue(), output.toString(), error.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public void disconnect() {

	}

	@Override
	public void connect() {
		// TODO Auto-generated method stub
		
	}
}
