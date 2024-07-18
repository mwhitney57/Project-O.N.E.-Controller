package dev.mwhitney.listeners;

import java.util.EventListener;

import dev.mwhitney.enums.ControllerCommand;
import dev.mwhitney.enums.ONECommand;

/**
 * 
 * A listener that handles requests between different components of the controller.
 * 
 * @author Matthew Whitney
 *
 */
public interface RequestListener extends EventListener {

	/**
	 * <ul>
	 * <p>	<b><i>messageRequested</i></b>
	 * <p>	<code>public void messageRequested(String message)</code>
	 * <p>	An interface method that invokes upon receiving a request to send the passed <tt>String</tt> message to the server.
	 * @param message - a <tt>String</tt> with the exact message intended for the server.
	 * </ul>
	 */
	public void messageRequested(String message);
	
	/**
	 * <ul>
	 * <p>	<b><i>commandRequested</i></b>
	 * <p>	<code>public void commandRequested(ONECommand command, String[] args)</code>
	 * <p>	An interface method that invokes upon receiving a request to send a command to the server.
	 * @param command - a {@link ONECommand} with the requested command.
	 * @param args - a <tt>String</tt> array containing command arguments. This <b>can</b> be <code>null</code> if there are no arguments.
	 * </ul>
	 */
	public void commandRequested(ONECommand command, String[] args);
	
	/**
	 * <ul>
	 * <p>	<b><i>controllerChangeRequested</i></b>
	 * <p>	<code>public void controllerChangeRequested(ControllerCommand command)</code>
	 * <p>	An interface method that invokes upon receiving a request to change the state of the controller.
	 * @param command - a {@link ControllerCommand} with the requested controller command.
	 * </ul>
	 */
	public void controllerChangeRequested(ControllerCommand command);
}
