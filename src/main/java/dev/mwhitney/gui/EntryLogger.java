package dev.mwhitney.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 
 * A class for logging, managing, and retrieving text entries.
 * 
 * @author Matthew Whitney
 * 
 */
public class EntryLogger {
	
	//	Variables
	/** An <code>int</code> for the currently selected <tt>Entry</tt>. */
	private int index;
	/** A <code>boolean</code> for whether or not the entries will expire after a set amount of time. */
	private boolean entriesExpire;
	/** A <code>float</code> for the amount of time each <tt>Entry</tt> should last in minutes. */
	private float expireTime;
	/** An <code>int</code> for the maximum amount of entries this <tt>EntryLogger</tt> is supposed to hold. */
	private int maxSize;
	
	//	Core Objects
	/** An <tt>Entry</tt> technically classified as index -1. It is for the text entry in-progress. */
	private Entry currentEntry;
	/** A synchronized <tt>List</tt> of this <tt>EntryLogger</tt>'s <tt>Entry</tt> objects. */
	private List<Entry> entries;
	/** A <tt>Thread</tt> responsible for checking for and removing expired <tt>Entry</tt> objects. */
	private Thread expireCheckThread;

	/**
	 * <ul>
	 * <p>	<b><i>EntryLogger</i></b>
	 * <p>	<code>public EntryLogger(int size, boolean expire)</code>
	 * <p>	Creates a new <tt>EntryLogger</tt>.
	 * @param size - an <code>int</code> for the maximum amount of entries to log and store at a time.
	 * @param expire - a <code>boolean</code> for whether or not the entries managed by this <tt>EntryLogger</tt> should expire.
	 * </ul>
	 */
	public EntryLogger(int size, boolean expire) {
		index = -1;
		entriesExpire = expire;
		expireTime = 5.0f;
		maxSize = size;
		
		currentEntry = new Entry("");
		entries = Collections.synchronizedList(new ArrayList<Entry>());
		expireCheckThread = new Thread() {
			@Override
			public void run() {
				while(!isInterrupted()) {
					//	Check for expired entries.
					synchronized(entries) {
						final Iterator<Entry> iter = entries.iterator();
						while(iter.hasNext()) {
							final Entry e = iter.next();
							if(isExpired(e)) {
								iter.remove();
							}
						}
					}
					
					//	Ensure the index stays within the size of the list.
					if(index > getSize() - 1) index = getSize() - 1;
					
					//	Sleep
					try {
						Thread.sleep(5000);
					} catch (InterruptedException ie) {
						interrupt();
						ie.printStackTrace();
					}
				}
			}
		};
		expireCheckThread.start();
	}
	
	/**
	 * <ul>
	 * <p>	<b><i>EntryLogger</i></b>
	 * <p>	<code>public EntryLogger()</code>
	 * <p>	Creates a new <tt>EntryLogger</tt>.
	 * <p>	This constructor uses a default maximum log size of <code>20</code>.
	 * 		Use {@link #EntryLogger(int)} to adjust this size.
	 * </ul>
	 */
	public EntryLogger() {
		this(20, true);
	}
	
	/**
	 * <ul>
	 * <p>	<b><i>log</i></b>
	 * <p>	<code>public void log(String text)</code>
	 * <p>	Creates a new <tt>Entry</tt> with the passed <tt>String</tt> text.
	 * 		This method also removes the oldest <tt>Entry</tt> if the new size would be greater than the maximum size.
	 * @param text - the <tt>String</tt> of text to log as an <tt>Entry</tt>.
	 * </ul>
	 */
	public void log(String text) {
		if(hasEntries() && getSize() >= getMaxSize()) {
			entries.remove(getSize() - 1);
		}
		entries.add(0, new Entry(text));
		currentEntry.setText("");
		resetIndex();
	}
	
