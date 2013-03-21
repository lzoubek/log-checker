package com.redhat.qe.tools.checklog.test;

import org.testng.annotations.AfterSuite;

import com.redhat.qe.tools.checklog.CheckLog;
import com.redhat.qe.tools.checklog.ICommandRunner;
import com.redhat.qe.tools.checklog.LocalCommandRunner;
import com.redhat.qe.tools.checklog.LogFile;
import com.redhat.qe.tools.checklog.RemoteCommandRunner;

/**
 * check child classes for more tests/examples. Test methods that end with fail() are supposed to fail.
 * It's also supposed that there is user upload with password upload on your local machine
 * @author lzoubek@redhat.com
 *
 */
// we define our local and remote logfiles in the base-class
@CheckLog(enabled=false,logs={
	@LogFile(id="1",logFile="${user.home}/test1.log",grabMe=false), // - should not attempt to be grabbed (check test output)
	@LogFile(id="2",logFile="${env:HOME}/test2.log"),
	@LogFile(id="3",logFile="test3.log",user="upload",host="localhost",pass="upload"),
	@LogFile(id="4",logFile=BaseTest.test4Log), // not cleaned - must be grabbed as the only one
})
public class BaseTest {

	protected static String test1Log = "$HOME/test1.log";
	protected static String test2Log = "$HOME/test2.log";
	protected static String test3Log = "test3.log";
	protected static final String test4Log = "/tmp/test4.log";
	protected static ICommandRunner local = new LocalCommandRunner();
	protected static ICommandRunner remote = new RemoteCommandRunner("upload", "localhost", "upload", null);

	@AfterSuite
	public void cleanupFiles() {
		local.connect();
		local.runAndWait("rm -rf "+test1Log);
		local.runAndWait("rm -rf "+test2Log);
		local.runAndWait("echo \"this is the only file to be grabbed at the end\" > "+test4Log);
		local.disconnect();
		remote.connect();
		remote.runAndWait("rm -rf "+test3Log);
		remote.disconnect();
	}
}


