package dev.mwhitney.remote;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.swing.Timer;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.neovisionaries.ws.client.WebSocketState;

import dev.mwhitney.gui.ControllerInterface;
import dev.mwhitney.listeners.MessageListener;

/**
 * 
 * A slightly altered version of the remote client used in the Project O.N.E. local system.
 * 
 * @author Matthew Whitney
 *
 */
public class RemoteClient {
	
	//	WebSocket Components
	/** The <tt>WebSocketFactory</tt> used to create <tt>WebSocket</tt> instances. */
	private WebSocketFactory webSocketFactory;
	/** The <tt>WebSocket</tt> used to communicate with the server. */
	private WebSocket webSocket;
	
	//	Listeners
	/** The <tt>MessageListener</tt> responsible for handling incoming messages. */
	private MessageListener messageListener;
	
	//	Variables
	/** A <code>long</code> for the last time a Ping! was sent to the server. */
	private long lastPingTime;
	/** A <code>boolean</code> for whether or not the client should attempt to reconnect if disconnected. */
	private boolean shouldReconnect = true;
	/** A <tt>Timer</tt> used for keeping the connection to the server alive. */
	private Timer keepAliveTimer = new Timer(20000, (actionEvent) -> {
		//	If the WebSocket is open, send a keep-alive message.
		if(webSocket.isOpen()) {
			webSocket.sendText("#connection=keep-alive");
		}
	});
	/** A <tt>Timer</tt> used for reconnecting to the server if the connection is lost. */
	private Timer reconnectTimer = new Timer(5000, (actionEvent) -> {
		//	Attempt to reconnect.
		if(!webSocket.isOpen() && webSocket.getState() != WebSocketState.CONNECTING) {
			setupClient();
			connectToServer();
		}
	});
	
	
	/**
	 * <ul>
	 * <p>	<b><i>RemoteClient</i></b>
	 * <p>	<code>public RemoteClient()</code>
	 * <p>	Creates a new <tt>RemoteClient</tt>.
	 * </ul>
	 */
	public RemoteClient() {		
		//	WebSocket & Server Connection Setup
		webSocketFactory = new WebSocketFactory();
		webSocketFactory.setVerifyHostname(false);
		setupClient();
		connectToServer();
		
		//	General Setup
		keepAliveTimer.start();
		reconnectTimer.setInitialDelay(50);
		reconnectTimer.setDelay(5000);
	}
	
