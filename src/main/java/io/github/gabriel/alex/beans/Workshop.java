package io.github.gabriel.alex.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

/**
 * 
 * @author Alex Gabriel
 *
 */
@Component
public class Workshop {
	/**
	 * Short URI of the instance of workshop (part of the URI after #)
	 */
	private String shortURI;
	private String title;
	private String description;
	private String stage;
	private String organizer;
	private List<String> creativeStrategies;
	private Date beginDate;
	private Date endDate;
	private List<String> subjects;
	private int numberOfIdeas;
	
	
	/**
	 * Constructor Workshop - create an instance of workshop with 
	 * @param shortURI
	 */
	public Workshop(String shortURI){
			setShortURI(shortURI);	
			this.creativeStrategies = new ArrayList<String>();
			this.subjects = new ArrayList<String>();
	}
	/**
	 * Default constructor 
	 */
	public Workshop(){
		this.creativeStrategies = new ArrayList<String>();
		this.subjects = new ArrayList<String>();
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the shortURI
	 */
	public String getShortURI() {
		return shortURI;
	}
	/**
	 * @param shortURI the shortURI to set
	 */
	public void setShortURI(String shortURI) {
		this.shortURI = shortURI;
	}

	/**
	 * @return the beginDate
	 */
	public Date getBeginDate() {
		return beginDate;
	}
	/**
	 * @param beginDate the beginDate to set
	 */
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the organizer
	 */
	public String getOrganizer() {
		return organizer;
	}
	/**
	 * @param organizer the organizer to set
	 */
	public void setOrganizer(String organizer) {
		this.organizer = organizer;
	}
	public void addCreativeStrat(String shortUri) {
		this.creativeStrategies.add(shortUri);
	}
	/**
	 * @return the creativeStrategies
	 */
	public List<String> getCreativeStrategies() {
		return creativeStrategies;
	}
	/**
	 * @param creativeStrategies the creativeStrategies to set
	 */
	public void setCreativeStrategies(List<String> creativeStrategies) {
		this.creativeStrategies = creativeStrategies;
	}
	/**
	 *  
	 * @param subject  to add to the list of subjects associated to the workshop
	 */
	public void addSubject(String subject) {
		this.subjects.add(subject);
	}
	/**
	 * @return the subjects
	 */
	public List<String> getSubjects() {
		return subjects;
	}
	/**
	 * @param subjects the subjects to set
	 */
	public void setSubjects(List<String> subjects) {
		this.subjects = subjects;
	}
	/**
	 * @return the stage
	 */
	public String getStage() {
		return stage;
	}
	/**
	 * @param stage the stage to set
	 */
	public void setStage(String stage) {
		this.stage = stage;
	}
	
}
