package com.misha.scoreboard.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.misha.scoreboard.dto.SportEventRequestDto;
import com.misha.scoreboard.exception.SportEventNotFoundException;
import com.misha.scoreboard.model.SportEvent;
import com.misha.scoreboard.repository.SportEventRepo;
import com.misha.scoreboard.service.NotifyServ;
import com.misha.scoreboard.service.SportEventServ;

import lombok.extern.slf4j.Slf4j;

/**
 *The {@link EventService} implementation class, that calls 
 *interface {@link SportEventRepo} methods to perform operations into the database.
 *
 * @author Mykhaylo.T
 *
 */
@Service
@Slf4j
public class SportEventServImpl implements SportEventServ {
	
	private SportEventRepo eventRepository;
	private NotifyServ notifyService;
		
	@Autowired
	public void setServerSentService(final NotifyServ serverSentEventService) {
		this.notifyService = serverSentEventService;
	}
	
	@Autowired
	public void setEventRepository(final SportEventRepo eventRepository) {
		this.eventRepository = eventRepository;
	}
	
	public String getWelcomeMessage() {
		return "GLAD TO MEET YOU!!! <br> Thank you for wasted time, enjoy the life.";
	}

	@Override
	public SportEvent getEventById(Long eventId) {
		return eventRepository.findById(eventId)
				.orElseThrow(()-> new SportEventNotFoundException(String.format("Event with id: %s not found", eventId)));
	}
	
	@Override
	public CompletableFuture<SportEvent> createEvent(SportEvent event) {
		return CompletableFuture.supplyAsync(() -> {
			return eventRepository.save(event);
		});
	}

	@Override
	public CompletableFuture<SportEvent> findById(Long id) {
		return CompletableFuture.supplyAsync(() -> {
			return eventRepository.findById(id).orElseThrow(RuntimeException::new);
		});
	}
	
	@Override
	public CompletableFuture<List<SportEvent>> findAll() {
		return CompletableFuture.supplyAsync(() -> {
			return eventRepository.findAll();
		});
	}
	
	@Override
	public CompletableFuture<Optional<SportEvent>> getLastUpdated() {
		return  findAll().thenApply(e -> e.stream().filter(event -> event.getUpdatingDate() != null)
				.max(Comparator.comparing(SportEvent::getUpdatingDate)));
	}
	
	@Override
	public void notifyData(SportEventRequestDto event) {
		notifyService.publishData(event);
	}

}
