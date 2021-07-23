package com.misha.scoreboard.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Contains all info about data event request that will be converted the to
 * entity by {@link UtilMapper}
 * 
 * @author Mykhaylo.T
 *
 */
@Data
@NoArgsConstructor
public class SportEventRequestDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private Long id;
	@NotBlank(message = "Team Home name is mandatory")
	private String teamHome;
	@NotBlank(message = "Team Away name is mandatory")
	private String teamAway;
	@NotNull(message = "Score Home  is mandatory")
	private Integer scoreHome;
	@NotNull(message = "Score Awat is mandatory")
	private Integer scoreAway;
	
	private String updatingDate;

	public SportEventRequestDto(String teamNameA, String teamNameB, Integer scoreA, Integer scoreB) {
		this.teamHome = teamNameA;
		this.teamAway = teamNameB;
		this.scoreHome = scoreA;
		this.scoreAway = scoreB;
	}

	/**
	 * Format date to String
	 * @param date
	 * @param timezone
	 */
	public void formatUpdateDate(Date date) {
		if(date != null) {
			this.updatingDate = dateFormat.format(date);	
		}
	}

}
