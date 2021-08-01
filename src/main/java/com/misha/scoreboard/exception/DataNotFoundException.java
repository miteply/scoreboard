
package com.misha.scoreboard.exception;

/**
 * Manages  when no one events are available in the database
 * 
 * @author Mykhaylo.T
 *
 */
public class DataNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public DataNotFoundException () {
		super("NO DATA FOUND");
	}

}
