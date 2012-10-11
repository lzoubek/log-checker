package com.redhat.qe.tools.checklog.test;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.redhat.qe.tools.checklog.CheckLog;
import com.redhat.qe.tools.checklog.LogFile;

public class FilterAndIgnoreTest extends BaseTest {

	@BeforeClass
	public void beforeTest() {
		local.connect();
		local.runAndWait("echo \"\" > "+test1Log);
		local.runAndWait("echo \"\" > "+test2Log);
		local.disconnect();
	}
	
	@Test
	@CheckLog(
			logs=@LogFile(refId="1",failOn="1234")
	)
	public void filterFail() {
		local.connect();
		local.runAndWait("echo \" 1234 - this is expected failure\" >> "+test1Log);
		local.disconnect();
	}
	
	@Test
	@CheckLog(
			logs=@LogFile(refId="1",failOn="abc")
	)
	public void filterSuccess() {
		local.connect();
		local.runAndWait("echo \" xxx - this is NOT expected failure\" >> "+test1Log);
		local.disconnect();
	}
	
	@Test
	@CheckLog(
			logs=@LogFile(refId="1",failOn="abc",ignore="efg")
	)
	public void filteredButIgnored() {
		local.connect();
		local.runAndWait("echo \" abcdefg - this is NOT expected failure\" >> "+test1Log);
		local.disconnect();
	}
	
	@Test
	@CheckLog(
			logs=@LogFile(refId="2",failOn="eee",ignore="rrr")
	)
	public void filteredSomeIgnoredButFail() {
		local.connect();
		local.runAndWait("echo \" eee - this is expected failure\" >> "+test2Log);
		local.runAndWait("echo \" eeerrr - this is NOT expected failure\" >> "+test2Log);
		local.disconnect();
	}
}
