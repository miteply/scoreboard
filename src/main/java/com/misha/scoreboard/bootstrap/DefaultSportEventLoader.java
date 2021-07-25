package com.misha.scoreboard.bootstrap;


import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.misha.scoreboard.model.SportEvent;
import com.misha.scoreboard.service.NotifyServ;
import com.misha.scoreboard.service.SportEventServ;
import com.misha.scoreboard.utils.UtilMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Loads the initial data in the H2 in memory database.
 * @author Mykhaylo.T
 *
 */
@Slf4j
@Component
public class DefaultSportEventLoader implements CommandLineRunner {

	private SportEvent	footbalEvent1 = new SportEvent("Jonik", "Misha", 1, 0);
	private SportEvent	footbalEvent2 = new SportEvent("Misha", "Jonik", 1, 0);
	private SportEvent  templateEvent = new SportEvent("TeamA", "TeamB", 10, 100);
	
	@Autowired private SportEventServ sportEventService;
	@Autowired private UtilMapper utilMapper;
	@Autowired private NotifyServ notifyService;
	@Autowired private ExecutorService executor;
	@Autowired private Faker fakerData;
	
	private volatile static int total;//saved events in the database
	
	@Override
	public void run(String... args) throws Exception {
		log.info("Starting to save init data ...");
		//loadSportEvents();
	}
	
	/*private void loadSportEvents() {
			executor.submit(() -> {
			 //basic template which notify the inserted data 
			 sportEventService.createEvent(templateEvent)
			 				  .thenApply(e -> utilMapper.convertToDto(e))
			 				  .thenAccept(e -> {
			 					  	
			 				  		total++;
			 				   });
			  persistInitialRandomDataIntoDatabase();
		    });
	}*/
	
	private void persistInitialRandomDataIntoDatabase()  {
			/*sportEventService.createEvent(footbalEvent1);
			total++;
			sportEventService.createEvent(footbalEvent2);
			total++;
			
			for(int i = 0; i < 3; i++) {
				sportEventService.createEvent(initRandomSportsEventData(fakerData.team().name(),
																  fakerData.team().name(),
																  fakerData.number().randomDigit(),
																  fakerData.number().randomDigit()));
			}
			
		log.info(String.format("Total %s sport events are saved in the database", total));
		log.info("last inserted:"  + sportEventService.findLastUpdated());*/
	}

	private SportEvent initRandomSportsEventData(final String nameA, String nameB, Integer scoreA, Integer scoreB) {
		total++;
		return SportEvent.builder().teamHome(nameA).teamAway(nameB).scoreHome(scoreA).scoreAway(scoreB).build();
	}
}
