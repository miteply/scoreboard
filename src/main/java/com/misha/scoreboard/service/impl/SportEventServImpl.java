package com.misha.scoreboard.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import com.misha.scoreboard.dto.SportEventRequestDto;
import com.misha.scoreboard.exception.SportEventNotFoundException;
import com.misha.scoreboard.model.SportEvent;
import com.misha.scoreboard.repository.SportEventRepo;
import com.misha.scoreboard.service.NotifyServ;
import com.misha.scoreboard.service.SportEventServ;
import com.misha.scoreboard.utils.UtilMapper;

import lombok.extern.slf4j.Slf4j;

/**
 *The {@link EventService} implementation class, used to handle the logic of 
 *inserting, updating and retrieving the data by calling {@link SportEventRepo}
 *
 * @author Mykhaylo.T
 *
 */

@Service
@Slf4j
public class SportEventServImpl implements SportEventServ {
	
	private SportEventRepo eventRepository;
	private NotifyServ notifyService;
	private UtilMapper utilMapper;
		
	@Autowired
	public void setServerSentService(final NotifyServ serverSentEventService) {
		this.notifyService = serverSentEventService;
	}
	@Autowired
	public void setEventRepository(final SportEventRepo eventRepository) {
		this.eventRepository = eventRepository;
	}
	@Autowired
	public void setUtilMapper(final UtilMapper mapper) {
		this.utilMapper = mapper;
	}
	

	
	//find all events with name only if not null
	@Override
	public List<SportEvent> findAll(String name) {
		if(name!= null && !name.isBlank()) {
			eventRepository.findAll(Example.of(SportEvent
					.builder()
					.teamHome(name)
					.teamAway(name)
					.build(), ExampleMatcher.matchingAny()));
		}
		return eventRepository.findAll();
	}
	
	@Override
	public SportEventRequestDto findById(Long id) {
		return utilMapper.convertToDto(eventRepository.findById(id).orElseThrow(()-> new SportEventNotFoundException("ID NOT VALID")));
	}
	
	@Override
	public SportEventRequestDto findLastUpdated() {
		return  findAll(null)
						.stream()
						.filter(event -> event.getUpdatingDate() != null)
						.max(Comparator.comparing(SportEvent::getUpdatingDate))
						.map(e -> utilMapper.convertToDto(e))
						.orElseThrow(RuntimeException::new);
	}
	
	@Override
	public SportEventRequestDto createEvent(SportEventRequestDto event) {
		CompletableFuture<SportEventRequestDto> createdEvent = CompletableFuture.supplyAsync(() -> {
			return eventRepository.save(utilMapper.convertToEntity(event));
		}).thenApply(e -> utilMapper.convertToDto(e));
		createdEvent.thenAccept(e -> notifyData(e));
		try {
			return createdEvent.get();
		} catch (InterruptedException | ExecutionException e1) {
			log.error("", e1);
			e1.printStackTrace();
			return null;
		}
		
	}

	@Override
	public SportEventRequestDto updateEvent(SportEventRequestDto event) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
	private void notifyData(SportEventRequestDto e) {
		notifyService.publishData(e);
	}




	
}
