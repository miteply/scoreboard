package com.misha.scoreboard.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.misha.scoreboard.exception.SportEventNotFoundException;
import com.misha.scoreboard.model.SportEvent;
import com.misha.scoreboard.repository.SportEventRepo;
import com.misha.scoreboard.service.SportEventServ;

/**
 * @author Mykhaylo.T
 *
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest
class EventControllerTest {
	
	private SportEvent	event1 = new SportEvent("Misha", "Jonik", 1, 0);
	private SportEvent	event2 = new SportEvent("Jonik", "Misha", 1, 0);

	@Autowired
	private WebApplicationContext webApplicationContext;
	private MockMvc mockMvc;
	@MockBean 
	private SportEventServ eventServiceMock;
	@Autowired
	SportEventRepo sportEventRepository;
	
	
	@BeforeEach
	void setUp() throws Exception {
		this.mockMvc = webAppContextSetup(webApplicationContext).build();
	}
	
	@Test
	public void givenEvent_thenReturnNameHome() throws Exception {		
		sportEventRepository.save(event1);
		Assertions.assertEquals("Misha",event1.getTeamHome());
	}

	@Test
	@DisplayName("Test findById Not Found")
	public void getEventWithUnknownIdShouldReturn404() throws Exception {
		
		when(eventServiceMock.findById(99L)).thenThrow(new SportEventNotFoundException("ID 99 not found"));
		
		this.mockMvc.perform(get("/api/events/99"))
		.andExpect(status().isNotFound());
	}
}
