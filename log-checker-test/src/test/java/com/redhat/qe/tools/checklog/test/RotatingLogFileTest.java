package com.redhat.qe.tools.checklog.test;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.redhat.qe.tools.checklog.CheckLog;
import com.redhat.qe.tools.checklog.LogFile;

public class RotatingLogFileTest extends BaseTest {

	@BeforeClass
	public void beforeClass() {
		local.connect();
		for (int i=0;i<10;i++) {
			local.runAndWait("echo \" ERROR - this is an UNEXPECTED message to be found\" >>  /tmp/test5.log");
		}
		local.disconnect();
	}
	
	@Test
	@CheckLog(logs={
			@LogFile(logFile="/tmp/test5.log",grabMe=false)
	})
	public void rotatingLogFileFail() {
		local.connect();
		local.runAndWait("echo \" ERROR - this is an 1 half of expected message to be found\" >>  /tmp/test5.log");
		local.runAndWait("mv /tmp/test5.log /tmp/test5.log.1");
		local.runAndWait("echo \" ERROR - this is an 2nd half of expected message to be found\" >>  /tmp/test5.log");
		local.disconnect();
	}
}
