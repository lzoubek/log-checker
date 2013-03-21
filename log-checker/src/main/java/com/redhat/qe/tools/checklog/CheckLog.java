package com.redhat.qe.tools.checklog;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.testng.TestNG;

/**
 * This annotation can be on class or method and is processed by {@link CheckLogTestNGListener} listener. 
 * Adding this annotation to class (or even superclass) or method does following:
 * <ol>
 * <li>Before each test method runs, new {@link LogFileReader} instance is created and particular remote log is being watched</li>
 * <li>After test method finishes, gathered output is checked for <b>fail expression</b> lines. If such line is found, test 
 * is marked as FAILED and appropriate Throwable is set to test result.</li>
 * </ol>
 * Method level declaration has precedence before class level. Class level has precedence before superclass and global setting. 
 * <br><br>
 * <b>Example code:</b>
 * <pre>
 * &#64;CheckLog()
 * public class AbstractTest { }
 * 
 * public class LogCheckEnabled extends AbstractTest {
 * 
 * 	&#64;CheckLog(enabled=false)
 * 	&#64;Test()
 * 	public void methodLogCheckDisabled() {}
 *
 * 	&#64;CheckLog(
 * 		LogFile(host="localhost",user="hudson",pass="${secret.property}",logFile="/tmp/output.log")
 * 		LogFile(host="otherhost",user="hudson",pass="${secret.property2}",logFile="${env:APP_HOME}/debug.log",filterExpression="INFO")
 * 	)
 * 	&#64;Test()
 * 	public void methodCheck2DifferentLogs() {}
 * 	}
 * 
 *	&#64;CheckLog(enabled=false)
 *	public class LogCheckDisabled extends AbstractTest { }
 * </pre>
 * 
 * @author lzoubek@redhat.com
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface CheckLog {
	/**
	 * remote log destination definitions (yes, we can observe more different log files on different hosts)
	 * @return
	 */
	 LogFile[] logs() default @LogFile;
	/**
	 * says whether checking is enabled. Set this to false to disable log checking for particular class or method
	 */
	boolean enabled() default true;
	/**
	 * enabled this option logs will be checked even for failed tests and found messages appended to failure stack-trace
	 * @return
	 */
	boolean assertFailed() default true;
	/**
	 * when enabling this option, all defined {@link LogFile} with {@link LogFile#grabMe()}
	 * will copied to <b>{@link TestNG#getOutputDirectory()}/logs</b> after whole suite is finished.
	 * {@link CheckLog#enabled()} annotation is ignored for grabbing
	 * @return
	 */
	boolean grabLogFiles() default false;
	
}
