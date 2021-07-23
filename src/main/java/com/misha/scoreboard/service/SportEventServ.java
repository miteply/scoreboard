package com.misha.scoreboard.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Component;

import com.misha.scoreboard.dto.SportEventRequestDto;
import com.misha.scoreboard.model.SportEvent;

/**
 * SportEventService is the interface, 
 * that defines business functionality for {@link SportEvent}
 * @author Mykhaylo.T
 *
 */
@Component
public interface SportEventServ  {
	
	SportEvent getEventById(Long eventId);;
	CompletableFuture<List<SportEvent>> findAll();
	CompletableFuture<SportEvent> findById(Long id);
	CompletableFuture<SportEvent> createEvent(SportEvent event);
	CompletableFuture<Optional<SportEvent>> getLastUpdated();
	void notifyData(SportEventRequestDto event);
	

}
