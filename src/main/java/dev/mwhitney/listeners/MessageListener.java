package dev.mwhitney.listeners;

import java.util.EventListener;

/**
 * 
 * A slimmed-down version of the message listener used in the Project O.N.E. local system.
 * 
 * @author Matthew Whitney
 *
 */
public interface MessageListener extends EventListener {
	
	/**
	 * <ul>
	 * <p>	<b><i>messageReceived</i></b>
	 * <p>	<code>public void messageReceived(String message)</code>
	 * <p>	An interface method that invokes upon receiving a standard message from the server.
	 * <p>	This method takes a <tt>String</tt> containing the message sent by the server.
	 * @param message - a <tt>String</tt> containing the standard message sent by the server.
	 * </ul>
	 */
	public void messageReceived(String message);
	
	/**
	 * <ul>
	 * <p>	<b><i>broadcastReceived</i></b>
	 * <p>	<code>public void broadcastReceived(String broadcastMessage)</code>
	 * <p>	An interface method that invokes upon receiving a broadcast message from the server.
	 * <p>	This method takes a <tt>String</tt> containing the broadcast message relayed by the server.
	 * <p>	<b>Note:</b> The passed broadcast message contains solely the content and has the type prefix removed from the <tt>String</tt>.
	 * @param broadcastMessage - a <tt>String</tt> containing the broadcast message relayed by the server.
	 * </ul>
	 */
	public void broadcastReceived(String broadcastMessage);
	
	/**
	 * <ul>
	 * <p>	<b><i>connectionMessageReceived</i></b>
	 * <p>	<code>public void connectionMessageReceived(String connectionMessage)</code>
	 * <p>	An interface method that invokes upon receiving a connection message from the server.
	 * <p>	This method takes a <tt>String</tt> containing the connection message sent by the server.
	 * <p>	<b>Note:</b> The passed connection message contains solely the content and has the type prefix removed from the <tt>String</tt>.
	 * @param connectionMessage - a <tt>String</tt> containing the connection message sent by the server.
	 * </ul>
	 */
	public void connectionMessageReceived(String connectionMessage);
	
	/**
	 * <ul>
	 * <p>	<b><i>notificationReceived</i></b>
	 * <p>	<code>public void notificationReceived(String notificationMessage)</code>
	 * <p>	An interface method that invokes upon receiving a notification message from the server.
	 * <p>	This method takes a <tt>String</tt> containing the notification message sent by the server.
	 * <p>	<b>Note:</b> The passed notification message contains solely the content and has the type prefix removed from the <tt>String</tt>.
	 * @param notificationMessage - a <tt>String</tt> containing the notification message sent by the server.
	 * </ul>
	 */
	public void notificationReceived(String notificationMessage);
	
	/**
	 * <ul>
	 * <p>	<b><i>responseMessageReceived</i></b>
	 * <p>	<code>public void responseMessageReceived(String responseMessage)</code>
	 * <p>	An interface method that invokes upon receiving a response message from the server.
	 * <p>	This method takes a <tt>String</tt> containing the response message sent by the server.
	 * <p>	<b>Note:</b> The passed response message contains solely the content and has the type prefix removed from the <tt>String</tt>.
	 * @param responseMessage - a <tt>String</tt> containing the response message sent by the server.
	 * </ul>
	 */
	public void responseMessageReceived(String responseMessage);
}
