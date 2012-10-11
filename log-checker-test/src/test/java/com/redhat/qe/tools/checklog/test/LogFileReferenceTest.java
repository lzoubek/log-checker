package com.redhat.qe.tools.checklog.test;

import org.testng.annotations.Test;

import com.redhat.qe.tools.checklog.CheckLog;
import com.redhat.qe.tools.checklog.LogFile;

@CheckLog(logs={
		@LogFile(refId="1")
})
public class LogFileReferenceTest extends BaseTest {

	@Test
	public void referencedByClassFail() {
		local.connect();
		local.runAndWait("echo \" ERROR - this is expected failure\" >> "+test1Log);
		local.runAndWait("echo \" ERROR - this is UNEXPECTED failure\" >> "+test2Log);
		local.disconnect();
	}
	@Test
	@CheckLog(logs={
			@LogFile(refId="2")
	})
	public void referencedByMethodFail() {
		local.connect();
		local.runAndWait("echo \" ERROR - this is expected failure\" >> "+test2Log);
		local.runAndWait("echo \" ERROR - this is UNEXPECTED failure\" >> "+test1Log);
		local.disconnect();
	}
	@Test
	@CheckLog(logs={
			@LogFile(refId="1",logFile="${env:HOME}/test4.log")
	})
	public void referencedByMethodOverriddenLogFileFail() {
		local.connect();
		local.runAndWait("echo \" ERROR - this is expected failure\" >> $HOME/test4.log");
		local.disconnect();
	}
	@Test
	@CheckLog(enabled=false,
	logs={
			@LogFile(refId="1",logFile="${env:HOME}/test4.log")
	})
	public void referencedButDisabled() {
		local.connect();
		local.runAndWait("echo \" ERROR - this is UNEXPECTED failure\" >> $HOME/test4.log");
		local.disconnect();
	}
	@Test
	@CheckLog(
			logs={@LogFile(refId="999")}
	)
	public void referencedInvalidLogFile() {
		
	}
	@Test
	@CheckLog
	public void emptyAnnotation() {
		
	}

}
