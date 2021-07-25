package com.misha.scoreboard.web;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.misha.scoreboard.service.NotifyServ;

/**
 * Server Sent Event provides the endpoint to create a client 
 * connections for real time messaging 
 * @author Mykhaylo.T
 *
 */

@RestController
public class SseCtrl {
	
	private NotifyServ notifyService;
	
	public SseCtrl (NotifyServ notifyService) {
		this.notifyService = notifyService;
	}
	
	/**
	 * Creates client connection, 
	 * handles GET request and produces text/event-stream
	 * @return 
	 * @return SseEmitter
	 */
	@CrossOrigin 
	@GetMapping(value = "/sse/receive",
			    produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public SseEmitter subscribeForRealTimeAction() {
		return notifyService.subscribe();
	}
}
