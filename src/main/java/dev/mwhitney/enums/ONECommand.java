package dev.mwhitney.enums;

/**
 * 
 * Commands for the Project O.N.E. system.
 * 
 * @author Matthew Whitney
 *
 */
public enum ONECommand {
	/**
	 * <ul>
	 * 	<p>	<b><i>OPEN</i></b>
	 * 	<p> The command for unlocking the door by retracting the solenoid.
	 * 
	 * 	<p> Alias: <b>UNLOCK</b>
	 * </ul>
	 */
	OPEN, 
	
	/**
	 * <ul>
	 * 	<p>	<b><i>CLOSE</i></b>
	 * 	<p> The command for locking the door by letting the solenoid extend.
	 * 
	 * 	<p> Alias: <b>LOCK</b>
	 * </ul>
	 */
	CLOSE,
	
	/**
	 * <ul>
	 * 	<p>	<b><i>SYSTEM_LOCK</i></b>
	 * 	<p> The command for locking the door lock system.
	 * </ul>
	 */
	SYSTEM_LOCK,
	
	/**
	 * <ul>
	 * 	<p>	<b><i>SYSTEM_UNLOCK</i></b>
	 * 	<p> The command for unlocking the door lock system.
	 * </ul>
	 */
	SYSTEM_UNLOCK,
	
	/**
	 * <ul>
	 * 	<p>	<b><i>MANUALUNLOCKS_DISABLE</i></b>
	 * 	<p>	The command for disabling manual unlocks on the system.
	 * </ul>
	 */
	MANUALUNLOCKS_DISABLE,
	
	/**
	 * <ul>
	 * 	<p>	<b><i>MANUALUNLOCKS_ENABLE</i></b>
	 * 	<p>	The command for enabling manual unlocks on the system.
	 * </ul>
	 */
	MANUALUNLOCKS_ENABLE,
	
	/**
	 * <ul>
	 * 	<p> <b><i>POKE</i></b>
	 * 	<p>	The command for poking (notifying) any active client/controller users.
	 *  <p> It is unlikely to have use in the opposite direction (Client --> System).
	 * </ul>
	 */
	POKE,
	
	/**
	 * <ul>
	 * 	<p>	<b><i>CONNECT</i></b>
	 * 	<p> The command for connecting to the server.
	 * </ul>
	 */
	CONNECT,
	
	/**
	 * <ul>
	 * 	<p>	<b><i>DISCONNECT</i></b>
	 * 	<p> The command for disconnecting from the server.
	 * </ul>
	 */
	DISCONNECT,
	
	/**
	 * <ul>
	 * 	<p>	<b><i>RECONNECT</i></b>
	 * 	<p> The command for reconnecting to the server.
	 * </ul>
	 */
	RECONNECT,
	
	/**
	 * <ul>
	 * 	<p>	<b><i>PING</i></b>
	 * 	<p> The command for pinging the server.
	 * </ul>
	 */
	PING
}
