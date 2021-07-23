package com.misha.scoreboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.misha.scoreboard.model.SportEvent;

/**
 * Interacts with {@link SportEvent} entity from the database,
 * 
 * @author Mykhaylo.T
 *
 */
@Repository
public interface SportEventRepo extends JpaRepository<SportEvent, Long> {

}