	/**
	 * <ul>
	 * <p>	<b><i>setupClient</i></b>
	 * <p>	<code>private void setupClient()</code>
	 * <p>	Sets up a standard client that communicates with the server.
	 * <p>	This method creates a new <tt>WebSocket</tt> instance and configures it appropriately.
	 * 		It does not, however, connect to the server with that newly created instance.
	 * 		To connect to the server, use the dedicated <code>connectToServer()</code> method.
	 * </ul>
	 */
	private void setupClient() {
		try {
			webSocket = webSocketFactory.createSocket("wss://one-server.minimunch57.club");
		} catch (IOException ioe) {
			System.out.println(ControllerInterface.CONSOLE_ERROR + "<!> Error creating WebSocket instance. (setCli)");
			ioe.printStackTrace();
		}
		webSocket.addHeader("token", System.getenv("PROJECT_ONE_CONTROLLER"));
		webSocket.addListener(new WebSocketAdapter() {
			//	Connected to the server.
			@Override
			public void onConnected(WebSocket webSocket, Map<String, List<String>> headers) throws Exception {
				System.out.println("<#> Connected to the server.");
				
				//	Don't continue any existing reconnection attempts.
				if(reconnectTimer.isRunning()) {
					reconnectTimer.stop();
				}
				
				//	Send first keep-alive message.
				webSocket.sendText("#connection=keep-alive");
			}
			//	Disconnected from the server.
			@Override
			public void onDisconnected(WebSocket webSocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
				System.out.println("<#> Disconnected from the server.");
				
				//	Set sleep time before attempting to reconnect.
				if(shouldReconnect) {
					reconnectTimer.restart();
				}
				else {
					//	Ensure that future disconnects may automatically reconnect.
					shouldReconnect = true;
				}
			}
			//	Received a message from the server.
			@Override
			public void onTextMessage(WebSocket webSocket, String message) throws Exception {
				//	Fire the appropriate listener method based on the received message's type.
				message = message.trim();
				if(message.startsWith("#broadcast")) {
					messageListener.broadcastReceived(message.replaceFirst("#broadcast=", "").trim());
				}
				else if(message.startsWith("#connection")) {
					messageListener.connectionMessageReceived(message.replaceFirst("#connection=", "").trim());
				}
				else if(message.startsWith("#command")) {
					final String trimmedMessage = message.replaceFirst("#command=", "").trim();
					if(trimmedMessage.startsWith("notification:")) {
						messageListener.notificationReceived(trimmedMessage.replaceFirst("notification:", ""));
					}
					else {
						// Unknown or Unhandled Command
						messageListener.messageReceived(message);
					}
				}
				else if(message.startsWith("#response")) {
					message = message.replaceFirst("#response=", "").trim();
					
					if(message.equalsIgnoreCase("Pong!")) {
						final int timeDifference = (int) ((System.nanoTime() - lastPingTime) / 1000000);
						message = message.concat(" (" + timeDifference + " ms)");
					}
					messageListener.responseMessageReceived(message);
				}
				else {
					messageListener.messageReceived(message);
				}
			}
		});
	}
	
	/**
	 * <ul>
	 * <p>	<b><i>connectToServer</i></b>
	 * <p>	<code>public void connectToServer()</code>
	 * <p>	Connects to the communications server asynchronously using the current <tt>WebSocket</tt> instance.
	 * </ul>
	 */
	public void connectToServer() {
		//	Attempt to connect to the server asynchronously.
		new Thread(() -> {
			try {
				webSocket.connect();
			} catch (WebSocketException wse) {
				System.out.println(ControllerInterface.CONSOLE_ERROR + "<!> Could not connect to server. (Is it down?)");
			}
		}).start();
	}
	
	/**
	 * <ul>
	 * <p>	<b><i>reconnectToServer</i></b>
	 * <p>	<code>public void reconnectToServer()</code>
	 * <p>	Reconnects to the communications server using a new <tt>WebSocket</tt> instance.
	 * </ul>
	 */
	public void reconnectToServer() {
		if((!isConnected() && !isConnecting()) && !reconnectTimer.isRunning()) {
			reconnectTimer.restart();
		}
	}
	
	/**
	 * <ul>
	 * <p>	<b><i>disconnectFromServer</i></b>
	 * <p>	<code>public void disconnectFromServer()</code>
	 * <p>	Disconnects from the communications server.
	 * <p>	This is a helper method for calling <code>disconnectFromServer(boolean)</code> with a value of false.
	 * 		This means that any calls to this method will not result in automatic reconnection attempts afterwards.
	 * </ul>
	 */
	public void disconnectFromServer() {
		disconnectFromServer(false);
	}
	
	/**
	 * <ul>
	 * <p>	<b><i>disconnectFromServer</i></b>
	 * <p>	<code>public void disconnectFromServer(boolean reconnect)</code>
	 * <p>	Disconnects from the communications server and attempts to reconnect if the passed <code>boolean</code> is <code>true</code>.
	 * @param reconnect - a <code>boolean</code> for whether or not the client should attempt to reconnect when disconnected.
	 * </ul>
	 */
	public void disconnectFromServer(boolean reconnect) {
		//	Ensure the controller will not attempt to reconnect.
		shouldReconnect = reconnect;
		if(reconnectTimer.isRunning()) {
			reconnectTimer.stop();
		}
		
		webSocket.disconnect();
	}
	