	/**
	 * <ul>
	 * <p>	<b><i>retrievePrevious</i></b>
	 * <p>	<code>public String retrievePrevious(String currentText)</code>
	 * <p>	Retrieves the previous entry based on the current <code>index</code>.
	 * <p>	This method will increment the <tt>EntryLogger</tt>'s internal <code>index</code> and return the text from the <tt>Entry</tt> at that index.
	 * 		If the current index is <code>-1</code> when this method is called, then the passed <tt>String</tt> will be saved for later at that index.
	 * 		Regardless, the method will retrieve the text, assuming the new <code>index</code> would remain inside the allowed range.
	 * 		If the new index would go outside of this range, then this method will continue to return the text from the <tt>Entry</tt> at that respective boundary.
	 * 		If there are no entries logged, then this method will return an empty <tt>String</tt>.
	 * @param currentText - a <tt>String</tt> for the entered, but unconfirmed, text. Typically what is in the entry text field.
	 * @return a <tt>String</tt> with the previous entry, if available, based on the current index.
	 * </ul>
	 */
	public String retrievePrevious(String currentText) {
		if(hasEntries()) {
			if(index == -1) {
				currentEntry.setText(currentText);
			}
			index =  Math.max(-1, Math.min(index + 1, getSize() - 1));
			return entries.get(index).getText();
		}
		return "";
	}
	
	/**
	 * <ul>
	 * <p>	<b><i>retrieveFollowing</i></b>
	 * <p>	<code>public String retrieveFollowing()</code>
	 * <p>	Retrieves the following entry based on the current <code>index</code>.
	 * <p>	This method will decrement the <tt>EntryLogger</tt>'s internal <code>index</code> and return the text from the <tt>Entry</tt> at that index.
	 * 		If the new index will be <code>-1</code>, then the saved <tt>Entry</tt>'s <tt>String</tt> will be returned.
	 * 		Otherwise, the method will retrieve the text, assuming the new <code>index</code> would remain inside the allowed range.
	 * 		If the new index would go outside of this range, then this method will continue to return the text from the <tt>Entry</tt> at that respective boundary.
	 * 		If there are no entries logged, then this method will return an empty <tt>String</tt>.
	 * @return a <tt>String</tt> with the following entry, if available, based on the current index.
	 * </ul>
	 */
	public String retrieveFollowing() {
		if(hasEntries()) {
			index = Math.max(-1, Math.min(index - 1, getSize() - 1));
			if(index == -1) {
				return currentEntry.getText();
			}
			else {
				return entries.get(index).getText();
			}
		}
		return "";
	}
	
	/**
	 * <ul>
	 * <p>	<b><i>resetIndex</i></b>
	 * <p>	<code>private void resetIndex()</code>
	 * <p>	Resets the current selection index back to <code>-1</code>, the default value.
	 * <p>	<b>Note:</b> The default index value of <code>-1</code> does not correspond to any <tt>Entry</tt> that is added or that expires.
	 * 		It corresponds to a singular, internal <tt>Entry</tt> that keeps track of any in-progress text entry that has yet to be submitted and logged.
	 * </ul>
	 */
	private void resetIndex() {
		index = -1;
	}
	
	/**
	 * <ul>
	 * <p>	<b><i>clear</i></b>
	 * <p>	<code>public void clear()</code>
	 * <p>	Clears every <tt>Entry</tt> from this <tt>EntryLogger</tt>.
	 * </ul>
	 */
	public void clear() {
		entries.clear();
	}
	
	/**
	 * <ul>
	 * <p>	<b><i>isExpired</i></b>
	 * <p>	<code>private boolean isExpired(Entry e)</code>
	 * <p>	Checks if the passed <tt>Entry</tt> is expired, meaning it has met or exceeded the set expire time.
	 * <p>	<b>Note:</b> An <tt>Entry</tt> <i>can</i> become 'unexpired.'
	 * 		This method checks if the <tt>Entry</tt> has existed longer than the set expire time <b>when it is called</b>.
	 * 		This means that, if the expire time is changed before this method is called, an <tt>Entry</tt> can then become expired or unexpired.
	 * @param e - the <tt>Entry</tt> to check for expiration.
	 * @return <code>true</code> if the passed <tt>Entry</tt> has expired; <code>false</code> otherwise.
	 * </ul>
	 */
	private boolean isExpired(Entry e) {
		return (TimeUnit.MINUTES.convert(System.nanoTime() - e.getCreationTime(), TimeUnit.NANOSECONDS) > expireTime);
	}
	
