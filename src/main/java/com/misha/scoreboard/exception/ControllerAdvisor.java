package com.misha.scoreboard.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Interceptor of exceptions thrown by methods annotated 
 * with @RequestMapping.
 * Declares @ExceptionHandler to be shared across multiple @Controller class
 * Handles exceptions when an event is not found and when a data 
 * for a new event to be saved is not valid.
 * @author Mykhaylo.T
 *
 */
@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {
	
	/**
	 * This is a handler method for the SportEventNotFoundException. 
	 * Sends a ResponseEntity with a timestamp, error message and a status code to the client.
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(SportEventNotFoundException.class)
	public ResponseEntity<Object> handleSportEventNotFoundException(
			SportEventNotFoundException ex, WebRequest request) {
		
		Map<String,Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", "SportEvent not found");
		
		return new ResponseEntity<> (body, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(DataNotFoundException.class)
	public ResponseEntity<Object> handleDataNotFoundException(
			DataNotFoundException ex, WebRequest request) {
		
		Map<String,Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", "No sport events found");
		
		return new ResponseEntity<> (body, HttpStatus.NOT_FOUND);
	}
	
	/**
	 * Thrown when validation on an argument annotated @Valid fails-
	 */
	@Override						
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request){
		
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("status", status.value());
		
		List<String> errors = ex.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(e -> e.getDefaultMessage())
				.collect(Collectors.toList());
		
		body.put("errors", errors);
		
		return new ResponseEntity<> (body, HttpStatus.BAD_REQUEST);
	}

}
