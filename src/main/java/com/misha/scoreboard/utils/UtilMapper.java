package com.misha.scoreboard.utils;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.misha.scoreboard.dto.SportEventRequestDto;
import com.misha.scoreboard.model.SportEvent;
import com.misha.scoreboard.service.SportEventServ;

import lombok.NoArgsConstructor;

/**
 * Manages conversion between the Entity and DTO
 * @author Mykhaylo.T
 *
 */
@Component
@NoArgsConstructor
public class UtilMapper {
	
	@Autowired SportEventServ service;
	@Autowired ModelMapper modelMapper;
	
	/**
	 * Converts entity class to DTO
	 * @param entity
	 * @return
	 */
	public SportEventRequestDto convertToDto(SportEvent entity) {
		 SportEventRequestDto map = modelMapper.map(entity, SportEventRequestDto.class);
		 map.formatUpdateDate(entity.getUpdatingDate());
		return map;
	}
	
	/**
	 * Converts DTO class to an entity
	 * @param dto
	 * @return
	 */
	public SportEvent convertToEntity (SportEventRequestDto dto) {
		SportEvent event = modelMapper.map(dto,SportEvent.class);
		if(dto.getId() != null) {
			SportEventRequestDto eventOld = service.findById(dto.getId());
			event.setId(eventOld.getId());
		}
		return event;
	}
}
