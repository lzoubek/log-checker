package com.redhat.qe.tools.checklog;

/**
 * This annotation defines a log destination. To define remote log destination please specify user, host and pass.
 * If you leave user or host default values it means we are working on your local box.
 * Then you need to specify a path to log file.
 * <b>ANY</b> of above properties can contain:
 * <ul>
 * <li><b>${system.property}</b> to reference java system property</li>
 * <li><b>${env:VARIABLE}</b> to reference environment variable</li>
 * </ul>
 * These are processed by {@link CheckLogTestNGListener} and replaced with right values at runtime
 * @author lzoubek@redhat.com
 *
 */
public @interface LogFile {	
	/**
	 * id of this annotation. Can be referenced from sub-class using {@link LogFile#refId()}. Reference means
	 * that this LogFile is used as a base for applying values or just referenced
	 * @return
	 */
	String id() default "";
	/**
	 * reference id of a LogFile annotation that must be either in one of super classes or on class level (in case this annotation
	 * is on method level) By specifying this property you are re-using all properties from referenced annotation and only override by
	 * setting some in this annotation.
	 * @return
	 */
	String refId() default "";
	/**
	 * hostname or IP where to find remote log file. Defaults mean local log file 
	 * <br><br>
	 * <b>${env:VARIABLE}</b> can be used to reference environment variable<br>
	 * <b>${system.property}</b> can be used to reference java system property
	 */
	String host() default "";	
	/**
	 * user to login via SSH, Defaults mean local log file
	 */
	String user() default "";
	/**
	 * user's pasword
	 * <br> {@link LogFile#keyfile()} has priority over {@link LogFile#pass()} If both set, password is
	 * treated as a paraphrase to a keyfile
	 */
	String pass() default "";
	/**
	 * user's private keyfile (set {@link LogFile#pass()} as well to use keyfile with non-empty paraprhase)  
	 * @return
	 */
	String keyfile() default "";
	/**
	 * path to watched log file relative to {@link LogFile#user()} HOME or absolute path 
	 */
	String logFile() default "${env:REMOTE_LOGFILE}";
	/**
	 * an expression passed to <b>grep</b> tool to detect BAD lines in log file
	 */
	String failOn() default "\' ERROR \'";
	/**
	 * an expression passed to <b>grep</b> tool to ignore possibly BAD lines (matching {@link LogFile#failOn()}) in log file
	 */
	String ignore() default "";
}
