package io.github.gabriel.alex;

import java.util.Date;

/**
 * 
 * @author Alex Gabriel
 *
 */
public class Workshop {
	/**
	 * Short URI of the instance of workshop (part of the URI after #)
	 */
	private String shortURI;
	private String title;
	private String description;
	private String stage;
	private String organizer;
	private String[] creativeStrategy;
	private Date beginDate;
	private Date endDate;
	
	
	/**
	 * Constructor Workshop - create an instance of workshop with 
	 * @param shortURI
	 */
	Workshop(String shortURI){
			setShortURI(shortURI);	
	}
	/**
	 * Default constructor 
	 */
	Workshop(){	}

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
	 * @return the creativeStrategy
	 */
	public String[] getCreativeStrategy() {
		return creativeStrategy;
	}
	/**
	 * @param creativeStrategy the creativeStrategy to set
	 */
	public void setCreativeStrategy(String[] creativeStrategy) {
		this.creativeStrategy = creativeStrategy;
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
	
	
}
