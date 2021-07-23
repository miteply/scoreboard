package com.misha.scoreboard.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.misha.scoreboard.dto.SportEventRequestDto;
import com.misha.scoreboard.model.SportEvent;
/**
 * Tests mapping DTO <-> ENTITY
 * @author Mykhaylo.T
 *
 */
@SpringBootTest
class UtilMapperTest {
	
	@Autowired
	UtilMapper modelMapper;

    @Test
	@DisplayName("converting Entity to Dto")
	void whenConvertSportEventEntityToDto_thenCorrect() {
		SportEvent entity = new SportEvent("misha", "zeus", 1, 2);
		SportEventRequestDto dto = modelMapper.convertToDto(entity);
		
		Assertions.assertEquals(entity.getTeamHome(), dto.getTeamHome());
		Assertions.assertEquals(entity.getTeamAway(), dto.getTeamAway());
		Assertions.assertEquals(entity.getScoreHome(), dto.getScoreHome());
		Assertions.assertEquals(entity.getScoreAway(), dto.getScoreAway());
	}
	
	@Test
	@DisplayName("converting Dto to Entity")
	void whenConvertSportEventDtoToEntity_thenCorrect() {
		SportEventRequestDto dto = new SportEventRequestDto("misha", "zeus", 1, 2);
		SportEvent entity = modelMapper.convertToEntity(dto);
		
		Assertions.assertEquals(dto.getTeamHome(), entity.getTeamHome());
		Assertions.assertEquals(dto.getTeamAway(), entity.getTeamAway());
		Assertions.assertEquals(dto.getScoreHome(), entity.getScoreHome());
		Assertions.assertEquals(dto.getScoreAway(), entity.getScoreAway());
	}
}
