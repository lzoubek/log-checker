log-checker
===========

TestNG Listener with annotations for checking local and remote log files. This project might be useful,
when you run integration tests using [testng](http://testng.org) especially when testing complex systems
that run on different hosts.

log-checker helps you to implement additional validation of your .log files on your systems. 

## Features
 * additional log file checks using annotations on class or method level
 * checks log files on local box or remote (using SSH)
 * checks multiple log files for one test method
 * java system properties or environment variables can be used as placeholders in annotation strings

## How does it work?
 * before test method runs, a testng listener checks for annotations and for each @LogFile remembers number of lines
 * after test method finished, listener checks each @Logfile again to see the difference. If something was appended to logfile
and there is a match on failOn expression, method is marked as FAILED.

## Example

We define 3 log files on class level. This means all 3 log files are going to be checked for every @Test method.

Test method mytest2 is a little bit special. It referenes @LogFile definition from class level annotation and alters its configuration. 

```
@CheckLog(
	logs={
		// we define path to log file using system property and seek for ERROR lines
		@LogFile(id="local",logFile="${user.home}/test1.log",failOn="ERROR"),
		// we define path to log file using environment variable and seek for ERROR lines but we do not care about NullPointer
		@LogFile(id="local2",logFile="${env:HOME}/test2.log",failOn="ERROR",ignore="NullPointer"),
		// we define remote log file 
		@LogFile(id="remote",logFile="test3.log",user="joe",host="example.host",pass="${example.secret}")
	}
)
public class MyTest {

	@Test
	public void myTest() {
	
	}
	// for this test we do not want to ignore NullPointer and we don't want to check other log files
	@CheckLog(logs=@LogFile(refId="local2",ignore=""))
	@Test
	public void myTest2() {

	}
}
```
For more examples check `log-checker-test` project. Note that if you run tests on this project, test methods that end `fail()` are supposed to fail
