package com.misha.scoreboard.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * 	//weblux reactive use NON relational - 
	
	// crea la lista conessione stabilit: 
	
	
	
	// lista di tutti client registrati
	//subscribe
	
	//registra conessione e tiene traccia 
	// richiama endpoint 
	
	//typora
 */
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.misha.scoreboard.dto.SportEventRequestDto;

/** * 
 * Server Sent Event Service to manage unidirectional communication from the server to browser, 
 * saves all subscribers and then push data to them when {@link SportDataEvent.class} 
 * is saved or updated in the database.
 * 
 * @author Mykhaylo.T
 */

@Service
public class NotifyServ {
	
	private static final Logger LOG = LogManager.getLogger(NotifyServ.class);
	private Set<SseEmitter> sseEmitters = new HashSet<SseEmitter>();
    
	@Autowired
	private ExecutorService executorService;
	
	public SseEmitter subscribe () {
			return createEmitter();
	}
	
	public void publishData(SportEventRequestDto data) {
		LOG.info("...notify clients about the event: " + data.toString());
		
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonData;
			try {
				jsonData = objectMapper.writeValueAsString(data);
				executorService.execute(()->{
					sseEmitters.forEach(client -> {
						try {
							client.send(SseEmitter.event().name("eventSaved").data(jsonData));
						} catch (IOException e) {
							client.completeWithError(e);
						}
					});
				});
			} catch (JsonProcessingException e1) {
				e1.printStackTrace();
			}
	}
	
	private SseEmitter createEmitter() {
		final SseEmitter sseEmitter = new SseEmitter();
		
		sseEmitter.onCompletion(() -> {
			synchronized (this.sseEmitters) {
				this.sseEmitters.remove(sseEmitter);
			}
		});
	
		sseEmitter.onTimeout(()-> {
			sseEmitter.complete();
		});
		sseEmitters.add(sseEmitter);
	return sseEmitter;
	}
}
