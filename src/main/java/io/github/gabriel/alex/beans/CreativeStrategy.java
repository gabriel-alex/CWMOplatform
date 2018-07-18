package io.github.gabriel.alex.beans;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class CreativeStrategy {
	
	/**
	 * Name of the instance of Creative Strategy
	 */
	private String name;
	
	/**
	 * Short URI of the creative strategy in the ontology - part of the URI after
	 * the #
	 */
	private String shortURI;
	private String workshop; 
	private String description;
	private List<String> facilitators;
	private List<String> activities;
	private String stage;
	
	/**
	 * Constructor CreativeStrategy - create an instance of creative strategy with a short URI
	 * @param URI
	 */
	public CreativeStrategy(String URI) {
		setShortURI(shortURI);
		this.facilitators = new ArrayList<String>();
		this.activities = new ArrayList<String>();
	}
	
	/**
	 *  Constructor CreativeStrategy- create an empty instance of CreativeStrategy 
	 */
	public CreativeStrategy() {
		this.facilitators = new ArrayList<String>();
		this.activities = new ArrayList<String>();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @return the workshop
	 */
	public String getWorkshop() {
		return workshop;
	}

	/**
	 * @param workshop the workshop to set
	 */
	public void setWorkshop(String workshop) {
		this.workshop = workshop;
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

	/**
	 * @return the facilitators
	 */
	public List<String> getFacilitator() {
		return facilitators;
	}

	/**
	 * @param facilitators the facilitator to set
	 */
	public void setFacilitator(List<String> facilitator) {
		this.facilitators = facilitator;
	}
	
	/**
	 * 
	 * @param Add a facilitator in the list
	 */
	public void addFacilitator(String fcltr) {
		this.facilitators.add(fcltr);
	}

	/**
	 * @return the activities
	 */
	public List<String> getActivities() {
		return activities;
	}

	/**
	 * @param activities the activities to set
	 */
	public void setActivities(List<String> activities) {
		this.activities = activities;
	}
	
	public void addActivities(String act) {
		this.activities.add(act);
	}
	
}