	/**
	 * <ul>
	 * <p>	<b><i>doEntriesExpire</i></b>
	 * <p>	<code>public boolean doEntriesExpire()</code>
	 * <p>	Checks if this <tt>EntryLogger</tt>'s entries expire.
	 * @return <code>true</code> if the entries expire after a set amount of time; <code>false</code> otherwise.
	 * </ul>
	 */
	public boolean doEntriesExpire() {
		return entriesExpire;
	}
	
	/**
	 * <ul>
	 * <p>	<b><i>setEntriesExpire</i></b>
	 * <p>	<code>public void setEntriesExpire(boolean expire)</code>
	 * <p>	Sets whether or not this <tt>EntryLogger</tt> is to have its entries expire after the set amount of time.
	 * @param expire - a <code>boolean</code> for whether or not the entries will expire after a set amount of time.
	 * </ul>
	 */
	public void setEntriesExpire(boolean expire) {
		entriesExpire = expire;
	}
	
	/**
	 * <ul>
	 * <p>	<b><i>getExpireTime</i></b>
	 * <p>	<code>public float getExpireTime()</code>
	 * <p>	Gets the set amount of time that entries last for before expiring.
	 * 		The expiration time is the essentially the length of time that each entry should last (A.K.A. lifespan).
	 * 		It should not be confused with a typical expiration date or time, which is a set <i>period in time</i>.
	 * @return a <code>float</code> for the amount of time each <tt>Entry</tt> should last in minutes.
	 * </ul>
	 */
	public float getExpireTime() {
		return expireTime;
	}
	
	/**
	 * <ul>
	 * <p>	<b><i>setExpireTime</i></b>
	 * <p>	<code>public void setExpireTime(float time)</code>
	 * <p>	Sets the amount of time that entries last for before expiring.
	 * @param time - the amount of time each <tt>Entry</tt> should last in minutes.
	 * </ul>
	 */
	public void setExpireTime(float time) {
		expireTime = time;
	}
	
	/**
	 * <ul>
	 * <p>	<b><i>hasEntries</i></b>
	 * <p>	<code>public boolean hasEntries()</code>
	 * <p>	Checks if this <tt>EntryLogger</tt> has any entries currently logged.
	 * @return <code>true</code> if there are <b>any</b> entries logged at the time of the request; <code>false</code> otherwise.
	 * </ul>
	 */
	public boolean hasEntries() {
		return (getSize() > 0);
	}

	/**
	 * <ul>
	 * <p>	<b><i>getMaxSize</i></b>
	 * <p>	<code>public int getMaxSize()</code>
	 * <p>	Gets the <b>current</b> amount of entries this <tt>EntryLogger</tt> is holding.
	 * @return an <code>int</code> for the current amount of entries held.
	 * </ul>
	 */
	public int getSize() {
		return entries.size();
	}
	
	/**
	 * <ul>
	 * <p>	<b><i>getMaxSize</i></b>
	 * <p>	<code>public int getMaxSize()</code>
	 * <p>	Gets the <b>maximum</b> amount of entries this <tt>EntryLogger</tt> will hold.
	 * @return an <code>int</code> for the maximum amount of entries.
	 * </ul>
	 */
	public int getMaxSize() {
		return maxSize;
	}

	/**
	 * <ul>
	 * <p>	<b><i>setMaxSize</i></b>
	 * <p>	<code>public void setMaxSize(int size)</code>
	 * <p>	Sets the <b>maximum</b> amount of entries this <tt>EntryLogger</tt> will hold.
	 * @param size - an <code>int</code> for the maximum amount of entries.
	 * </ul>
	 */
	public void setMaxSize(int size) {
		maxSize = size;
	}
}
