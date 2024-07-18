package dev.mwhitney.gui;

/**
 * 
 * A simple class representing a text entry that keeps track of a <tt>String</tt> and the time it was created.
 * 
 * @author Matthew Whitney
 * 
 */
public class Entry {

	//	Variables
	/** A <code>long</code> for the time this <tt>Entry</tt> was created in nanoseconds. */
	private long creationTime;
	/** A <tt>String</tt> for the text associated with this <tt>Entry</tt>. */
	private String entryText;
	
	/**
	 * <ul>
	 * <p>	<b><i>Entry</i></b>
	 * <p>	<code>public Entry(String text)</code>
	 * <p>	Creates a new <tt>Entry</tt>, saving the time it was created.
	 * @param text - the <tt>Entry</tt>'s text.
	 * </ul>
	 */
	public Entry(String text) {
		creationTime = System.nanoTime();
		entryText = text;
	}
	
	/**
	 * <ul>
	 * <p>	<b><i>getCreationTime</i></b>
	 * <p>	<code>public long getCreationTime()</code>
	 * <p>	Gets the system time at which this <tt>Entry</tt> was created in nanoseconds.
	 * @return a <code>long</code> with the creation time.
	 * </ul>
	 */
	public long getCreationTime() {
		return creationTime;
	}
	
	/**
	 * <ul>
	 * <p>	<b><i>getText</i></b>
	 * <p>	<code>public String getText()</code>
	 * <p>	Gets this <tt>Entry</tt>'s text.
	 * @return a <tt>String</tt> with the text.
	 * </ul>
	 */
	public String getText() {
		return entryText;
	}
	
	/**
	 * <ul>
	 * <p>	<b><i>setText</i></b>
	 * <p>	<code>public void setText(String text)</code>
	 * <p>	Sets this <tt>Entry</tt>'s text.
	 * @param text - a <tt>String</tt> with the new text.
	 * </ul>
	 */
	public void setText(String text) {
		entryText = text;
	}
}