	/**
	 * <ul>
	 * <p>	<b><i>isConnected</i></b>
	 * <p>	<code>public boolean isConnected()</code>
	 * <p>	Returns a <code>boolean</code> for whether or not this <tt>RemoteClient</tt> is connected to the server.
	 * @return <code>true</code> if connected to the server; <code>false</code> otherwise.
	 * </ul>
	 */
	public boolean isConnected() {
		return webSocket.isOpen();
	}
	
	/**
	 * <ul>
	 * <p>	<b><i>isConnecting</i></b>
	 * <p>	<code>public boolean isConnecting()</code>
	 * <p>	Returns a <code>boolean</code> for whether or not this <tt>RemoteClient</tt> is connecting to the server.
	 * @return <code>true</code> if connecting to the server; <code>false</code> otherwise.
	 * </ul>
	 */
	public boolean isConnecting() {
		return webSocket.getState()==WebSocketState.CONNECTING;
	}
	
	/**
	 * <ul>
	 * <p>	<b><i>isAttemptingConnection</i></b>
	 * <p>	<code>public boolean isAttemptingConnection()</code>
	 * <p>	Returns a <code>boolean</code> for whether or not this <tt>RemoteClient</tt> is attempting to connect to the server.
	 * <p>	This method differs from <code>isConnecting()</code>. It also checks if reconnection attempts are being made or are planning to be made after a set time.
	 * @return <code>true</code> if attempting to connect to the server; <code>false</code> otherwise.
	 * </ul>
	 */
	public boolean isAttemptingConnection() {
		return isConnecting() || reconnectTimer.isRunning();
	}
	
	/**
	 * <ul>
	 * <p>	<b><i>pingServer</i></b>
	 * <p>	<code>public void pingServer()</code>
	 * <p>	Pings the server, saving the time in nanoseconds immediately after the message is sent.
	 * <p>	The nanosecond time save is to keep track of the time it takes for a ping to reach the server, and for a response to be received.
	 * 		The time is not saved if the message fails to send for whatever reason, such as the server not being connected.
	 * 		When a "Pong!" response is received from the server, the nanosecond time is retrieved again.
	 * 		The ping is calculated by subtracting the first saved time from the second time.
	 * 		Keep in mind that this logic is not performed within this method.
	 * 		<b>This method just sends the "Ping!" and saves the first nanosecond time.</b>
	 * <p>	This pinging method uses RTT (Round Trip Time). End-to-End would only be from the controller to the server or vice-versa.
	 * 		That is not how this function helps to calculate the ping time.
	 */
	public void pingServer() {
		if(sendMessage("#ping")) lastPingTime = System.nanoTime();
	}
	
	/**
	 * <ul>
	 * <p>	<b><i>sendMessage</i></b>
	 * <p>	<code>public boolean sendMessage(String message)</code>
	 * <p>	Sends the passed <tt>String</tt> message to the server.
	 * @param message - the message to be sent to the server in the form of a <tt>String</tt>.
	 * @return <code>true</code> if the message was sent successfully; <code>false</code> otherwise.
	 * </ul>
	 */
	public boolean sendMessage(String message) {
		if(webSocket.isOpen()) {
			webSocket.sendText(message);
			return true;
		}
		System.out.println(ControllerInterface.CONSOLE_ERROR + "The server is not connected.");
		return false;
	}
	
	/**
	 * <ul>
	 * <p>	<b><i>setMessageListener</i></b>
	 * <p>	<code>public void setMessageListener(MessageListener ml)</code>
	 * <p>	Sets the one and only <tt>MessageListener</tt> to be used by this <tt>RemoteClient</tt>.
	 * @param ml - the <tt>MessageListener</tt> to be used by this <tt>RemoteClient</tt>.
	 * </ul>
	 */
	public void setMessageListener(MessageListener ml) {
		messageListener = ml;
	}
}
