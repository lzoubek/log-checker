package com.redhat.qe.tools.checklog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * this class provides access to any log file using either SSH or local fs
 * @author lzoubek@redhat.com
 *
 */
public class LogFileReader {
	
	protected static final Logger log = Logger.getLogger(LogFileReader.class.getName());
	private final String logFile;
	private final ICommandRunner client;
	private int startLine = -1;
	private final String user;
	private final String host;
	private String filter = null;
	private String ignore = null;
	/**
	 * creates new instance of RemoteLogAccess
	 * @throws IOException 
	 */
	public LogFileReader(String user, String host, String pass, String key, String logFile) throws IOException {
		this.logFile = logFile;
		this.host = host;
		this.user = user;
		ICommandRunner runner;
		if (user==null || host==null || "".equals(user) || "".equals(host)) {
			runner = new LocalCommandRunner();
		}
		else {			
			runner = new RemoteCommandRunner(user, host, pass, key);
		}
		this.client = runner;
		log.fine("Created "+toString());
			
	}

	public void setIgnore(String ignore) {
		this.ignore = ignore;
	}
	public String getIgnore() {
		return ignore;
	}
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
	public String getLogfile() {
		return logFile;
	}
	/**
	 * checks whether agent.log file exists
	 * @return
	 */
	public boolean existsLogFile() {
		return existsFile(getLogfile());
	}
	private boolean existsFile(String file) {
		return client.runAndWait("ls "+file).getExitCode().equals(0);
	}
	private int getLineNumbers(String file) {
		String line = client.runAndWait("cat "+file+" | wc -l").getStdout();
		try 
		{
			return Integer.parseInt(line.trim());
		}
		catch (Exception ex) {
			throw new RuntimeException("Unable to parse line number count of agent log "+getLogfile(),ex);
		}
	}
	/**
	 * starts watching log file {@link LogFileReader#getLogfile()}
	 */
	public void watch() {
		this.startLine = getLineNumbers(getLogfile());
	}
	public void disconnect() {
		client.disconnect();
	}
	/**
	 * returns content of <b>agent.log</b> since {@link LogFileReader#watch()} 
	 * or {@link LogFileReader#getContent()} was called. Note that calling this also calls {@link LogFileReader#watch()}
	 * so you get only appended content
	 * @return
	 */
	public String getContent() {
		return getContent(null,null);
	}
	/**
	 * returns content of <b>agent.log</b> since {@link LogFileReader#watch()} 
	 * or {@link LogFileReader#getContent()} was called. Note that calling this also calls {@link LogFileReader#watch()}
	 * so you get only appended content
	 * @param grepFilter expression to filter results, if null, grep is not used at all
	 * @param grepIgnore expression to do 2nd level of result filtering - items matching this expression will not be returned
	 * @return
	 */
	public String getContent(String grepFilter, String grepIgnore) {
		if (this.startLine<0) {
			return "";
		}
		String grepCmd = "";
		if (grepFilter!=null && !"".equals(grepFilter)) {
			grepCmd = " | grep "+grepFilter;
		}
		if (grepIgnore!=null && !"".equals(grepIgnore)) {
			grepCmd+=" | grep -v "+grepIgnore;
		}
		int current = getLineNumbers(getLogfile());
		int lines = current - this.startLine;		
		if (lines==0) {
			this.startLine = -1;
			return "";
		}
		if (lines>0) {
			this.startLine = -1;
			return client.runAndWait("tail -n "+lines+" "+getLogfile()+grepCmd).getStdout();
		}
		else {
			// it seems'like log files was rotated, we'll return tail of log.1 + whole log
			String rotatedLog = getLogfile()+".1";
			StringBuilder sb = new StringBuilder();
			if (existsFile(rotatedLog)) {
				current = getLineNumbers(rotatedLog);
				lines = current - this.startLine;
				if (lines>0) {
					sb.append(client.runAndWait("tail -n "+lines+" "+rotatedLog+grepCmd).getStdout());
				}				
			}
			sb.append(client.runAndWait("cat "+getLogfile()+grepCmd).getStdout());
			this.startLine = -1;
			return sb.toString();
		}
	}
	/**
	 * @param filter expression passed to <b>grep</b> tool
	 * @return lines from file {@link LogFileReader#getLogfile()} matching <b>grep</b> param expression since {@link LogFileReader#watch()} was called
	 */
	public List<String> lines(String grep,String grepIgnore) {
		String content = getContent(grep, grepIgnore).trim();
		String[] lines = content.split("\n");
		List<String> ret = new ArrayList<String>();
		for (String line : lines) {
			if (line.length()>0) {
				ret.add(line);
			}
		}
		return ret;
	}

	/**
	 * 
	 * @return lines from file {@link LogFileReader#getLogfile()} matching expression {@link LogFileReader#getFilter()} and 
	 * NOT matching expression {@link LogFileReader#getIgnore()} since {@link LogFileReader#watch()} was called
	 */
	public List<String> filteredLines() {
		return lines(getFilter(),getIgnore());
	}
	@Override
	public String toString() {
		if (client.getClass().equals(LocalCommandRunner.class)) {
			return "["+new File(getLogfile()).getAbsolutePath()+"]";
		}
		return "["+user+"@"+host+":"+getLogfile()+"]";
	}
}
