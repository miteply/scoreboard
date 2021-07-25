package com.misha.scoreboard.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.misha.scoreboard.dto.SportEventRequestDto;
import com.misha.scoreboard.model.SportEvent;

/**
 * SportEventService is the interface, 
 * that defines business functionality for {@link SportEvent}.
 *
 * @author Mykhaylo.T
 *
 */

@Component
public interface SportEventServ  {
	
	List<SportEvent> findAll(String name);
	SportEventRequestDto findById(Long id);
	SportEventRequestDto findLastUpdated();
	SportEventRequestDto createEvent(SportEventRequestDto event);
	SportEventRequestDto updateEvent(SportEventRequestDto event);
	
}
