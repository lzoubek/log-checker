package com.redhat.qe.tools.checklog.test;

import org.testng.annotations.Test;

import com.redhat.qe.tools.checklog.CheckLog;
import com.redhat.qe.tools.checklog.LogFile;

public class RemoteLogFileTest extends BaseTest {

	@Test
	@CheckLog(logs=@LogFile(refId="3"))
	public void remoteLogFileFail() {
		remote.connect();
		remote.runAndWait("echo \" ERROR - this is expected failure\" >> "+test3Log);
		remote.disconnect();
	}
	@Test
	@CheckLog(
			logs=@LogFile(user="abcd",host="localhost",pass="nopassword")
			)
	public void invalidCredentials() {
		
	}
	@Test
	@CheckLog(
			logs=@LogFile(user="abcd",host="localhost",pass="nopassword",keyfile="/tmp/foo.pem")
			)
	public void nonexistingKeyfile() {
		
	}
}
