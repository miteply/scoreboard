package com.misha.scoreboard.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * The SportEvent model class with attributes of the event, 
 * defines the table in the database
 * 
 * @author Mykhaylo.T
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "event")
@Component
@EntityListeners(AuditingEntityListener.class)
public class SportEvent implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Version
	@NotNull
	private Long version;
	
	@Column (name ="teamHome", nullable = false)
	private String teamHome;
	
	@Column (name ="teamAway", nullable = false)
	private String teamAway;
	
	@Column (name ="scoreHome", nullable = false)
	private Integer scoreHome;
	
	@Column (name ="scoreAway", nullable = false)
	private Integer scoreAway;
	
	@CreatedDate
	@Column(name = "creationDate", updatable = false, nullable = false)
	private String creationDate;
   
	@LastModifiedDate
    @Column(name = "updatingDate",nullable = false)
	private Date updatingDate;
	
	public SportEvent(String teamNameA, String teamNameB, Integer scoreA, Integer scoreB) {
		this.teamHome = teamNameA;
		this.teamAway = teamNameB;
		this.scoreHome = scoreA;
		this.scoreAway = scoreB;
	}
	
}
