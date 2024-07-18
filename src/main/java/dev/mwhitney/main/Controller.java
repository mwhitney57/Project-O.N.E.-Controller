package dev.mwhitney.main;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import dev.mwhitney.enums.ControllerCommand;
import dev.mwhitney.enums.ONECommand;
import dev.mwhitney.gui.ControllerInterface;
import dev.mwhitney.listeners.MessageListener;
import dev.mwhitney.listeners.RequestListener;
import dev.mwhitney.remote.RemoteClient;

/**
 * 
 * The class responsible for managing the entire Project O.N.E. Controller.
 * 
 * @author Matthew Whitney
 *
 */
public class Controller {
	
	//	Static Return Values
	/** A <code>float</code> for the current controller version. */
	final public static String CURRENT_VERSION = "1.4.3";
	/** A <tt>String</tt> for the path to the application's file folder. */
	@SuppressWarnings("unused")
	final private static String APP_FOLDER_PATH = System.getProperty("user.home") + "/Minimunch57/ProjectONEController/";
	
	//	Primary Objects
	/** The <tt>ControllerInterface</tt> GUI that can be displayed to easily communicate with the server. */
	private ControllerInterface controllerGUI;
	/** The <tt>RemoteClient</tt> used to communicate with the server. */
	private RemoteClient remoteClient;
	/** The <tt>TrayIcon</tt> that handles the pop-up menu and exists on the system tray. */
	private TrayIcon trayIcon;
	
	/**
	 * <ul>
	 * <p>	<b><i>Controller</i></b>
	 * <p>	<code>public Controller()</code>
	 * <p>	Creates a new <tt>Controller</tt>.
	 * <p>	NOTE: There is only intended to be one instance of this class operating at any given time.
	 * </ul>
	 */
	public Controller() {
		//	Create controller interface and request listener.
		try {
			SwingUtilities.invokeAndWait(() -> {
				controllerGUI = new ControllerInterface();
			});
		} catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		}
		setupRequestListener();
		
		//	Connect to the server.
		remoteClient = new RemoteClient();
		setupMessageListener();
		
