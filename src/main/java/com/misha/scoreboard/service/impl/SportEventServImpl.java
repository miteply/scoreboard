package com.misha.scoreboard.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.misha.scoreboard.dto.SportEventRequestDto;
import com.misha.scoreboard.exception.ConflictVersionEventException;
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
	@Transactional
	public SportEventRequestDto createEvent(SportEventRequestDto event) {
		
		try {
			
			CompletableFuture<SportEventRequestDto> createdEvent = CompletableFuture
					.supplyAsync(() -> {
										return eventRepository.save(utilMapper.convertToEntity(event));})
					.thenApply(e -> utilMapper.convertToDto(e));
			createdEvent.thenAccept(e -> notifyData(e));
			
			return createdEvent.get();
		} catch (InterruptedException | ExecutionException e1) {
			log.error("", e1);
			e1.printStackTrace();
			return null;
		}
		
	}

	/**
	 * Server adds the unique Version value in header response when the client performs
	 * request to get data. It's helpful to avoid the concurrent
	 * updating. Before update the data in the database, the client must perform the get
	 * request to obtain the version of the resource returned by the server in the response header.
	 * @return
	 */
	@Override
	public SportEventRequestDto updateEvent(SportEventRequestDto dto, Long version, Long id) {

		SportEvent eventEntity = eventRepository.findById(id).get();

		if (dto != null && !isLastVersion(version, eventEntity.getVersion())) 
			throw new ConflictVersionEventException("The request could not be completed due to a conflict with the current "
					+ "state of the target resource, should pass the last version...");
		
		
		SportEventRequestDto updatedDto = utilMapper
				.convertToDto(eventRepository.save(dataToUpdate(id, dto, eventEntity)));
		notifyData(updatedDto);
		
		return updatedDto;
	}
	
	/**
	 * When the conflicts occurs the map will be returned in 
	 * {@link SpoerEventCtrl.class}.
	 * It contains the last version of the resource aved in the database and 
	 * the client resource to update. The resolve the conflict the client should send 
	 * the last version of the resource {@link SportEvent.class}.
	 */
	public Map<String, Object> currentLastVersion(SportEventRequestDto currentVersion, Long id) {
		Map<String, Object> eventVersions = new ConcurrentHashMap<String, Object>();
		eventVersions.put("clientVersion", currentVersion);
		eventVersions.put("serverVersion", eventRepository.findById(id));
		
		return eventVersions;
	}
	 
	/**
	 * check the version of the resource if a data to update has not been modified 
	 * by some one else in the meantime.
	 * @param event
	 */
	private boolean isLastVersion(Long clientVersion, Long serverVersion) {
		return clientVersion == serverVersion ? true : false;
	}
	
	/**
	 * Update the entity with only receive values from the client
	 * @param dto
	 * @param id
	 * @return
	 */
	private SportEvent dataToUpdate(Long id, SportEventRequestDto dto, SportEvent entityToUpdate) {
		if(requireUpdate(dto.getTeamHome()))
			entityToUpdate.setTeamHome(dto.getTeamHome());
		if(requireUpdate(dto.getTeamAway()))
			entityToUpdate.setTeamAway(dto.getTeamAway());
		if(requireUpdate(String.valueOf(dto.getScoreHome())))
			entityToUpdate.setScoreHome(dto.getScoreHome());
		if(requireUpdate(String.valueOf(dto.getScoreAway())))
			entityToUpdate.setScoreAway(dto.getScoreAway());
		
		return entityToUpdate;
	}
	
	/**
	 * Check is the fields needs to be updated
	 * @param data
	 * @return
	 */
	private boolean requireUpdate(String data) {
		return data != null && !data.isEmpty();
	}
	
	/**
	 * Notify client about the event 
	 * @param e
	 */
	private void notifyData(SportEventRequestDto e) {
		notifyService.publishData(e);
	}

}
