package com.misha.scoreboard.web;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.misha.scoreboard.dto.SportEventRequestDto;
import com.misha.scoreboard.model.SportEvent;
import com.misha.scoreboard.service.SportEventServ;
import com.misha.scoreboard.utils.UtilMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * The controller handles HTTP requests from client, provides methods to execute
 * some operations on an SportEvent resource
 * 
 * @author Mykhaylo.T
 *
 */

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/events")
@Slf4j
public class SportEventCtrl {
	
	private UtilMapper utilMapper;
	private SportEventServ eventService;

	public SportEventCtrl(final SportEventServ service) {
		this.eventService = service;
	}
	
	@Autowired
	public void setUtilMapper(final UtilMapper modelMapper) {
		this.utilMapper = modelMapper;
	}
	
	/**
	 * Return all events from the database, if request parameter name
	 * is passed in URL, the response will be a list of events that contains the name for teamAway or teamHome.
	 * @param name
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@GetMapping
	public ResponseEntity<List<SportEvent>> getAll(@RequestParam(required = false) String name) {

		return ResponseEntity
				.ok((name != null && !name.isBlank()) ? eventService.findAll() : eventService.findAllByName(name));

	}
    
	/**
	 * Takes an event Id from the Url and performs operation to find item in the database.
	 * Automatically add a new Header response with the name "Version" to manage concurrent update
	 * of the resource.
	 * 
	 * @param eventId
	 * @return
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable("id") Long id, HttpServletRequest request) {
		SportEventRequestDto findById = eventService.findById(id);
		return ResponseEntity.ok().header("Version", findById.getVersion().toString())// version of the resource
				.body(findById);
	}
	
	/**
	 * Find the last event record saved in the database, using the updated column which is
	 * initialized automatically when the resource was created and then updated.
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@GetMapping("/last")
	public ResponseEntity<SportEventRequestDto > getLastUpdated() {
		return new ResponseEntity<>(eventService.findLastUpdated(),HttpStatus.FOUND);
	}
	
	/**
	 * Calls the EventService to persist the entity in the database,
	 * returns the URL of the resource in the HTTP Location header for the
	 * future retrieval.
	 * @param eventRequestDto
	 * @param response
	 * @param request
	 * @return
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SportEventRequestDto> create(@Valid @RequestBody SportEventRequestDto eventRequestDto,
														HttpServletResponse response,
			   											HttpServletRequest request,
														UriComponentsBuilder uriBuilder) throws InterruptedException, ExecutionException {
		log.info("creating new event:" + eventRequestDto.toString());
		
		SportEventRequestDto sportEventCreated = eventService.createEvent(eventRequestDto);
	
		UriComponents uriComponents = uriBuilder.path("/api/events/{id}").buildAndExpand(sportEventCreated.getId());
		response.setHeader("Location", uriComponents.toUri().toString());
	
		return new ResponseEntity<>(sportEventCreated,HttpStatus.CREATED);
	}
	
	/**	
	 * Finds the id and updates the entity with new value
	 * only if the request contains the header with the
	 * name Version and its value is equals to database's version, the client data will
	 * updated in the database.
	 * @param id
	 * @param dto
	 * @return
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	@PatchMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable("id") Long id,
									@RequestHeader("Version") Long version,
									@RequestBody SportEventRequestDto dto) throws InterruptedException, ExecutionException {
		try { 
			dto.setVersion(version);
			return ResponseEntity.ok(eventService.updateEvent(dto, version, id));
		} catch (Exception e) {
			log.error("Invalid version of the resource", e);
			
			return new ResponseEntity<>(eventService.currentLastVersion(dto,id), HttpStatus.CONFLICT);
		}
	}
	
}