		//	Create and set up the tray icon.
		setupTray();
	}
	
	/**
	 * <ul>
	 * <p>	<b><i>setupTray</i></b>
	 * <p>	<code>private void setupTray()</code>
	 * <p>	Sets up the tray icon and all of its components.
	 * <p>	<b>Note:</b> This method is only meant to be called once by the <code>main()</code> method.
	 * </ul>
	 */
	private void setupTray() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		//	Setup the PopupMenu.
		final PopupMenu popMenu = new PopupMenu("Project O.N.E. Controller");
		//	Setup the TrayIcon
		trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().getImage(Controller.class.getResource("/club/minimunch57/images/trayIcon.png")), "Project O.N.E. Controller", popMenu);
		trayIcon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				if(mouseEvent.getButton() == MouseEvent.BUTTON1) {
					if(mouseEvent.getClickCount() == 3) {
						handleCommand(ONECommand.OPEN, null);
					}
				}
			}
		});

		//	Create MenuItems and add ActionListeners.
		final MenuItem quickOpenItem = new MenuItem("Quick Open");
		quickOpenItem.addActionListener((actionEvent) -> {
			handleCommand(ONECommand.OPEN, null);
		});
		
		final MenuItem quickLockItem = new MenuItem("Quick Close");
		quickLockItem.addActionListener((actionEvent) -> {
			handleCommand(ONECommand.CLOSE, null);
		});
		
		final MenuItem aboutItem = new MenuItem("About");
		aboutItem.addActionListener((actionEvent) -> {
			JOptionPane.showMessageDialog(null, "Project O.N.E. Controller v" + CURRENT_VERSION + "\n\nCreated by Matthew Whitney", "Project O.N.E.",
					JOptionPane.INFORMATION_MESSAGE, new ImageIcon(Controller.class.getResource("/club/minimunch57/images/icon.png")));
		});
		
		final MenuItem settingsItem = new MenuItem("Open Interface");
		settingsItem.addActionListener((actionEvent) -> {
			//	If the controller GUI is already displayed, send it to the front. Otherwise, display it.
			controllerGUI.display();
		});
		
		final MenuItem exitItem = new MenuItem("Exit");
		exitItem.addActionListener((actionEvent) -> {
			closeController();
		});
		
		//	Add items to the pop-up menu.
		popMenu.add(quickOpenItem);
		popMenu.add(quickLockItem);
		popMenu.addSeparator();
		popMenu.add(aboutItem);
		popMenu.add(settingsItem);
		popMenu.add(exitItem);
		
		//	Add the tray icon to the system's tray.
		try {
			SystemTray.getSystemTray().add(trayIcon);
		} catch (AWTException awte) {
			awte.printStackTrace();
		}
	}
	
	/**
	 * <ul>
	 * <p>	<b><i>setupRequestListener</i></b>
	 * <p>	<code>private void setupRequestListener()</code>
	 * <p>	Sets up the request listener to receive requests.
	 * <p>	<b>Note:</b> This method is only meant to be called once by the <code>main()</code> method.
	 * </ul>
	 */
	private void setupRequestListener() {
		if(controllerGUI != null) {
			controllerGUI.setRequestListener(new RequestListener() {
				@Override
				public void messageRequested(String message) {
					if(remoteClient != null) {
						remoteClient.sendMessage(message);
					}
				}
				
				@Override
				public void commandRequested(ONECommand command, String[] args) {
					handleCommand(command, args);
				}
				
				@Override
				public void controllerChangeRequested(ControllerCommand command) {
					handleCommand(command);
				}
			});
		}
	}
	
	/**
	 * <ul>
	 * <p>	<b><i>setupMessageListener</i></b>
	 * <p>	<code>private void setupMessageListener()</code>
	 * <p>	Sets up the message listener to receive messages from the server.
	 * <p>	<b>Note:</b> This method is only meant to be called once by the <code>main()</code> method.
	 * </ul>
	 */
	private void setupMessageListener() {
		if(remoteClient != null) {
			remoteClient.setMessageListener(new MessageListener() {
				@Override
				public void messageReceived(String message) {
					System.out.println("!M:" + message);
				}

				@Override
				public void broadcastReceived(String broadcastMessage) {
					trayIcon.displayMessage("Broadcast Received", broadcastMessage, MessageType.NONE);
					System.out.println("!B:" + broadcastMessage);
				}
				
				@Override
				public void connectionMessageReceived(String connectionMessage) {
					if(!connectionMessage.equals("keep-alive")
					&& !connectionMessage.equals("CONNECTED")
					&& !connectionMessage.equals("DISCONNECTED")) {
						System.out.println("!C:" + connectionMessage);
					}
				}
				
				@Override
				public void notificationReceived(String notificationMessage) {
					if(notificationMessage.toUpperCase().startsWith("POKE")) {
						trayIcon.displayMessage("Poke Received!", notificationMessage.toUpperCase(), MessageType.NONE);
						notificationMessage = "Poke!";
					}
					System.out.println("!N:" + notificationMessage);
				}

				@Override
				public void responseMessageReceived(String responseMessage) {
					System.out.println("!R:" + responseMessage);
				}
			});
		}
	}
	
	/**
	 * <ul>
	 * <p>	<b><i>handleCommand</i></b>
	 * <p>	<code>private boolean handleCommand(ONECommand command, String[] args)</code>
	 * <p>	Handles system commands from the controller.
	 * @param command - the {@link ONECommand} to handle.
	 * @param args - the command arguments, if any. This <b>can</b> be <code>null</code>.
	 * @return <code>true</code> if the command was valid and handled; <code>false</code> otherwise.
	 * </ul>
	 */
	private boolean handleCommand(ONECommand command, String[] args) {
		//	The print prefix, which may change depending on the command.
//		String printPrefix = "";
		
		//	Handle Commands
		switch (command) {
			case OPEN: {
				//	In case of zero arguments, default to 3 seconds.
				int seconds = 3;
				if(args != null) {
					try {
						seconds = Math.max(Math.min(9, Integer.valueOf(args[0])), 1);
					} catch(NumberFormatException nfe) {
						System.out.println(ControllerInterface.CONSOLE_ERROR + "Invalid argument for command: OPEN");
						break;
					}
				}
				remoteClient.sendMessage("#command=!security:unlock " + seconds);
				System.out.println("> SENT --> Open (" + seconds + ")");
				break;
			}
			case CLOSE: {
				remoteClient.sendMessage("#command=!security:lock");
				System.out.println("> SENT --> Close");
				break;
			}
			case SYSTEM_UNLOCK: {
				remoteClient.sendMessage("#command=!security:system:unlock");
				System.out.println("> SENT --> System Unlock");
				break;
			}
			case SYSTEM_LOCK: {
				remoteClient.sendMessage("#command=!security:system:lock");
				System.out.println("> SENT --> System Lock");
				break;
			}
			case MANUALUNLOCKS_DISABLE: {
				remoteClient.sendMessage("#command=!security:manualunlocks:disable");
				System.out.println("> SENT --> Disable Manual Unlocks");
				break;
			}
			case MANUALUNLOCKS_ENABLE: {
				remoteClient.sendMessage("#command=!security:manualunlocks:enable");
				System.out.println("> SENT --> Enable Manual Unlocks");
				break;
			}
			case POKE: {
				remoteClient.sendMessage("#command=notification:poke");
				System.out.println("> SENT --> Poke");
				break;
			}
			case CONNECT: {
				if(!remoteClient.isConnected()) {
					remoteClient.reconnectToServer();
				}
				else {
					System.out.println(ControllerInterface.CONSOLE_ERROR + "Already connected to the server.");
				}
				break;
			}
			case DISCONNECT: {
				if(remoteClient.isConnected() || remoteClient.isAttemptingConnection()) {
					remoteClient.disconnectFromServer(false);
				}
				else {
					System.out.println(ControllerInterface.CONSOLE_ERROR + "Not connected to the server.");
				}
				break;
			}
			case RECONNECT: {
				if(!remoteClient.isConnected()) {
					remoteClient.reconnectToServer();
				}
				else {
					remoteClient.disconnectFromServer(true);
				}
				break;
			}
			case PING: {
				remoteClient.pingServer();
				break;
			}
			default:
				return false;
		}
		return true;
	}
	
	/**
	 * <ul>
	 * <p>	<b><i>handleCommand</i></b>
	 * <p>	<code>private boolean handleCommand(ControllerCommand command)</code>
	 * <p>	Handles controller commands.
	 * @param command - the {@link ControllerCommand} to handle.
	 * @return <code>true</code> if the command was valid and handled; <code>false</code> otherwise.
	 * </ul>
	 */
	private boolean handleCommand(ControllerCommand command) {
		//	Handle Commands
		switch (command) {
			//	Exit the application.
			case EXIT: {
				closeController();
				break;
			}
			//	Call the garbage collector in an effort to free up some memory.
			case GC: {
				System.gc();
				break;
			}
			//	Clear the output text/console window.
			case CLEAR: {
				controllerGUI.clearConsole();
				break;
			}
			//	Get the current application version.
			case VERSION: {
				//	Handled in ControllerInterface.
				break;
			}
			default:
				return false;
		}
		return true;
	}
	
	/**
	 * <ul>
	 * <p>	<b><i>closeController</i></b>
	 * <p>	<code>private void closeController()</code>
	 * <p>	Closes the controller, disconnecting from the server and removing the icon from the tray.
	 * </ul>
	 */
	private void closeController() {
		remoteClient.disconnectFromServer();
		System.exit(0);
	}
}
