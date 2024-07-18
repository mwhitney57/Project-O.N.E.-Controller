package dev.mwhitney.enums;

/**
 * 
 * Commands for the Project O.N.E. Controller.
 * 
 * @author Matthew Whitney
 *
 */
public enum ControllerCommand {
	/**
	 * <ul>
	 * 	<p>	<b><i>EXIT</i></b>
	 * 	<p> The controller command for exiting the application.
	 * </ul>
	 */
	EXIT,
	
	/**
	 * <ul>
	 * 	<p>	<b><i>GC</i></b>
	 * 	<p> The controller command for calling upon the Java garbage collector.
	 * </ul>
	 */
	GC,
	
	/**
	 * <ul>
	 * 	<p>	<b><i>CLEAR</i></b>
	 * 	<p>	The controller command for clearing the text output/console window.
	 * </ul>
	 */
	CLEAR,
	
	/**
	 * <ul>
	 * 	<p>	<b><i>VERSION</i></b>
	 * 	<p>	The controller command for getting the current application version.
	 * </ul>
	 */
	VERSION
}
