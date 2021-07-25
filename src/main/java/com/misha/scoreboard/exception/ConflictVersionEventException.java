package com.misha.scoreboard.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Manage exception conflicts when  a PATCH request is used.
 * Concurrent update is achieved by the attribute @Version declared in 
 * {@link SportEvent.class}. To update the resource 
 * the client's version should match with the server's version. 
 * 
 * 
 * 
 * @author Mykhaylo.T
 *
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictVersionEventException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public ConflictVersionEventException(String e) {
		super(e);
	}	

}
